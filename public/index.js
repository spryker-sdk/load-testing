const fs = require('fs-extra');
const path = require('path');
const glob = require("glob");
const {spawn} = require('child_process');
const dateFormat = require('date-format');
const rimraf = require("rimraf");
const fastify = require('fastify')({logger: true});
const dotenv = require("dotenv").config({ path: process.cwd() + '/.env' });
const port = process.env.PORT || 3000;
const host = process.env.HOST || '0.0.0.0';
const https = require('https');
const newrelicQuery='{\n  actor {\n    account(id: ACCOUNT_ID) {\n      nrql(query: \"SELECT count(*), max(duration)*1000, min(duration)*1000, average(duration)*1000 FROM Transaction WHERE spryker_router_resources_path=\u0027/TARGET_ROUTE/*\u0027 AND request.method = \u0027REQUEST_TYPE\u0027 AND appName = \u0027APPLICATION_NAME\u0027 SINCE \u0027SINCE_DATE_TIME\u0027 UNTIL \u0027UNTIL_DATE_TIME\u0027\") {\n        results\n      }\n    }\n  }\n}';
const scenarios = require('../resources/scenarios/scenarios.js');

const destinationFolder = 'web';
const reportSuffix = '/report';
const instanceStore = require('data-store')({path: process.cwd() + '/web/instance.json'});

const getInstanceList = () => {
    let projects = new Map(Object.entries(instanceStore.get()));
    projects.forEach((value, key) => value.key = key);

    return projects;
}

const getNewrelicTime = () => {
    return new Date().toISOString().replace("T", " ");
}

const initTestCases = () => {
    let storage = new Map();

    for (let i = 0; i < scenarios.length; i++) {
        if (scenarios[i].status === 'inactive') {
            continue;
        }
        for (let j = 0; j < scenarios[i].tests.length; j++) {
            let testCase = scenarios[i].tests[j]
            storage.set(testCase.id, testCase);
        }
    }
    return storage;
}

const jobs = new Map();
const reports = new Map();
let pendingJobs = new Map();
let instanceList = getInstanceList();

const getTestCaseById = (testCaseId) => {
    if (pendingJobs.has(testCaseId)) {
        return pendingJobs.get(testCaseId);
    }
    return null
}

async function executeTestCase(test, instance, instanceName, testType, targetRps, duration, description) {
    let startTime = getNewrelicTime();
    let testName = test.id;
    let title = `${testName}${testType}`;
    let project = instanceList.get(instance);
    let key = `${instance}/${testName}/${testType}/${targetRps}-RPS/` + dateFormat.asString('yyyy-MM-dd-hh-mm-ss');
    let reportPath = `/report/${key}/report/index.html`;
    let reportFolder = `./${destinationFolder}/${key}`;
    let newrelicLogFilePath = `${reportFolder}/newrelic.json`;
    await fs.mkdirs(reportFolder);

    let runObject = {
        id: key,
        done: false,
        exitCode: -1,
        when: new Date().getTime(),
        log: [],
        instance,
        testName,
        testType,
        targetRps,
        duration,
        description,
        title,
        reportPath,
        newrelicLogFilePath: newrelicLogFilePath,
        newrelicLog: [],
        newrelicLogPath: `/newrelic/${key}`,
        logPath: `/log/${key}`,
        valid: null,
    };

    jobs.set(key, runObject);
    reports.set(key, runObject);

    var env = Object.create(process.env);
    env.JAVA_OPTS = (process.env.JAVA_OPTS || '')
        + ` -DYVES_URL=${project.yves}`
        + ` -DGLUE_URL=${project.glue}`
        + ` -DFE_URL=${project.fe_api}`
        + ` -DBACKEND_API_URL=${project.backend_api}`
        + ` -DINSTANCE_NAME=${instanceName}`
        + ` -DDURATION=${duration}`
        + ` -DTARGET_RPS=${targetRps}`;

    description = description || '-';
    const run = spawn('./gatling/bin/gatling.sh', ['-sf=resources/scenarios/spryker', `-rd='${description}'`, `-rf=${reportFolder}`, `-s=spryker.${testName}${testType}`], {env: env});

    run.stdout.on('data', data => {
        runObject.log.push({
            entry: data.toString(),
            error: false
        });
    });

    run.stderr.on('data', data => {
        runObject.log.push({
            entry: data.toString(),
            error: true
        });
    });

    run.on('close', async code => {
        runObject.done = true;
        runObject.exitCode = code;
        runObject.success = code === 0;
        jobs.delete(key);
        pendingJobs.delete(key);
        if (!runObject.success) {
            await fs.remove(reportFolder);
            return;
        }

        await glob(`${reportFolder}/*/`, (er, directories) => {
            directories.map(async directory => fs.rename(directory, `${reportFolder}${reportSuffix}`));
        });

        await fs.writeJson(`${reportFolder}/report.json`, {...runObject, log: null});
        await fs.writeJson(`${reportFolder}/report.log.json`, runObject.log);

    });

    while (!runObject.done) {
        // fastify.log.info(`Job ID:  ${runObject.id} Job Status: ${runObject.done} Job error: ${runObject.error}`);
        await new Promise(resolve => setTimeout(resolve, 1000));
    }

    if (test.route.length > 0) {
        fastify.log.info(`Sleep before request to newrelic`);
        await new Promise(resolve => setTimeout(resolve, 90000)); // sleep 1 and half min until data will appears in newrelic.
        await collectNewrelicLogs(project, startTime, getNewrelicTime(), test.route, test.requestType, newrelicLogFilePath);
    }

    return runObject;
}

