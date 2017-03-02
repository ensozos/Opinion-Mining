package classifier;

import net.sf.javaml.tools.weka.WekaClassifier;

/**
 * Created by ilias on 22/2/2017.
 */
public class VotedPerceptron extends Classifier {

    public VotedPerceptron(){
        weka.classifiers.functions.VotedPerceptron votedPerceptron = new weka.classifiers.functions.VotedPerceptron();
        classifier = new WekaClassifier(votedPerceptron);
    }

}
