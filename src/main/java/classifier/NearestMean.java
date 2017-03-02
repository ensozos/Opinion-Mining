package classifier;

import net.sf.javaml.classification.NearestMeanClassifier;

/**
 * Created by ilias on 6/1/2017.
 */
public class NearestMean extends Classifier {

    public NearestMean() {
        classifier = new NearestMeanClassifier();
    }

}
