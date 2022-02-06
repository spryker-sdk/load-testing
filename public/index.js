const fs = require('fs-extra');
const path = require('path');
const glob = require("glob");
const rimraf = require("rimraf");
const fastify = require('fastify')({logger: true});
const dotenv = require("dotenv").config({ path: process.cwd() + '/.env' });
const scenarios = require('../resources/scenarios/scenarios.js');
let variables = require('../resources/js/variables');
const {getInstanceList, initTestCases, getTestCaseById, executeTestCase} = require('../resources/js/testCase');
const timeouts = require('../resources/js/timeout');

fastify.register(require('fastify-formbody'));
fastify.register(require('point-of-view'), {
    engine: {
        mustache: require('mustache'),
    },
    templates: 'resources/templates',
});

const partials = {
    partials: {
        header: 'header.mustache',
        footer: 'footer.mustache'
    }
};

fastify.register(require('fastify-static'), {
    root: path.join(__dirname, '/../' + variables.destinationFolder),
    prefix: variables.reportSuffix,
    decorateReply: false,
});

fastify.register(require('fastify-static'), {
    root: path.join(__dirname, '/../resources/css'),
    prefix: '/css',
    decorateReply: false,
});

fastify.register(require('fastify-static'), {
    root: path.join(__dirname, '/../resources/js'),
    prefix: '/js',
    decorateReply: false,
});

fastify.get('/', (req, reply) => {
    readReports();

    reply.view('list.mustache', {
        title: 'Load testing',
        scenarios: scenarios,
        projects: Array.from(variables.instanceList.values()),
        jobs: Array.from(variables.jobs.values()).length,
        reports: Array.from(variables.reports.values()),
        pendingJobs: variables.pendingJobs.size,
        amountOfTestCases: initTestCases().size,
    }, {...partials});
});

fastify.get('/reports', async (req, reply) => {
    await readReports();
    let list = Array.from(variables.reports.values()).map(report => ({...report, log: null}));

    reply.send(list);
});

fastify.get('/status', async (req, reply) => {
    let status = {
        total: initTestCases().size,
        pending: variables.pendingJobs.size,
        running: variables.jobs.size,
    }
    let list = [status];

    reply.send(list);
});

fastify.post('/reports', async(req, reply) => {
    await readReports();

    let data = JSON.parse(req.body);
    let reportPath = 'web/' + data.id + '/report.json';

    variables.reports.set(String(data.id), data);
    await fs.writeJson(reportPath, {...data, log: null});

    reply.code(200);
    reply.send('');
});

fastify.delete('/reports/*', (req, reply) => {
    let reportId = String(req.params['*']);

    let archivePath = variables.destinationFolder + '/archive/' + reportId;
    let reportPath = variables.destinationFolder + '/' + reportId;

    if (!variables.reports.has(reportId)) {
        reply.code(404);
        reply.send('');

        return;
    }

    if (fs.existsSync(archivePath)) {
        rimraf.sync(archivePath);
    }

    fs.mkdirs(archivePath);
    fs.rename(
        reportPath,
        archivePath
    );

    variables.reports.delete(reportId);

    reply.code(200);
    reply.send('');
});

fastify.get('/run', (req, reply) => {

    let template = variables.jobs.size === 0 ? 'run.mustache' : 'error.mustache';
    reply.view(template, {
        title: 'Run a test',
        errorMessage: 'Some tests are already running.',
        scenarios: scenarios,
        projects: Array.from(variables.instanceList.values()),
        jobs: Array.from(variables.jobs.values()),
        reports: Array.from(variables.reports.values()),
        pendingJobs: variables.pendingJobs.size,
        amountOfTestCases: initTestCases().size,
    }, {...partials});
});

fastify.post('/run', async (req, reply) => {
    let instance = req.body.instance;
    let testName = req.body.testName;
    let testType = req.body.testType;
    let targetRps = req.body.targetRps;
    let duration = req.body.duration;
    let description = req.body.description;
    let instanceNameRegex = /\(([a-zA-Z_-]+)\)/i;
    let found = instance.match(instanceNameRegex);
    let instanceName = Array.isArray(found) && found.length > 1 ? found[1] : "";

    if (!variables.instanceList.has(instance) || variables.jobs.size > 0) {
        reply.code(404);
        reply.send(`Project ${instance} is not found.`);
        return;
    }
    variables.pendingJobs = initTestCases();
    let testCase = getTestCaseById(testName);
    if (testCase == null) {
        reply.code(404);
        reply.send(`Given test ${testName} was not found.`);
        return;
    }
    variables.pendingJobs.clear();
    variables.pendingJobs.set(testCase.id, testCase);
    executeTestCase(testCase, instance, instanceName, testType, targetRps, duration, description);

    reply.redirect(302, `/`);
});

fastify.get('/clear', async (req, reply) => {
    variables.pendingJobs.clear();
    reply.redirect(302, `/`);
});

fastify.get('/drop', async (req, reply) => {
    await glob(`./${variables.destinationFolder}/*`, (er, files) => {
        for (const filesKey in files) {
            if (fs.lstatSync(files[filesKey]).isDirectory()) {
                fs.removeSync(files[filesKey]);
            }
        }
    });
    variables.reports.clear();
    reply.redirect(302, `/`);
});

fastify.get('/run_all', (req, reply) => {
    let template = variables.jobs.size === 0 ? 'run_all.mustache' : 'error.mustache';
    reply.view(template, {
        title: 'Run All Test',
        errorMessage: 'Some tests are already running.',
        scenarios: scenarios,
        projects: Array.from(variables.instanceList.values()),
        jobs: Array.from(variables.jobs.values()),
        reports: Array.from(variables.reports.values()),
        pendingJobs: variables.pendingJobs.size,
        amountOfTestCases: initTestCases().size,
    }, {...partials});
});

