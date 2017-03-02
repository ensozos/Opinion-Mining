var fs = require('fs');
var _ = require('lodash');
var path = require('path');

var TYPE_POS = 1;
var TYPE_NEG = -1;

var MIN_RATIO = 1.4;

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

function makeWordObj(word) {
    return {
        "word": word,
        "appearances": 0,
        "numberOfFiles": 0,
        "numberOfPosFiles": 0,
        "numberOfNegFiles": 0
    }
}

function getFileType(file) {
    var parts = file.split('_');
    return parts[2] == "pos" ? TYPE_POS: TYPE_NEG;
}

function fixedWidth(str, w) {
    var len = str.length;

}

var wordsIndex = {};
var stats = [];

var trainingDir = "./preprocessed-reviews/train";
var files = fs.readdirSync(trainingDir);

files = _.filter(files, function(file) {
    if (path.extname(file) != ".txt")
        return false;
    return true;
});

var NUMBER_ALL = 0;
var NUMBER_POS = 0;
var NUMBER_NEG = 0;

for (var i in files) {
    NUMBER_ALL++;

    var wordsInFile = [];
    var filename = files[i];
    var content = fs.readFileSync(trainingDir + '\\' + filename, 'utf8').replace("\n", " ").trim();
    var words = content.split(" ");

    var type = getFileType(filename);
    if (type == TYPE_POS)
        NUMBER_POS++;
    else
        NUMBER_NEG++;

    for (var j in words) {
        var word = words[j];
        var wi, obj;

        if (_.has(wordsIndex, word)) {
            wi = wordsIndex[word];
            obj = stats[wi];
        } else {
            wi = stats.length;
            wordsIndex[word] = wi;
            obj = makeWordObj(word);
            stats.push(makeWordObj(word));
        }

        obj.appearances = obj.appearances + 1;

        if (!_.includes(wordsInFile, word)) {
            wordsInFile.push(word);

            obj.numberOfFiles = obj.numberOfFiles + 1;

            if (type == TYPE_POS) {
                obj.numberOfPosFiles = obj.numberOfPosFiles + 1;
            } else {
                obj.numberOfNegFiles = obj.numberOfNegFiles + 1;
            }
        }

        stats[wi] = obj;
    }

}

var sortedStats = _.sortBy(stats, function(item) {
    return -item.numberOfFiles/NUMBER_ALL;
});

var finalContent = "";
var wordsToBeRemoved = [];

for (var i in sortedStats) {
    var obj = sortedStats[i];
    var line = "";

    var percentAll = (100*obj.numberOfFiles/NUMBER_ALL);
    var percentPos = (100*obj.numberOfPosFiles/NUMBER_POS);
    var percentNeg = (100*obj.numberOfNegFiles/NUMBER_NEG);
    var ratio = Math.max(percentPos, percentNeg)/Math.min(percentPos, percentNeg);

    if (ratio < MIN_RATIO)
        wordsToBeRemoved.push(obj.word);

    line += _.padEnd(obj.word, 20);
    line += _.padEnd(percentAll.toFixed(2) + '%', 7);
    line += _.padEnd(percentPos.toFixed(2) + '%', 7);
    line += _.padEnd(percentNeg.toFixed(2) + '%', 7);
    line += _.padEnd(ratio.toFixed(2), 6);

    finalContent += line + "\n";
}

// fs.writeFileSync('./results.txt', JSON.stringify(sortedStats, null, 4));
fs.writeFileSync('./results.txt', finalContent);
fs.writeFileSync('./words.txt', JSON.stringify(wordsToBeRemoved, null, 4));

// console.log(sortedStats);