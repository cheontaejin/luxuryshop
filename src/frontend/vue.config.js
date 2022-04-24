const path = require("path");

module.exports = {
    transpileDependencies: ["vuetify"],

    devServer: {
        proxy: {
            "/": {
                target: "http://localhost:7000",
                ws: true,
                changeOrigin: true
            }
        },
        port: 9001
    },
    outputDir: path.resolve("__dirname", "../../main/resources/static"),
};
