var EMPTY = '';
var SPACE = ' ';
var PUNCTUATION = '!"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~´'; // maybe you cant some chars, paste to pastebin

// libraries
var _ = require('lodash');
var stemmer = require('stemmer');

var punctuationRegEx = new RegExp(PUNCTUATION.split(EMPTY).join('|\\'), 'g');
var stopwords = require('./stopwords.json').sort();
var common = require('./common-words.json').sort();
stopwords.push('br');

function _includes(arr, item) {
    return _.sortedIndexOf(arr, item) > -1;
}

module.exports = function(text) {
    var words = text.split(SPACE);
    var processed = EMPTY;

    for (var w in words) {
        var word = words[w].trim();

        // ignores empty or one char words
        if (word.length < 2) continue;

        // ignores numbers
        if (word.match(/\d+/)) continue;

        // removes any punctuation
        word = word.replace(punctuationRegEx, EMPTY);

        // ignores stopwords
        if (_includes(stopwords, word)) continue;

        // stemming
        word = stemmer(word);

        // ignores common words
        if (_includes(common, word)) continue;

        processed += SPACE + word;
    }

    return processed.trim();
}