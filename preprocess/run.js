var preprocess = require('./preprocess');
var fs = require('fs');
var path = require('path');
var _ = require('lodash');

var UNIQUE_ID = 0;
var RAW_DIR = './raw-reviews';
var RESULTS_DIR = './preprocessed-reviews';

function makeDir(dir) {
    if (!fs.existsSync(dir))
        fs.mkdirSync(dir);
}

function format() {
    var args = Array.prototype.slice.call(arguments);
    var str = args.shift();
    
    for (i in args) {
        var val = args[i];
        var reg = new RegExp('\\{' + i + '\\}', 'g');
        
        str = str.replace(reg, val);
    }
    
    return str;
}

function readFile(readFilename, writeFilename) {
    data = fs.readFileSync(readFilename, 'utf8');
    fs.writeFileSync(writeFilename, preprocess(data), 'utf8');
}

function prepFile(label, filename, id) {
    var basename = path.basename(filename, '.txt');
    var orid = basename.split('_')[0];
    
    var readFilename = format('{0}/{1}/{2}', RAW_DIR, label, filename);

    if (label != "test") {
        var rating = basename.split('_')[1];
        var strId = ("00000" + id).slice(-5);
        var writeFilename = format('{0}/train/{1}_{2}_{3}_{4}.txt', RESULTS_DIR, strId, orid, label, rating);
    } else {
        // var writeFilename = format('{0}/test/{1}_{2}.txt', RESULTS_DIR, id, orid);
        var writeFilename = format('{0}/test/{1}.txt', RESULTS_DIR, orid);
    }

    readFile(readFilename, writeFilename);
}

function prepDir(label) {
    var files = fs.readdirSync(RAW_DIR + "/" + label);
    
    // filter files
    files = _.filter(files, function(file) {
        if (path.extname(file) != ".txt")
            return false;
        return true;
    });

    files = _.sortBy(files, function(file) {
        return parseInt(path.basename(file, ".txt"));
    });

    for (var i=0; i<files.length; i++)
        prepFile(label, files[i], UNIQUE_ID + i);

    UNIQUE_ID += files.length;
}

function run(train) {
    if (!fs.existsSync(RAW_DIR))
        throw "Nothing to see here...";

    if (train) {
        makeDir(format('{0}/train', RESULTS_DIR));
        prepDir("pos");
        prepDir("neg");
    } else {
        makeDir(format('{0}/test', RESULTS_DIR));
        prepDir("test");
    }
}

// Initialize dirs
makeDir(RESULTS_DIR);

if (_.includes(process.argv, "-test"))
    run(false);
else
    run(true);