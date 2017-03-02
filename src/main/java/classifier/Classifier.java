package classifier;

import net.sf.javaml.classification.evaluation.CrossValidation;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * Created by ilias on 4/1/2017.
 */
public abstract class Classifier {

    protected net.sf.javaml.classification.Classifier classifier;

    public void checkAccuracy(DefaultDataset trainDataset,int folds) {
        CrossValidation validation = new CrossValidation(classifier);
        validation.crossValidation(trainDataset, folds, new Random(System.currentTimeMillis()));
        printResult(validation, trainDataset);
    }

    public void printResult(CrossValidation validation, DefaultDataset traintDataset) {

        Map<Object, PerformanceMeasure> performanceMeasureMap = validation.crossValidation(traintDataset, 6, new Random(1));

        for (Object o : performanceMeasureMap.keySet()) {
            System.out.println(o + ": <TP> " + performanceMeasureMap.get(o).getTPRate());
            System.out.println(o + ": <FN> " + performanceMeasureMap.get(o).getFNRate());
            System.out.println(o + ": <FP> " + performanceMeasureMap.get(o).getFPRate());
            System.out.println(o + ": <TN> " + performanceMeasureMap.get(o).getTNRate());
            System.out.println(o + ":" + performanceMeasureMap.get(o).getAccuracy());
        }
    }

    public ArrayList<String> evaluate(DefaultDataset trainset, DefaultDataset testset) {
        classifier.buildClassifier(trainset);

        ArrayList<String> list = new ArrayList<String>();

        int i = 0;
        for (Instance inst : testset) {
            if (++i % 30 == 0)
                System.out.println(i);

            Object predictedClassValue = classifier.classify(inst);
            list.add(predictedClassValue.toString().equalsIgnoreCase("POSITIVE") ? "1" : "0");
        }

        return list;
    }

}
