const yaml = require('yaml');
const fs = require('fs');

const scenario = require('../scenario');

exports.generate = (filePath) => {
    const importFile = fs.readFileSync(filePath, 'utf8');
    const fileContent = yaml.parse(importFile);

    for (let endpoint in fileContent.paths) {
        const schemas = fileContent.hasOwnProperty('components') && fileContent.components.hasOwnProperty('schemas')
            ? fileContent.components.schemas
            : {}
        scenario.generateGatlingTestFile(endpoint, fileContent.paths[endpoint], schemas);
    }
}



