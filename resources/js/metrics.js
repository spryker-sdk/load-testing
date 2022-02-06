module.exports = metricsLimit = {
    zed: {
        get: {
            avg: 200,
            max: 220,
        }
    },
    backend: {
        post: {
            50: {
                avg: 300,
                max: 400,
            },
            70: {
                avg: 500,
                max: 600,
            }
        },
        get: {
            avg: 150,
            max: 180,
        }
    }
}
