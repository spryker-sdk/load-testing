const fs = require('fs-extra');
const path = require('path');
const glob = require("glob");
const {spawn} = require('child_process');
const dateFormat = require('date-format');
const rimraf = require("rimraf");
const fastify = require('fastify')({logger: true});
const port = process.env.PORT || 3000;

const scenarios = require('../resources/scenarios/scenarios.js');

const destinationFolder = 'web';
const reportSuffix = '/report';
const instanceStore = require('data-store')({path: process.cwd() + '/web/instance.json'});

const getInstanceList = () => {
    let projects = new Map(Object.entries(instanceStore.get()));
    projects.forEach((value, key) => value.key = key);

    return projects;
}

const jobs = new Map();
const reports = new Map();
let instanceList = getInstanceList();

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
    }, {...partials});
});

fastify.post('/run', async (req, reply) => {
    let instance = req.body.instance;
    let testName = req.body.testName;
    let testType = req.body.testType;
    let targetRps = req.body.targetRps;
    let duration = req.body.duration;
    let description = req.body.description;
    let title = `${testName}${testType}`;

    if (!instanceList.has(instance) || jobs.size > 0) {
        reply.code(404);
        reply.send(`Project ${instance} is not found.`);
        return;
    }

    let project = instanceList.get(instance);
    let key = `${instance}/${testName}/${testType}/${targetRps}-RPS/` + dateFormat.asString('yyyy-MM-dd-hh-mm-ss');
    let reportPath = `/report/${key}/report/index.html`;
    let reportFolder = `./${destinationFolder}/${key}`;
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
        logPath: `/log/${key}`,
        valid: null,
    };

    jobs.set(String(key), runObject);
    reports.set(String(key), runObject);

    var env = Object.create(process.env);
    env.JAVA_OPTS = (process.env.JAVA_OPTS || '')
        + ` -DYVES_URL=${project.yves}`
        + ` -DGLUE_URL=${project.glue}`
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

    reply.redirect(302, `${runObject.logPath}#bottom`);
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

    if (key !== "") {
        let project = Object.assign({},
            yves && {"yves": yves},
            glue && {"glue": glue},
            key && {"key": key},
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
    }, {...partials});
});

const readReports = async () => {
    await glob(`./${destinationFolder}/!(archive)/**/report.json`, (er, files) => {
        Array.isArray(files) && files.map(file => {
            let runObject = fs.readJsonSync(file);
            reports.set(runObject.id, runObject);
        });
    });
};

// Run the server!
const start = async () => {

    await readReports();

    try {
        await fastify.listen(port, '0.0.0.0');
        fastify.log.info(`server listening on ${fastify.server.address().port}`)
    } catch (err) {
        fastify.log.error(err);
    }
};

start();
