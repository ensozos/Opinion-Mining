package classifier;

import net.sf.javaml.tools.weka.WekaClassifier;
import weka.classifiers.bayes.BayesianLogisticRegression;

/**
 * Created by ilias on 18/2/2017.
 */
public class LogisticRegression extends Classifier {

    public LogisticRegression() {
        BayesianLogisticRegression bayesianLogisticRegression = new BayesianLogisticRegression();
        classifier = new WekaClassifier(bayesianLogisticRegression);
    }

}
