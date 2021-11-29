const fs = require('fs');

const scenariosListFilePath = process.cwd() + '/resources/scenarios/scenarios.js';
const prependingString = 'const scenarios = ';
const appendingString = '\n\nmodule.exports = scenarios;'

exports.add = (className, type='Frontend API') => {
    const fileContent = fs.readFileSync(scenariosListFilePath, 'utf8');
    const jsonDataObject = prepareStringToJsonObject(fileContent);
    addToList(jsonDataObject, className, type);
}

prepareStringToJsonObject = (data) => {
    return JSON.parse(data.replace(appendingString, '')
        .replace(prependingString, ''));
}

getSectionByType = (data, type) => {
    for (let section of data) {
        if (section.title === type) {
            return section;
        }
    }

    return {};
}

findAndUpdateSection = (data, type, modifiedSection) => {
    for (let section of data) {
        if (section.title === type) {
            data[section] = modifiedSection;
        }
    }

    return data;
}

getTestByClassName = (data, className) => {
    for (let item of data) {
        if (item.id === className) {
            return item;
        }
    }

    return null;
}

writeToFile = (data, filePath) => {
    fs.writeFileSync(filePath, data);
}

buildFileContent = (data, type, modifiedSection) => {
    let fileContent = "";
    fileContent += prependingString;
    fileContent += JSON.stringify(findAndUpdateSection(data, type, modifiedSection),null, 2);
    fileContent += appendingString;
    return fileContent;
}

addToList = (data, className, type) => {
    const sectionToEdit = getSectionByType(data, type);
    const testIsFound = getTestByClassName(sectionToEdit.tests, className);
    if (!testIsFound) {
        sectionToEdit.tests.unshift({
            id: className,
            title: className
        })
        const modifiedData = buildFileContent(data, type, sectionToEdit);
        writeToFile(modifiedData, scenariosListFilePath);
    }
}
