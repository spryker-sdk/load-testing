const queryString = require('querystring');

exports.process = (data, endpoint) => {
    const parameters = {
        query: {},
        headers: "",
        path: endpoint
    }

    if (data.hasOwnProperty('parameters') && Array.isArray(data.parameters)) {
        for (let inputParameter of data.parameters) {
            if (!inputParameter.hasOwnProperty('name')
                || !inputParameter.hasOwnProperty('in')
                || !inputParameter.hasOwnProperty('required')
                || (inputParameter.required && !inputParameter.schema.hasOwnProperty('example'))
            ) {
                continue;
            }

            if (inputParameter.in === 'query') {
                generateQueryString(inputParameter, parameters);
            }

            if (inputParameter.in === 'header') {
                generateHeadersList(inputParameter, parameters);
            }

            if (inputParameter.in === 'path') {
                generatePath(inputParameter, parameters);
            }
        }
    }

    setPlaceholdersForPathParameters(parameters);
    parameters.query = queryString.stringify(parameters.query);
    if (parameters.query.length > 0) {
        parameters.path = parameters.path + `?${parameters.query}`
    }

    return parameters;
}

generateQueryString = (inputParameter, parameters) => {
    if (inputParameter.schema.example !== undefined) {
        parameters.query[inputParameter.name] = inputParameter.schema.example;
    }
}

generateHeadersList = (inputParameter, parameters) => {
    if (inputParameter.schema.example !== undefined) {
        parameters.headers += `    .header("${inputParameter.name}", "${inputParameter.schema.example}")\n`;
    }
}

generatePath = (inputParameter, parameters) => {
    if (inputParameter.schema.example !== undefined) {
        const pathRegex = new RegExp(`\{${inputParameter.name}\}`);
        parameters.path = parameters.path.replace(pathRegex, inputParameter.schema.example)
    }
}

setPlaceholdersForPathParameters = (parameters) => {
    parameters.path = parameters.path.replace(/\{/g, "${");
}
