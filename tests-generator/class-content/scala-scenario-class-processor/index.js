const preparationClasses = require('../preparation-classes');

const successCodeDefaultDescription = "Expected response to a valid request.";

exports.process = (className, method, data, parameters, requestBody) => {
    const scenarioName = data.summary.replaceAll(/\n/g, '');
    const preparationClassesList = data.hasOwnProperty('prepareDataSteps')
        ? preparationClasses.get(data.prepareDataSteps)
        : [];

    return generateImportSection(preparationClassesList) +
        generateClassOpeningSection(className, scenarioName) +
        generateClassRequestSection(method, parameters, data, requestBody) +
        generateClassScenarioSection(preparationClassesList) +
        generateClassClosingSection() +
        generateRampSteadyClassesSection(className, scenarioName);
}

importSection = () => {
    return "package spryker\n" +
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

generateClassRequestSection = (method, parameters, data, requestBody) => {
    const successfulResponseCode = getSuccessfulResponseCode(data.responses)
    const defaultClassRequestSection = classRequestSection(method, parameters.path, successfulResponseCode);
    let extraRequestData = "";
    if (data.hasOwnProperty('security')) {
        extraRequestData += generateAccessTokenHeader(data.security)
    }

    if (parameters.headers.length > 0) {
        extraRequestData += parameters.headers;
    }

    if (Object.keys(requestBody).length !== 0) {
        const stringifiedRequestBody = JSON.stringify(requestBody, null, 2);
        extraRequestData += `    .body(StringBody("""${stringifiedRequestBody}"""))\n`;
    }

    return defaultClassRequestSection.startWith + extraRequestData + defaultClassRequestSection.endWith;
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



const classScenarioSection = () => {
    return {
        startWith: "  val scn = scenario(scenarioName)\n",
        endWith: "    .exec(request)\n"
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
        "      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),\n" +
        "    ))\n" +
        "    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))\n" +
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