package classifier;

/**
 * Created by ilias on 4/1/2017.
 */
public class ClassificationFactory {

    public Classifier getClassifier(ClassifierType type) {

        if (type == null) return null; //or default
        if (type == ClassifierType.KNN) {
            return new Knn(10);
        } else if (type == ClassifierType.SUPPORT_VECTOR) {
            return new SupportVector();
        } else if (type == ClassifierType.NEAREST_MEAN) {
            return new NearestMean();
        } else if (type == ClassifierType.NAIVE_BAYES) {
            return new NaiveBayes();
        } else if (type == ClassifierType.RANDOM_FOREST) {
            return new RandomForest();
        } else if (type == ClassifierType.LOGISTIC_BAYES_REGRESSION) {
            return new LogisticRegression();
        } else if (type == ClassifierType.SIMPLE_LOGISTIC) {
            return new SimpleLogistic();
        } else if (type == ClassifierType.LMT) {
            return new LMT();
        } else if (type == ClassifierType.VOTED_PERCEPTRON) {
            return new VotedPerceptron();
        }

        return null;
    }

}
