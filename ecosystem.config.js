module.exports = {
    apps : [
        {
            name: "gatling",
            script: "./public/index.js",
            watch: true,
            env: {
                "PORT": "80",
            }
        }
    ]
}