async function collectNewrelicLogs(instance, timeFrameStart, timeFrameEnd, route, requestType, newrelicPath) {
    fastify.log.info(`Collect newrelic info:  ${timeFrameStart} - ${timeFrameEnd} : ${route}  Request type: ${requestType} Newrelic log path: ${newrelicPath}`)
    fastify.log.info(instance);

    if (!route.length) {
        fastify.log.info(`Skip request to newrelic due to empty route.`)
        return;
    }

    const data = JSON.stringify({
        "query": newrelicQuery.replace('ACCOUNT_ID', instance.newrelic_application_id)
            .replace('TARGET_ROUTE', route)
            .replace('REQUEST_TYPE', requestType.toUpperCase())
            .replace('APPLICATION_NAME', instance.newrelic_application)
            .replace('SINCE_DATE_TIME', timeFrameStart)
            .replace('UNTIL_DATE_TIME', timeFrameEnd),
        "variables": ""
    });

    fastify.log.info(`Payload:` + data);

    const options = {
        hostname: 'api.newrelic.com',
        port: 443,
        path: '/graphql',
        method: 'POST',
        json: true,
        headers: {
            'Content-Type': 'application/json',
            'Content-Length': data.length,
            'API-Key': instance.newrelic_api_key
        }
    }
    const req = https.request(options, res => {
        let data = '';
        res.on('data', (chunk) => {
            data += chunk;
        })

        res.on('end', async () => {
            await fs.writeJson(newrelicPath, JSON.parse(data));
            fastify.log.info(JSON.parse(data));
        });
    }).on("error", async (error) => {
        await fs.writeJson(newrelicPath, JSON.parse(data));
        fastify.log.info("Newrelic request error:" + error.message)
    })
    req.write(data);
    req.end();
}

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
    root: path.join(__dirname, '/../' + destinationFolder),
    prefix: reportSuffix,
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
    reply.view('list.mustache', {
        title: 'Load testing',
        scenarios: scenarios,
        projects: Array.from(instanceList.values()),
        jobs: Array.from(jobs.values()),
        reports: Array.from(reports.values()),
        pendingJobs: pendingJobs.size,
    }, {...partials});
});

fastify.get('/reports', (req, reply) => {
    let list = Array.from(reports.values()).map(report => ({...report, log: null}));

    reply.send(list);
});

fastify.post('/reports', async(req, reply) => {
    await readReports();

    let data = JSON.parse(req.body);
    let reportPath = 'web/' + data.id + '/report.json';

    reports.set(String(data.id), data);
    await fs.writeJson(reportPath, {...data, log: null});

    reply.code(200);
    reply.send('');
});

