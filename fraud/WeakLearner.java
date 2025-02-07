import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class WeakLearner {
    // dpFinal of the wl
    private int dpFinal;
    // vpFinal of the wl
    private int vpFinal;
    // spFinal of the wl
    private int spFinal;
    // number of dimensions
    private int k;

    // train the weak learner
    public WeakLearner(int[][] input, double[] weights, int[] labels) {
        if (input == null || weights == null || labels == null) {
            throw new IllegalArgumentException();
        }
        if (input.length != weights.length || input.length != labels.length) {
            throw new IllegalArgumentException();
        }
        for (double weight : weights) {
            if (weight < 0) {
                throw new IllegalArgumentException();
            }
        }
        for (int label : labels) {
            if (!(label == 1 || label == 0)) {
                throw new IllegalArgumentException();
            }
        }
        // Number of elements
        int n = input.length;
        // Number of dimensions
        k = input[0].length;
        double currentWeightChampion = Double.NEGATIVE_INFINITY;
        for (int dimension = 0; dimension < k; dimension++) {
            Input[] inputData = new Input[n];
            for (int i = 0; i < n; i++) {
                inputData[i] = new Input(input[i][dimension], labels[i], weights[i]);
            }

            Arrays.sort(inputData);

            for (int sp = 0; sp <= 1; sp++) {
                double allWeight = 0;
                // initialize weights for all points
                for (Input inputType : inputData) {
                    if ((sp == 0 && inputType.label == 1) ||
                            (sp == 1 && inputType.label == 0)) {
                        allWeight += inputType.weight;
                    }
                }
                int i = 0;
                while (i < n) {
                    // used to be incorrectly classified by the previous line: add
                    // used to be correctly classified: deduct
                    if (inputData[i].label == sp) {
                        allWeight += inputData[i].weight;
                    }
                    else {
                        allWeight -= inputData[i].weight;
                    }
                    int j = i;
                    // if there is points on the line, tracing points on the line,
                    // to find the last to update
                    while (j + 1 < n && inputData[j].vp == inputData[j + 1].vp) {
                        j++;
                        if (inputData[j].label == sp) {
                            allWeight += inputData[j].weight;
                        }
                        else {
                            allWeight -= inputData[j].weight;
                        }
                    }
                    // update when found
                    if (allWeight > currentWeightChampion) {
                        currentWeightChampion = allWeight;
                        spFinal = sp;
                        vpFinal = inputData[j].vp;
                        dpFinal = dimension;
                    }
                    // next index candidate (I always forgot this, ty Marcel and Pedro)
                    i = j + 1;
                }
            }
        }
    }


    private static class Input implements Comparable<Input> {
        // vp from input[]
        private int vp;
        // label of the vp
        private int label;
        // weight of the vp
        private double weight;

        // constructor for Input
        public Input(int vp, int label, double weight) {
            this.vp = vp;
            this.label = label;
            this.weight = weight;
        }

        // I don't remember why we have to implement this, but IntelliJ forced
        // me to have this
        public int compareTo(Input that) {
            return Integer.compare(this.vp, that.vp);
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null || sample.length != k) {
            throw new IllegalArgumentException();
        }
        if (spFinal == 0) {
            if (sample[dpFinal] <= vpFinal) {
                return 0;
            }
            else {
                return 1;
            }
        }
        else {
            if (sample[dpFinal] <= vpFinal) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }

    // return the dimension the learner uses to separate the data
    public int dimensionPredictor() {
        return dpFinal;
    }

    // return the value the learner uses to separate the data
    public int valuePredictor() {
        return vpFinal;
    }

    // return the sign the learner uses to separate the data
    public int signPredictor() {
        return spFinal;
    }

    // unit testing
    public static void main(String[] args) {
        In datafile = new In(args[0]);
        int n = datafile.readInt();
        int k = datafile.readInt();
        int[][] input = new int[n][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                input[i][j] = datafile.readInt();
            }
        }

        int[] labels = new int[n];
        for (int i = 0; i < n; i++) {
            labels[i] = datafile.readInt();
        }

        double[] weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = datafile.readDouble();
        }

        WeakLearner weakLearner = new WeakLearner(input, weights, labels);
        StdOut.printf("vp = %d, dp = %d, sp = %d\n", weakLearner.valuePredictor(),
                      weakLearner.dimensionPredictor(), weakLearner.signPredictor());
    }
}
