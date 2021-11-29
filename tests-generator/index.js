const yaml = require('yaml');
const fs = require('fs');

const scenario = require('./scenario');
const swaggerSchemaFilePath = process.cwd() + '/tests-generator/swagger-config/_schema.yml';

const importFile = fs.readFileSync(swaggerSchemaFilePath, 'utf8');
const fileContent = yaml.parse(importFile);

for (let endpoint in fileContent.paths) {
    scenario.generateGatlingTestFile(endpoint, fileContent.paths[endpoint]);
}


