package classifier;

import net.sf.javaml.tools.weka.WekaClassifier;

/**
 * Created by ilias on 6/1/2017.
 */
public class RandomForest extends Classifier {

    public RandomForest() {
        weka.classifiers.trees.RandomForest rf = new weka.classifiers.trees.RandomForest();
        classifier = new WekaClassifier(rf);
    }

}
