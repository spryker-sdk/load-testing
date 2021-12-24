const fs = require('fs');

const classContent = require('../class-content');
const scenariosList = require('../scenarios-list');

exports.generateGatlingTestFile = (endpoint, data, schemas, type) => {
    for (let method in data) {
        if (data[method].hasOwnProperty('skip') && data[method].skip === true) {
            continue;
        }

        const classContentFile = classContent.build(data[method], method, endpoint, schemas, type);
        const className = classContent.generateClassName(data[method].operationId, method, endpoint, type);
        const classFullPath = process.cwd() + `/resources/scenarios/spryker/${className}.scala`;

        generateScalaFile(classFullPath, classContentFile)
        scenariosList.add(className);
    }
}

function generateScalaFile(classFullPath, classContent) {
    fs.writeFileSync(classFullPath, classContent);
}
