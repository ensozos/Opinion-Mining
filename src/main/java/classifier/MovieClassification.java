package classifier;

import net.sf.javaml.core.DefaultDataset;
import java.util.ArrayList;

/**
 * Created by ilias on 4/1/2017.
 */
public class MovieClassification {

    private static final int FOALDS = 10;
    private ClassificationFactory classificationFactory;
    private DefaultDataset traintDataset;
    private classifier.Classifier mCl;

    public MovieClassification(DefaultDataset traintDataset, ClassifierType type) {
        this.traintDataset = traintDataset;
        classificationFactory = new ClassificationFactory();
        mCl = classificationFactory.getClassifier(type);
    }

    public void checkClassificationAccuracy() {
        mCl.checkAccuracy(traintDataset, FOALDS);
    }

    public ArrayList<String> classificationEvaluation(DefaultDataset testDataset) {
        return mCl.evaluate(traintDataset, testDataset);
    }

}
