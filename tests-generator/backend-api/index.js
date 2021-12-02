const fs = require('fs');

const gatlingTests = require('../gatling-tests');

const swaggerSchemaDirPath = process.cwd() + '/tests-generator/swagger-config/backend-api/';
const files = fs.readdirSync(swaggerSchemaDirPath);
const type = 'BackendApi';

for (let file of files) {
    gatlingTests.generate(swaggerSchemaDirPath + file, type);
}