fastify.post('/run_all', async (req, reply) => {
    let instance = req.body.instance;
    let testType = req.body.testType;
    let targetRps = req.body.targetRps;
    let duration = req.body.duration;
    let description = req.body.description;
    let instanceNameRegex = /\(([a-zA-Z_-]+)\)/i;
    let found = instance.match(instanceNameRegex);
    let instanceName = Array.isArray(found) && found.length > 1 ? found[1] : "";

    if (!variables.instanceList.has(instance) || variables.jobs.size > 0) {
        reply.code(404);
        reply.send(`Project ${instance} is not found.`);
        return;
    }
    variables.pendingJobs = initTestCases();

    async function runAll(instance, instanceName, testType, targetRps, duration, description) {
        for (let i = 0; i < scenarios.length; i++) {
            if (scenarios[i].status === 'inactive') {
                continue;
            }
            for (let j = 0; j < scenarios[i].tests.length; j++) {
                if (!variables.pendingJobs.size) {
                    break;
                }
                let testCase = scenarios[i].tests[j]
                if (testCase != null) {
                    await executeTestCase(testCase, instance, instanceName, testType, targetRps, duration, description);
                    await new Promise(resolve => setTimeout(resolve, timeouts.executeNextTestAfter));
                }
            }
        }
        variables.pendingJobs.clear();
    }

    runAll(instance, instanceName, testType, targetRps, duration, description);

    reply.redirect(302, `/`);
});

fastify.get('/instances', (req, reply) => {
    let instanceKey = req.query['key'];
    let selectedInstance = {};

    if (variables.instanceList.has(instanceKey)) {
        selectedInstance = variables.instanceList.get(instanceKey);
    }

    reply.view('instance.mustache', {
        title: 'Instances',
        scenarios: scenarios,
        projects: Array.from(variables.instanceList.values()),
        selectedInstance: selectedInstance,
        formTitle: Object.entries(selectedInstance).length === 0 ? 'Add instance' : 'Edit instance',
    }, {...partials});
});

fastify.get('/instances/list', (req, reply) => {
    reply.send(Array.from(variables.instanceList.values()));
});

fastify.post('/instances', (req, reply) => {
    let key = req.body.key;
    let yves = req.body.yves;
    let glue = req.body.glue;
    let backendApi = req.body.backend_api;
    let feApi = req.body.fe_api;
    let newrelicApplication = req.body.newrelic_application;
    let newrelicApiKey = req.body.newrelic_api_key;
    let newrelicApplicationId = req.body.newrelic_application_id;
    let mockServer = req.body.mock_server;

    if (key !== "") {
        let project = Object.assign({},
            yves && {"yves": yves},
            glue && {"glue": glue},
            key && {"key": key},
            backendApi && {"backend_api": backendApi},
            mockServer && {"mock_server": mockServer},
            feApi && {"fe_api": feApi},
            newrelicApplication && {"newrelic_application": newrelicApplication},
            newrelicApiKey && {"newrelic_api_key": newrelicApiKey},
            newrelicApplicationId && {"newrelic_application_id": newrelicApplicationId},
        );

        variables.instanceStore.set(key, project);

        variables.instanceList = getInstanceList();
    }

    reply.redirect(302, '/instances');
});

fastify.delete('/instances/:instanceKey', (req, reply) => {
    let key = String(req.params['instanceKey']);
    let statusCode = 404;

    if (variables.instanceStore.has(key)) {
        variables.instanceStore.del(key);
        variables.instanceList = getInstanceList();
        statusCode = 200;
    }

    reply.code(statusCode);
    reply.send('');
});

fastify.get('/log/*', async (req, reply) => {
    let runId = String(req.params['*']);
    console.log(runId);
    if (!variables.reports.has(runId)) {
        reply.code(404);
        reply.send('');
        return;
    }
    let runObject = variables.reports.get(runId);
    let template = runObject.done ? 'history-log.mustache' : 'log.mustache';
    let log = runObject.log || await fs.readJson(`./${variables.destinationFolder}/${runObject.id}/report.log.json`);
    reply.view(template, {
        runObject,
        log,
        title: 'Console log',
        errorMessage: 'Some tests are already running.',
        scenarios: scenarios,
        projects: Array.from(variables.instanceList.values()),
        jobs: Array.from(variables.jobs.values()),
        reports: Array.from(variables.reports.values()),
        pendingJobs: variables.pendingJobs.size,
        amountOfTestCases: initTestCases().size,
    }, {...partials});
});

const readReports = async () => {
    await glob(`./${variables.destinationFolder}/!(archive)/**/report.json`, (er, files) => {
        Array.isArray(files) && files.map(file => {
            let runObject = fs.readJsonSync(file);
            try {
                runObject.newrelicLog = fs.readJsonSync(runObject.newrelicLogFilePath)
                runObject.slaStatus = runObject.newrelicLog.slaPassed;
            } catch (err) {}
            variables.reports.set(runObject.id, runObject);
        });
    });
};

// Run the server!
const start = async () => {
    try {
        await readReports();
        await fastify.listen(variables.port, variables.host, error => {
            if (error) {
                fastify.log.info(`Server start error: ${error.error}`)
                process.exit(1);
            }
        });
        fastify.log.info(`server listening on ${fastify.server.address().port}`)
    } catch (err) {
        fastify.log.error(err);
    }
};

start();
