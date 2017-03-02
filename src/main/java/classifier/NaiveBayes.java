package classifier;

import net.sf.javaml.classification.bayes.NaiveBayesClassifier;

/**
 * Created by ilias on 6/1/2017.
 */
public class NaiveBayes extends Classifier {

    public NaiveBayes() {
        classifier = new NaiveBayesClassifier(true, true, true);
    }

}
