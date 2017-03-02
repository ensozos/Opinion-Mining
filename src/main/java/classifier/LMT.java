package classifier;

import net.sf.javaml.tools.weka.WekaClassifier;

/**
 * Created by ilias on 22/2/2017.
 */
public class LMT extends Classifier {

    public LMT() {

        try {
            weka.classifiers.trees.LMT lmt = new weka.classifiers.trees.LMT();
            classifier = new WekaClassifier(lmt);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
