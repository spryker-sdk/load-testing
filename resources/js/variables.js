const instanceStore = require('data-store')({path: process.cwd() + '/web/instance.json'});
const jobs = new Map();
const reports = new Map();
let pendingJobs = new Map();
const port = process.env.PORT || 3000;
const host = process.env.HOST || '0.0.0.0';
const destinationFolder = 'web';
const reportSuffix = '/report';


const getInstanceList = () => {
    let projects = new Map(Object.entries(instanceStore.get()));
    projects.forEach((value, key) => value.key = key);

    return projects;
}

let instanceList = getInstanceList();

module.exports = {
    jobs: jobs,
    reports: reports,
    pendingJobs: pendingJobs,
    instanceList: instanceList,
    instanceStore: instanceStore,
    port:port,
    host:host,
    destinationFolder:destinationFolder,
    reportSuffix:reportSuffix,
    getInstanceList: getInstanceList
};
