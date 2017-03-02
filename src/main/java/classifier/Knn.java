package classifier;

import net.sf.javaml.classification.KNearestNeighbors;

/**
 * Created by ilias on 6/1/2017.
 */
public class Knn extends Classifier {

    public Knn(int K) {
        classifier = new KNearestNeighbors(K);
    }

}
