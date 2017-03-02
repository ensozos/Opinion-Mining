package classifier;

import net.sf.javaml.tools.weka.WekaClassifier;
import weka.classifiers.functions.SMO;

/**
 * Created by ilias on 6/1/2017.
 */
public class SupportVector extends Classifier {

    public SupportVector() {
        SMO smo = new SMO();

        classifier = new WekaClassifier(smo);
    }

}
