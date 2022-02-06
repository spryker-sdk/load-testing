module.exports = class MetricsValidator {
    constructor() {
        this.failed = "red"
        this.passed = "green"
        this.limits = require("./metrics")
    }

    validate(results, area = 'backend', limit= 50) {
        let requestType = results.requestType;
        if (results.requestType !== 'get') {
            requestType = 'post'
        }

        results.avg = results.avg.toFixed(2);
        results.min = results.min.toFixed(2);
        results.max = results.max.toFixed(2);

        let limits = this.getRequestLimits(requestType, area, limit);
        results.slaPassed = results.max <= limits.max || results.avg <= limits.avg;

        return results;
    }

    getRequestLimits(requestType, area, limit) {
        if (requestType === 'get') {
            return this.limits[area]['get'];
        }
        return this.limits[area]['post'][limit];
    }
};
