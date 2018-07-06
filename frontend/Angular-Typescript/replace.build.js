var replace = require('replace-in-file');
var package = require("./package.json");

const files = 'dist/**/main*.js';

const timestamp = {
    files: files,
    from: /__ENV_BUILD_TIMESTAMP__/g,
    to: new Date().getTime(),
    allowEmptyPaths: false,
};

const version = {
    files: files,
    from: /__ENV_BUILD_VERSION__/g,
    to: package.version,
    allowEmptyPaths: false,
};


let changedFiles = replace.sync(timestamp);
changedFiles += replace.sync(version);
if (changedFiles < 2) {
    throw "Cannot set build timestamp or version to '" + files + "'";
}
console.log(`Modified build files with version=[${version.to}], timestamp [${timestamp.to}]`);