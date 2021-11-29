const fs = require('fs');

const classContent = require('../class-content');
const scenariosList = require('../scenarios-list');

exports.generateGatlingTestFile = (endpoint, data) => {
    for (let method in data) {
        if (method === 'get') {

            const classContentFile = classContent.build(data[method], method, endpoint);//generateClassContent(endpoint, className, method, data[method].summary);
            const className = classContent.generateClassName(data[method].operationId);
            const classFullPath = process.cwd() + `/resources/scenarios/spryker/${className}.scala`;

            generateScalaFile(classFullPath, classContentFile)
            scenariosList.add(className);
        }
    }
}

function generateScalaFile(classFullPath, classContent) {
    fs.writeFile(classFullPath, classContent, function(err) {
        if(err) {
            return console.log(err);
        }
    });
}
