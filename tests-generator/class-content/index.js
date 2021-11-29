const queryString = require('querystring');

const successCodeDefaultDescription = "Expected response to a valid request.";

importSection = () => {
    return  "package spryker\n" +
        "\n" +
        "import io.gatling.core.Predef._\n" +
        "import io.gatling.http.Predef._\n" +
        "import scala.concurrent.duration._\n" +
        "import spryker.GlueProtocol._\n" +
        "import spryker.Scenario._\n";
}

generateImportSection = (preparationClassesList) => {
    let extraImports = "";
    const defaultImportSection = importSection();
    for (let preparationClass of preparationClassesList) {
        extraImports += `import spryker.${preparationClass}._\n`;
    }

    return defaultImportSection + extraImports + "\n";
}

classOpeningSection = (className, scenarioName) => {
    return "trait " + className + "Base {\n" +
        "\n" +
        "  lazy val scenarioName = \"" + scenarioName + "\"\n" +
        "\n" +
        "  val httpProtocol = GlueProtocol.httpProtocol\n" +
        "\n";
}

generateClassOpeningSection = (className, scenarioName) => {
    return classOpeningSection(className, scenarioName);
}

classRequestSection = (method, endpoint, statusCode = 200) => {
    return {
        startWith: "  val request = http(scenarioName)\n" +
        "    ." + method + "(\"" + endpoint + "\")\n",
        endWith: "    .check(status.is(" + statusCode + "))\n" + "\n"
    }
}

generateAccessTokenHeader = (securityData) => {
    let accessTokenHeader = "";
    for (let item of securityData) {
        if (item.hasOwnProperty('BearerAuth')) {
            accessTokenHeader = "    .header(\"Authorization\", \"Bearer ${access_token}\")\n";
            break;
        }
    }

    return accessTokenHeader;
}

getSuccessfulResponseCode = (responses) => {
    let responseCode = 200;
    for (let code in responses) {
        if (responses[code].description === successCodeDefaultDescription) {
            responseCode = code;
            break;
        }
    }

    return responseCode;
}

generateClassRequestSection = (method, parameters, data) => {
    const successfulResponseCode = getSuccessfulResponseCode(data.responses)
    const defaultClassRequestSection = classRequestSection(method, parameters.path, successfulResponseCode);
    let extraRequestData = "";
    if (data.hasOwnProperty('security')) {
        extraRequestData += generateAccessTokenHeader(data.security)
    }

    if (parameters.headers.length > 0) {
        extraRequestData += parameters.headers;
    }

    return defaultClassRequestSection.startWith + extraRequestData + defaultClassRequestSection.endWith;
}

const classScenarioSection = () => {
    return {
        startWith: "  val scn = scenario(scenarioName)\n",
        endWith: "    .exec(request)\n" + "\n"
    }
}

generateClassScenarioSection = (preparationClassesList) => {
    let extraScenarios = "";
    const defaultClassScenarioSection = classScenarioSection();
    for (let preparationClass of preparationClassesList) {
        extraScenarios += `    .exec(${preparationClass}.executeRequest)\n`;
    }

    return defaultClassScenarioSection.startWith + extraScenarios + defaultClassScenarioSection.endWith;
}


classClosingSection = () => {
    return "  }\n" +
        "\n";
}

generateClassClosingSection = () => {
    return classClosingSection();
}

rampSteadyClassesSection = (className, scenarioName) => {
    return "class " + className + "Ramp extends Simulation with " + className + "Base {\n" +
        "\n" +
        "  override lazy val scenarioName = \"" + scenarioName + " [Incremental]\"\n" +
        "\n" +
        "  setUp(scn.inject(\n" +
        "      rampUsersPerSec(0) to (Scenario.targetRps.toDouble) during (Scenario.duration),\n" +
        "    ))\n" +
        "    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration))\n" +
        "    .protocols(httpProtocol)\n" +
        "}\n" +
        "\n" +
        "class " + className + "Steady extends Simulation with " + className + "Base {\n" +
        "\n" +
        "  override lazy val scenarioName = \"" + scenarioName + " [Steady RPS]\"\n" +
        "\n" +
        "  setUp(scn.inject(\n" +
        "      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),\n" +
        "    ))\n" +
        "    .throttle(\n" +
        "      jumpToRps(Scenario.targetRps),\n" +
        "      holdFor(Scenario.duration),\n" +
        "    )\n" +
        "    .protocols(httpProtocol)\n" +
        "}"
}

generateRampSteadyClassesSection = (className, scenarioName) => {
    return rampSteadyClassesSection(className, scenarioName);
}

exports.build = (data, method, endpoint) => {
    const parameters = generateParameters(data.parameters, endpoint)
    const className = this.generateClassName(data.operationId);
    const scenarioName = data.summary;
    const preparationClassesList = data.hasOwnProperty('prepareDataSteps')
        ? getPreparationClassesList(data.prepareDataSteps)
        : [];

    return generateImportSection(preparationClassesList) +
        generateClassOpeningSection(className, scenarioName) +
        generateClassRequestSection(method, parameters, data) +
        generateClassScenarioSection(preparationClassesList) +
        generateClassClosingSection() +
        generateRampSteadyClassesSection(className, scenarioName);
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
    if (inputParameter.schema.example === undefined) {
        parameters.path = parameters.path.replace(/\{/g, '${')
        return;
    }

    const pathRegex = new RegExp(`\{${inputParameter.name}\}`);
    parameters.path = parameters.path.replace(pathRegex, inputParameter.schema.example)
}

generateParameters = (inputParameters, endpoint) => {
    const parameters = {
        query: {},
        headers: "",
        path: endpoint
    }

    for (let inputParameter of inputParameters) {
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

    parameters.query = queryString.stringify(parameters.query);
    if (parameters.query.length > 0) {
        parameters.path = parameters.path + `?${parameters.query}`
    }

    return parameters;
}

exports.generateClassName = (operationId) => {
    let endpointName = operationId.replace(/\-([a-z])/g, function (match, letter) {
        return letter.toUpperCase();
    })

    return endpointName.charAt(0).toUpperCase() + endpointName.slice(1) + 'Api';
}

getPreparationClassesList = (prepareDataSteps) => {
    const classList = [];
    for (let prepareDataStep of prepareDataSteps) {
        classList.push(prepareDataStep + 'RequestApi')
    }

    return classList;
}
