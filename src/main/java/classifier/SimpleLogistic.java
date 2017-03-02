package classifier;

import net.sf.javaml.tools.weka.WekaClassifier;

/**
 * Created by ilias on 19/2/2017.
 */
public class SimpleLogistic extends Classifier {

    public SimpleLogistic() {

        weka.classifiers.functions.SimpleLogistic simpleLogistic = new weka.classifiers.functions.SimpleLogistic();
        classifier = new WekaClassifier(simpleLogistic);
    }

}
