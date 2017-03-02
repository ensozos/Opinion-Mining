import classifier.ClassifierType;
import classifier.MovieClassification;
import net.sf.javaml.core.DefaultDataset;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MovieReviewMining {
    private String inputDir;
    private String trainingDir;
    private String testDir;

    public MovieReviewMining(String[] args) {
        setDirs(args);

        evaluateAccuracy(ClassifierType.NEAREST_MEAN);
//        classifyTestData(ClassifierType.LOGISTIC_BAYES_REGRESSION, "predictions.txt");
    }

    /**
     * Evaluates the accuracy of a classifier using cross-validation
     *
     * @param type the classifier type
     */
    private void evaluateAccuracy(ClassifierType type) {
        System.out.println("STARTED");
        ReviewsReader trainReader = new ReviewsReader(trainingDir, ReviewsReader.Review.TYPE_TRAIN);
        trainReader.shuffle();
        InvertedIndex trainInvertedIndex = new InvertedIndex(trainReader);
        System.out.println("Inverted Index created.");
        DefaultDataset trainTfidfMatrix = TFIDFMatrix.createMatrix(trainInvertedIndex, trainReader, ReviewsReader.Review.TYPE_TRAIN);
        System.out.println("TF-IDF matrix created.");

        MovieClassification movieClassification = new MovieClassification(trainTfidfMatrix, type);
        movieClassification.checkClassificationAccuracy();
    }

    /**
     *  Appoints every instance of the test data to a class
     *
     * @param type the classifier type
     */
    private void classifyTestData(ClassifierType type, String filename) {
        System.out.println("STARTED");
        ReviewsReader trainReader = new ReviewsReader(trainingDir, ReviewsReader.Review.TYPE_TRAIN);
        trainReader.shuffle();
        InvertedIndex trainInvertedIndex = new InvertedIndex(trainReader);
        System.out.println("Inverted Index created.");
        DefaultDataset trainTfidfMatrix = TFIDFMatrix.createMatrix(trainInvertedIndex, trainReader, ReviewsReader.Review.TYPE_TRAIN);
        System.out.println("TF-IDF matrix created.");
        ReviewsReader testReader = new ReviewsReader(testDir, ReviewsReader.Review.TYPE_TEST);
        DefaultDataset testTfidfMatrix = TFIDFMatrix.createMatrix(trainInvertedIndex, testReader, ReviewsReader.Review.TYPE_TEST);
        System.out.println("TF-IDF matrix for test dataset created.");

        MovieClassification movieClassification = new MovieClassification(trainTfidfMatrix, type);
        writeResult(movieClassification.classificationEvaluation(testTfidfMatrix), filename);
    }

    /**
     * Saves the results of the classification to a file
     *
     * @param result the list containing the results
     * @param filename the name of the file
     */
    private void writeResult(ArrayList<String> result, String filename) {
        try {
            String sep = System.getProperty("file.separator");
            PrintWriter writer = new PrintWriter(System.getProperty("user.home") + sep +
                    "Desktop" + sep + filename, "UTF-8");

            final DecimalFormat decimalFormat = new DecimalFormat("00000");
            for (int i = 0; i < result.size(); i++) {
                writer.println(decimalFormat.format(i) + " " + result.get(i));
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the directory from where the application will read the data
     *
     * @param args the command line arguments
     */
    private void setDirs(String[] args) {
        String sep = System.getProperty("file.separator");

        if (args.length < 1) {
            inputDir = System.getProperty("user.home") + sep + "Desktop" + sep + "reviews";
        } else {
            inputDir = args[0];
        }

        trainingDir = inputDir + sep + "train";
        testDir = inputDir + sep + "test";
    }

    public static void main(String[] args) {
        new MovieReviewMining(args);
    }
}
