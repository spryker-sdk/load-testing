const fs = require('fs');

const classContent = require('../class-content');
const scenariosList = require('../scenarios-list');

exports.generateGatlingTestFile = (endpoint, data, schemas) => {
    for (let method in data) {
        const classContentFile = classContent.build(data[method], method, endpoint, schemas);//generateClassContent(endpoint, className, method, data[method].summary);
        const className = classContent.generateClassName(data[method].operationId);
        const classFullPath = process.cwd() + `/resources/scenarios/spryker/${className}.scala`;

        generateScalaFile(classFullPath, classContentFile)
        scenariosList.add(className);
    }
}

function generateScalaFile(classFullPath, classContent) {
    fs.writeFileSync(classFullPath, classContent);
}
