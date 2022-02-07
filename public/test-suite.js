#!/usr/bin/env node

const scenarios = require('../resources/scenarios/scenarios.js');
let variables = require('../resources/js/variables');
const {initTestCases, getTestCaseById, executeTestCase} = require('../resources/js/testCase');
const timeouts = require('../resources/js/timeout');
const newrelic = require("../resources/js/newrelic");
const yargs = require('yargs');
const {instanceList} = require("../resources/js/variables");

const options = yargs
    .usage("Usage: -n <name>")
    .option("i", { alias: "instance", describe: "Target instance for load testing", type: "string", demandOption: true })
    .choices('i', Array.from(instanceList.keys()))
    .option("t", { alias: "type", describe: "type of load testing", type: "string", demandOption: true  })
    .choices('t', ["Ramp", "Steady"])
    .option("r", { alias: "rps", describe: "Target RPS", type: "int", demandOption: true  })
    .choices('r', [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25, 30, 35, 40])
    .option("d", { alias: "duration", describe: "Target test duration", type: "int", demandOption: true  })
    .choices('d', [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25, 30, 35, 40])
    .argv;


// execute complete test suite
const start = async (instance, testType, targetRps, duration) => {
    try {
        let description = `Test suite execution ${newrelic.getNewrelicTime()}`;
        let instanceNameRegex = /\(([a-zA-Z_-]+)\)/i;
        let found = instance.match(instanceNameRegex);
        let instanceName = Array.isArray(found) && found.length > 1 ? found[1] : "";

        if (!variables.instanceList.has(instance) || variables.jobs.size > 0) {
            console.error(`Project ${instance} is not found.`);
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
                        console.log(`Execute test-case: ${testCase.id}: Start time: ${newrelic.getNewrelicTime()}`);
                        await executeTestCase(testCase, instance, instanceName, testType, targetRps, duration, description);
                        console.log(`Test-case execution done at: ${newrelic.getNewrelicTime()}`);
                        console.log(`Pause before next test-case: ${timeouts.executeNextTestAfter}`)
                        await new Promise(resolve => setTimeout(resolve, timeouts.executeNextTestAfter)).catch(() => {});
                    }
                }
            }
            variables.pendingJobs.clear();
        }

        await runAll(instance, instanceName, testType, targetRps, duration, description);
    } catch (err) {
        console.error(err);
    }
};

let instance = options.instance;
let testType = options.type;
let targetRps = options.rps;
let duration = options.duration;

start(instance, testType, targetRps, duration);
