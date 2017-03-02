import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.SparseInstance;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TFIDFMatrix {
    DefaultDataset matrix;

    public TFIDFMatrix() {
    }

    public static double calcIdfClassic(int N, int nt) {
        return (Math.log(N / (double) nt)) / Math.log(N);
    }

    public static double calcTfClassic(int freq, int max_freq) {
        return freq / (double) max_freq;
    }

    public static double calcIdf(int N, int nt) {
        return Math.log(1 + (N / nt));
    }

    public static double calcTf(int freq) {
        return 1 + Math.log(freq);
    }

    public List<Double> getWeigth() {
        List<Double> list = new ArrayList<Double>();
        return list;
    }

    public DefaultDataset getDataset() {
        return matrix;
    }

    public static DefaultDataset createMatrix(InvertedIndex ii, ReviewsReader rr, byte type) {
        if (type == ReviewsReader.Review.TYPE_TEST)
            return createTestMatrix(ii, rr);

        int reviewsLength = rr.getReviewsNumber();
        int wordsLength = ii.getWordsNumber();
        boolean noLabels = type == ReviewsReader.Review.TYPE_TEST;

        DefaultDataset matrix = new DefaultDataset();

        ArrayList<String> allWords = new ArrayList<String>(ii.getCatalog().keySet());
        for (int id = 0; id < reviewsLength; id++) {
            ReviewsReader.Review review = rr.getReview(id);
            SparseInstance instance;

            if (noLabels)
                instance = new SparseInstance(wordsLength);
            else
                instance = new SparseInstance(wordsLength, review.getStringLabel());

            String[] docWords = review.getWords();

            for (String word : docWords) {
                int ftd = ii.getWordFreqInDoc(word, id);

                if (ftd == 0) {
                    continue;
                }

                double nf = calcTfClassic(ftd, ii.getWordMaxFreqInDoc(id));
                int nt = ii.getWordFreqInCatalog(word);
                double idf = calcIdfClassic(reviewsLength, nt);
                double weight = nf * idf;

                if (weight > 0) {
                    int j = Collections.binarySearch(allWords, word);
                    instance.put(j, nf * idf);
                }
            }

            matrix.add(instance);
        }

        return matrix;
    }

    public static DefaultDataset createTestMatrix(InvertedIndex ii, ReviewsReader rr) {
        System.out.println("CREATE USING TEST!!");
        DefaultDataset matrix = new DefaultDataset();
        int reviewsLength = rr.getReviewsNumber();
        int wordsLength = ii.getWordsNumber();
        ArrayList<String> allWords = new ArrayList<String>(ii.getCatalog().keySet());

        for (int id = 0; id < reviewsLength; id++) {
            ReviewsReader.Review review = rr.getReview(id);
            SparseInstance instance = new SparseInstance(wordsLength);

            String[] docWords = review.getWords();
            for (String word : docWords) {
                if (!ii.hasWord(word))
                    continue;

                int ftd = getWordFreqInTestReview(word, docWords);
                double nf = calcTfClassic(ftd, review.getWordMaxFreq());
                int nt = ii.getWordFreqInCatalog(word);
                double idf = calcIdfClassic(reviewsLength, nt); // maybe change reviewsLength
                double weight = nf * idf;

                if (weight > 0) {
                    int j = Collections.binarySearch(allWords, word);
                    instance.put(j, nf * idf);
                }
            }

            matrix.add(instance);
        }

        return matrix;
    }

    private static int getWordFreqInTestReview(String word, String[] docWords) {
        int num = 0;
        for (String w: docWords)
            if (w.equals(word))
                num++;

        return num;
    }
}
