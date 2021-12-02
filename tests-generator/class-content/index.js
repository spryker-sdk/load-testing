const requestBodyProcessor = require('./request-body-processor');
const parametersProcessor = require('./parameters-processor');
const scalaScenarioClassProcessor = require('./scala-scenario-class-processor');

const mapMethodToAction = {
    'post': 'Create',
    'patch:': 'Update',
    'put:': 'Update',
    'delete': 'Delete',
    'get': 'Get'
}

exports.build = (data, method, endpoint, schemas, type) => {
    const parameters = parametersProcessor.process(data, endpoint);
    const requestBody = requestBodyProcessor.process(data, schemas);
    const className = this.generateClassName(data.operationId, method, endpoint, type);

    return scalaScenarioClassProcessor.process(className, method, data, parameters, requestBody);
}

exports.generateClassName = (operationId, method, endpoint, type = 'FrontendApi') => {
    const classIdentifier = operationId ? operationId : mapMethodToAction[method] + endpoint
    let endpointName = classIdentifier.replaceAll(/[/._-]([a-z])/g, function (match, letter) {
        return letter.toUpperCase();
    })

    return classIdentifier.charAt(0).toUpperCase() + endpointName.slice(1) + type;
}
