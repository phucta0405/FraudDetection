import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.List;

public class BoostingAlgorithm {
    // the cluster object as required
    private Clustering cluster;
    // array that stores the weight of each prediction
    private double[] weight;
    // create a defensive label
    private int[] labels;
    // an array that reduces the dimension, making it to n x k
    private int[][] reducedArray;
    // saving weak learners
    private List<WeakLearner> weakLeaners;
    // input length
    private int n;
    // size of the original sample
    private int sizeSample;

    // create the clusters and initialize your data structures
    public BoostingAlgorithm(int[][] input, int[] labels, Point2D[] locations, int k) {
        if (input == null || labels == null || locations == null ||
                k <= 0 || k > locations.length || labels.length != input.length) {
            throw new IllegalArgumentException();
        }
        for (int label : labels) {
            if (!(label == 1 || label == 0)) {
                throw new IllegalArgumentException();
            }
        }
        n = input.length;
        sizeSample = input[0].length;

        cluster = new Clustering(locations, k);
        weight = new double[n];
        for (int i = 0; i < n; i++) {
            weight[i] = 1.0 / n;
        }

        this.labels = labels;
        this.weakLeaners = new ArrayList<>();

        reducedArray = new int[n][k];
        for (int i = 0; i < n; i++) {
            reducedArray[i] = cluster.reduceDimensions(input[i]);
        }
    }


    // return the current weight of the ith point
    public double weightOf(int i) {
        if (i < 0 || i >= weight.length) {
            throw new IllegalArgumentException();
        }
        return weight[i];
    }

    // apply one step of the boosting algorithm
    public void iterate() {
        double sumWeight = 0;
        WeakLearner wl = new WeakLearner(reducedArray, weight, labels);
        for (int i = 0; i < n; i++) {
            if (wl.predict(reducedArray[i]) != labels[i]) {
                weight[i] *= 2;
            }
            sumWeight += weight[i];
        }
        for (int i = 0; i < n; i++) {
            weight[i] /= sumWeight;
        }
        weakLeaners.add(wl);
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null || sample.length != sizeSample) {
            throw new IllegalArgumentException();
        }

        int zeroPrediction = 0;
        int onePrediction = 0;
        int[] reducedDimArray = cluster.reduceDimensions(sample);
        for (WeakLearner wk : weakLeaners) {
            int prediction = wk.predict(reducedDimArray);
            if (prediction == 0) {
                zeroPrediction++;
            }
            else {
                onePrediction++;
            }
        }
        if (zeroPrediction >= onePrediction) {
            return 0;
        }
        else {
            return 1;
        }
    }

    // unit testing
    public static void main(String[] args) {
        // read in the terms from a file
        DataSet training = new DataSet(args[0]);
        DataSet testing = new DataSet(args[1]);
        int k = Integer.parseInt(args[2]);
        int T = Integer.parseInt(args[3]);

        int[][] trainingInput = training.getInput();
        int[][] testingInput = testing.getInput();
        int[] trainingLabels = training.getLabels();
        int[] testingLabels = testing.getLabels();
        Point2D[] trainingLocations = training.getLocations();
        Stopwatch stopwatch1 = new Stopwatch();
        // train the model
        BoostingAlgorithm model = new BoostingAlgorithm(trainingInput, trainingLabels,
                                                        trainingLocations, k);
        for (int t = 0; t < T; t++)
            model.iterate();
        double time1 = stopwatch1.elapsedTime();
        // calculate the training data set accuracy
        double training_accuracy = 0;
        for (int i = 0; i < training.getN(); i++)
            if (model.predict(trainingInput[i]) == trainingLabels[i])
                training_accuracy += 1;
        training_accuracy /= training.getN();

        Stopwatch stopwatch2 = new Stopwatch();
        // calculate the test data set accuracy
        double test_accuracy = 0;
        for (int i = 0; i < testing.getN(); i++)
            if (model.predict(testingInput[i]) == testingLabels[i])
                test_accuracy += 1;
        test_accuracy /= testing.getN();
        double time2 = stopwatch2.elapsedTime();

        StdOut.println("Training accuracy of model: " + training_accuracy);
        StdOut.println("Test accuracy of model: " + test_accuracy);
        StdOut.println("Time: " + (time1 + time2));
    }
}
