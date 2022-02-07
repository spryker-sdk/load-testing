const glob = require("glob");
const variables = require("./variables");
const fs = require("fs-extra");

module.exports.readReports = async () => {
    await glob(`./${variables.destinationFolder}/!(archive)/**/report.json`, (er, files) => {
        Array.isArray(files) && files.map(file => {
            let runObject = fs.readJsonSync(file);
            try {
                runObject.newrelicLog = fs.readJsonSync(runObject.newrelicLogFilePath)
                runObject.slaStatus = runObject.newrelicLog.slaPassed;
            } catch (err) {}
            variables.reports.set(runObject.id, runObject);
        });
    });
};
