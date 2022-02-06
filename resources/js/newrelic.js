const MetricsValidator = require("./metricsValidator");
const fs = require("fs-extra");
const https = require('https');
const timeouts = require('./timeout');

const newrelicQuery='{\n' +
    '  actor {\n' +
    '    account(id: ACCOUNT_ID) {\n' +
    '      nrql(query: "SELECT count(*) as requests, max(duration)*1000 as max, min(duration)*1000 as min, average(duration)*1000 as avg FROM Transaction WITH TIMEZONE \'UTC\' WHERE spryker_router_resources_path=\u0027TARGET_ROUTE\u0027 AND appName = \u0027APPLICATION_NAME\u0027 AND request.method in (\u0027REQUEST_TYPE\u0027) SINCE \u0027SINCE_DATE_TIME\u0027 UNTIL \u0027UNTIL_DATE_TIME\u0027") ' +
    '      {\n' +
    '        results\n' +
    '      }\n' +
    '    }\n' +
    '  }\n' +
    '}';


module.exports.getNewrelicTime = () => {
    return new Date().toISOString().replace("T", " ").replace(/\..+/, ``);
}

module.exports.collectNewrelicLogs =
    async function collectNewrelicLogs(instance, timeFrameStart, timeFrameEnd, route, requestType, newrelicPath) {
        if (!route.length && (!instance.newrelic_application_id.length || !instance.newrelic_application.length || !instance.newrelic_api_key)) {
            console.info(`Skip request to newrelic due to misconfiguration.`)
            return;
        }
        async function collect() {
            console.info(`Collect newrelic info:  ${timeFrameStart} - ${timeFrameEnd} : ${route}  Request type: ${requestType} Newrelic log path: ${newrelicPath}`)
            const data = JSON.stringify({
                "query": newrelicQuery.replace('ACCOUNT_ID', instance.newrelic_application_id)
                    .replace('TARGET_ROUTE', route)
                    .replace('REQUEST_TYPE', requestType.toUpperCase())
                    .replace('APPLICATION_NAME', instance.newrelic_application)
                    .replace('SINCE_DATE_TIME', timeFrameStart)
                    .replace('UNTIL_DATE_TIME', timeFrameEnd),
                "variables": ""
            });
            const validator = new MetricsValidator();

            console.info(`Payload:` + data);

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
                    let result = JSON.parse(data);
                    result = result.data.actor.account.nrql.results[0];
                    result.requestType = requestType;

                    await fs.writeJson(newrelicPath, validator.validate(result));
                    console.info(JSON.parse(data));
                });
            }).on("error", async (error) => {
                await fs.writeJson(newrelicPath, JSON.parse(data));
                console.error("Newrelic request error:" + error.message)
            })
            req.write(data);
            req.end();
        }

        console.info(`Sleep before request to newrelic`);
        return new Promise(() => setTimeout(collect, timeouts.collectNewrelicInfoAfter));
    }
