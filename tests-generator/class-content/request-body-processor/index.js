exports.process = (data, schemas) => {
    const requestBody = {};
    if (data.hasOwnProperty('requestBody')
        && data.requestBody.hasOwnProperty('content')
        && data.requestBody.content.hasOwnProperty('application/json')
        && data.requestBody.content['application/json'].hasOwnProperty('schema')
        && data.requestBody.content['application/json'].schema.hasOwnProperty('$ref')
    ) {
        const referencePathParts = data.requestBody.content['application/json'].schema.$ref.split('/');
        const schemaName = referencePathParts[3];

        if (schemas.hasOwnProperty(schemaName)) {
            assembleProperties(requestBody, schemas[schemaName], schemas)
        }
    }

    return requestBody;
}

assembleProperties = async (requestBody, schema, schemas) => {
    const properties = schema.properties;

    if (Array.isArray(requestBody)) {
        requestBody = requestBody[0];
    }
    for (let property in properties) {
        const propertyItem = properties[property];

        if (propertyItem.hasOwnProperty('type') && propertyItem.type === 'array') {
            if (propertyItem.hasOwnProperty(('example'))) {
                if (Array.isArray(propertyItem.example)) {
                    requestBody[property] = [propertyItem.example];
                    continue;
                }
                requestBody[property] = propertyItem.example;
                continue;
            }

            if (propertyItem.items.hasOwnProperty('$ref')
                || (propertyItem.hasOwnProperty('items')
                    && propertyItem.items.hasOwnProperty('oneOf')
                    && Array.isArray(propertyItem.items.oneOf)
                    && propertyItem.items.oneOf[0].hasOwnProperty('$ref')
                )
            ) {
                const referencePathParts = propertyItem.items.$ref.split('/');
                requestBody[property] = [{}];
                await assembleProperties(requestBody[property], schemas[referencePathParts[3]], schemas);
                continue;
            }
        }

        if (propertyItem.hasOwnProperty('$ref')) {
            const referencePathParts = propertyItem.$ref.split('/');
            requestBody[property] = {};
            await assembleProperties(requestBody[property], schemas[referencePathParts[3]], schemas);
            continue;
        }

        if (propertyItem.hasOwnProperty(('example'))) {
            requestBody[property] = propertyItem.example;
            continue;
        }

        if (propertyItem.hasOwnProperty(('enum'))) {
            requestBody[property] = propertyItem.enum[0];
            continue;
        }

        requestBody[property] = "${" + property + "}";
    }
}
