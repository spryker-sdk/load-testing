const dateFormat = require("date-format");
const fs = require("fs-extra");
const glob = require("glob");
const newrelic= require("./newrelic");
const {spawn} = require('child_process');
const scenarios = require("../scenarios/scenarios");
let variables = require('./variables');
const timeouts = require('./timeout');
const report = require('./reports');

const getTestCaseById = (testCaseId) => {
    if (variables.pendingJobs.has(testCaseId)) {
        return variables.pendingJobs.get(testCaseId);
    }
    return null
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

const updateReport = async (runObject, code, test, reportFolder, project, startTime, newrelicLogFilePath, key) => {
    runObject.done = true;
    runObject.end = new Date().getTime();
    runObject.exitCode = code;
    runObject.success = code === 0;
    variables.jobs.delete(key);
    variables.pendingJobs.delete(test.id);
    // if (!runObject.success) {
    //     await fs.remove(reportFolder);
    //     return;
    // }

    await glob(`${reportFolder}/*/`, (er, directories) => {
        directories.map(async directory => fs.rename(directory, `${reportFolder}${variables.reportSuffix}`));
    });

    await fs.writeJson(`${reportFolder}/report.json`, {...runObject, log: null});
    await fs.writeJson(`${reportFolder}/report.log.json`, runObject.log);
    await newrelic.collectNewrelicLogs(project, startTime, newrelic.getNewrelicTime(), test.route, test.requestType, newrelicLogFilePath);
    await report.readReports();
}
module.exports.getTestCaseById = getTestCaseById

module.exports.initTestCases = initTestCases

module.exports.executeTestCase = async function executeTestCase(test, instance, instanceName, testType, targetRps, duration, description) {
    let startTime = newrelic.getNewrelicTime();
    let testName = test.id;
    let title = `${testName}${testType}`;
    let project = variables.instanceList.get(instance);
    let key = `${instance}/${testName}/${testType}/${targetRps}-RPS/` + dateFormat.asString('yyyy-MM-dd-hh-mm-ss');
    let reportPath = `/report/${key}/report/index.html`;
    let reportFolder = `./${variables.destinationFolder}/${key}`;
    let newrelicLogFilePath = `${reportFolder}/newrelic.json`;
    await fs.mkdirs(reportFolder);

    let runObject = {
        id: key,
        done: false,
        exitCode: -1,
        when: new Date().getTime(),
        end: '',
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

    variables.jobs.set(key, runObject);
    variables.reports.set(key, runObject);

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

    run.stderr.on('error', async code => {
        await updateReport(runObject, code, test, reportFolder, project, startTime, newrelicLogFilePath, key);
    });

    run.on('exit', async code => {
        await updateReport(runObject, code, test, reportFolder, project, startTime, newrelicLogFilePath, key);
    });

    run.on('close', async code => {
        await updateReport(runObject, code, test, reportFolder, project, startTime, newrelicLogFilePath, key);
    });

    while (!runObject.done) {
        // fastify.log.info(`Job ID:  ${runObject.id} Job Status: ${runObject.done} Job error: ${runObject.error}`);
        await new Promise(resolve => setTimeout(resolve, timeouts.checkJobStatusAfter));
    }
    return runObject;
}
