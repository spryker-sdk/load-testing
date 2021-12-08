const fs = require('fs');

const gatlingTests = require('../gatling-tests');

const swaggerSchemaDirPath = process.cwd() + '/tests-generator/swagger-config/data-import-api/';
const files = fs.readdirSync(swaggerSchemaDirPath);
const type = 'DataImportApi';

for (let file of files) {
    gatlingTests.generate(swaggerSchemaDirPath + file, type);
}