fastify.delete('/reports/*', (req, reply) => {
    let reportId = String(req.params['*']);

    let archivePath = destinationFolder + '/archive/' + reportId;
    let reportPath = destinationFolder + '/' + reportId;

    if (!reports.has(reportId)) {
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

    reports.delete(reportId);

    reply.code(200);
    reply.send('');
});


fastify.get('/run', (req, reply) => {

    let template = jobs.size === 0 ? 'run.mustache' : 'error.mustache';
    reply.view(template, {
        title: 'Run a test',
        errorMessage: 'Some tests are already running.',
        scenarios: scenarios,
        projects: Array.from(instanceList.values()),
        jobs: Array.from(jobs.values()),
        reports: Array.from(reports.values()),
        pendingJobs: pendingJobs.size,
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

    if (!instanceList.has(instance) || jobs.size > 0) {
        reply.code(404);
        reply.send(`Project ${instance} is not found.`);
        return;
    }
    pendingJobs = initTestCases();
    let testCase = getTestCaseById(testName);
    if (testCase == null) {
        reply.code(404);
        reply.send(`Given test ${testName} was not found.`);
        return;
    }
    pendingJobs.clear();
    pendingJobs.set(testCase.id, testCase);
    executeTestCase(testCase, instance, instanceName, testType, targetRps, duration, description);

    reply.redirect(302, `/`);
});

fastify.get('/clear', async (req, reply) => {
    pendingJobs.clear();
    reply.redirect(302, `/`);
});

fastify.get('/run_all', (req, reply) => {
    let template = jobs.size === 0 ? 'run_all.mustache' : 'error.mustache';
    reply.view(template, {
        title: 'Run All Test',
        errorMessage: 'Some tests are already running.',
        scenarios: scenarios,
        projects: Array.from(instanceList.values()),
        jobs: Array.from(jobs.values()),
        reports: Array.from(reports.values()),
        pendingJobs: pendingJobs.size,
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

    if (!instanceList.has(instance) || jobs.size > 0) {
        reply.code(404);
        reply.send(`Project ${instance} is not found.`);
        return;
    }
    pendingJobs = initTestCases();

    async function runAll(instance, instanceName, testType, targetRps, duration, description) {
        for (let i = 0; i < scenarios.length; i++) {
            if (scenarios[i].status === 'inactive') {
                continue;
            }
            for (let j = 0; j < scenarios[i].tests.length; j++) {
                let testCase = scenarios[i].tests[j]
                if (testCase != null) {
                    fastify.log.info(`Test case:  ${testCase.id} : ${testCase.route}`)
                    let runObject = await executeTestCase(testCase, instance, instanceName, testType, targetRps, duration, description);
                    fastify.log.info(`Test case status:  ${runObject.done}`)
                }
            }
        }
    }

    runAll(instance, instanceName, testType, targetRps, duration, description);

    reply.redirect(302, `/`);
});

fastify.get('/instances', (req, reply) => {
    let instanceKey = req.query['key'];
    let selectedInstance = {};

    if (instanceList.has(instanceKey)) {
        selectedInstance = instanceList.get(instanceKey);
    }

    reply.view('instance.mustache', {
        title: 'Instances',
        scenarios: scenarios,
        projects: Array.from(instanceList.values()),
        selectedInstance: selectedInstance,
        formTitle: Object.entries(selectedInstance).length === 0 ? 'Add instance' : 'Edit instance',
    }, {...partials});
});

fastify.get('/instances/list', (req, reply) => {
    reply.send(Array.from(instanceList.values()));
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

    if (key !== "") {
        let project = Object.assign({},
            yves && {"yves": yves},
            glue && {"glue": glue},
            key && {"key": key},
            backendApi && {"backend_api": backendApi},
            feApi && {"fe_api": feApi},
            newrelicApplication && {"newrelic_application": newrelicApplication},
            newrelicApiKey && {"newrelic_api_key": newrelicApiKey},
            newrelicApplicationId && {"newrelic_application_id": newrelicApplicationId},
        );

        instanceStore.set(key, project);

        instanceList = getInstanceList();
    }

    reply.redirect(302, '/instances');
});

fastify.delete('/instances/:instanceKey', (req, reply) => {
    let key = String(req.params['instanceKey']);
    let statusCode = 404;

    if (instanceStore.has(key)) {
        instanceStore.del(key);
        instanceList = getInstanceList();
        statusCode = 200;
    }

    reply.code(statusCode);
    reply.send('');
});

fastify.get('/log/*', async (req, reply) => {
    let runId = String(req.params['*']);
    console.log(runId);
    if (!reports.has(runId)) {
        reply.code(404);
        reply.send('');
        return;
    }
    let runObject = reports.get(runId);
    let template = runObject.done ? 'history-log.mustache' : 'log.mustache';
    let log = runObject.log || await fs.readJson(`./${destinationFolder}/${runObject.id}/report.log.json`);
    reply.view(template, {
        runObject,
        log,
        title: 'Console log',
        errorMessage: 'Some tests are already running.',
        scenarios: scenarios,
        projects: Array.from(instanceList.values()),
        jobs: Array.from(jobs.values()),
        reports: Array.from(reports.values()),
        pendingJobs: pendingJobs.size,
    }, {...partials});
});

fastify.get('/newrelic/*', async (req, reply) => {
    let runId = String(req.params['*']);
    console.log(runId);
    if (!reports.has(runId)) {
        reply.code(404);
        reply.send('');
        return;
    }
    let runObject = reports.get(runId);
    if (!runObject.done) {
        reply.redirect(404, "/");
    }
    let template = 'newrelic-log.mustache';
    let log = await fs.readJson(`./${destinationFolder}/${runObject.id}/newrelic.json`);
    reply.view(template, {
        runObject,
        results: JSON.stringify(log),
        title: 'Console log',
        errorMessage: 'Some tests are already running.',
        scenarios: scenarios,
        projects: Array.from(instanceList.values()),
        jobs: Array.from(jobs.values()),
        reports: Array.from(reports.values()),
        pendingJobs: pendingJobs.size,
    }, {...partials});
});

const readReports = async () => {
    await glob(`./${destinationFolder}/!(archive)/**/report.json`, (er, files) => {
        Array.isArray(files) && files.map(file => {
            let runObject = fs.readJsonSync(file);
            // if (fs.exists(runObject.newrelicLogFilePath)) {
            // fastify.log.info(`${runObject.newrelicLogFilePath}`)
            // runObject.newrelicLog = fs.readJsonSync(runObject.newrelicLogFilePath)
            // }
            reports.set(runObject.id, runObject);
        });
    });
};

// Run the server!
const start = async () => {
    try {
        await readReports();
        await fastify.listen(port, host, error => {
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
