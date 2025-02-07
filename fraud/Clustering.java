import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Clustering {
    // initialize an array to store clusters
    private int[] clusterArray;
    // initialize an int to store number of clusters to use across 3 methods
    private int numCluster;

    // run the clustering algorithm and create the clusters
    public Clustering(Point2D[] locations, int k) {
        numCluster = k;
        if (locations == null || numCluster <= 0 || numCluster > locations.length) {
            throw new IllegalArgumentException();
        }
        EdgeWeightedGraph graph = new EdgeWeightedGraph(locations.length);
        for (int i = 0; i < locations.length; i++) {
            for (int j = i + 1; j < locations.length; j++) {
                graph.addEdge(new Edge(i, j,
                                       locations[i].distanceSquaredTo(locations[j])));
            }
        }
        // applying MST algorithms
        KruskalMST mst = new KruskalMST(graph);
        EdgeWeightedGraph clusterGraph = new EdgeWeightedGraph(locations.length);
        int countEdge = 0;
        for (Edge edge : mst.edges()) {
            if (countEdge >= locations.length - numCluster) {
                break;
            }
            clusterGraph.addEdge(edge);
            countEdge++;
        }
        // establish connected components array that stores id (for Boosting)
        CC connect = new CC(clusterGraph);
        clusterArray = new int[locations.length];
        for (int i = 0; i < locations.length; i++) {
            clusterArray[i] = connect.id(i);
        }
    }

    // return the cluster of the ith location
    public int clusterOf(int i) {
        if (i < 0 || i >= clusterArray.length) {
            throw new IllegalArgumentException();
        }
        return clusterArray[i];
    }

    // use the clusters to reduce the dimensions of an input
    public int[] reduceDimensions(int[] input) {
        if (input == null || input.length != clusterArray.length) {
            throw new IllegalArgumentException();
        }
        int[] reducedOutput = new int[numCluster];
        for (int i = 0; i < input.length; i++) {
            int clusterLeader = clusterOf(i);
            reducedOutput[clusterLeader] += input[i];
        }
        return reducedOutput;
    }

    // unit testing (required)
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException();
        }
        int c = Integer.parseInt(args[0]);
        int p = Integer.parseInt(args[1]);
        Point2D[] centers = new Point2D[c];

        int count = 0;
        while (count < c) {
            double x = StdRandom.uniformDouble(0, 1000);
            double y = StdRandom.uniformDouble(0, 1000);
            Point2D candidate = new Point2D(x, y);
            boolean isValid = true;
            for (int i = 0; i < count; i++) {
                if (centers[i].distanceSquaredTo(candidate) < 16) {
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                centers[count++] = candidate;
            }
        }
        // using polar coordinates to generate points within the circle
        Point2D[] locations = new Point2D[c * p];
        int index = 0;
        for (Point2D center : centers) {
            for (int j = 0; j < p; j++) {
                double r = StdRandom.uniformDouble(0, 1);
                double theta = StdRandom.uniformDouble(0, 2 * Math.PI);
                double x = center.x() + r * Math.cos(theta);
                double y = center.y() + r * Math.sin(theta);
                locations[index++] = new Point2D(x, y);
            }
        }

        Clustering cluster = new Clustering(locations, c);
        for (int i = 0; i < c; i++) {
            for (int j = 1; j < p; j++) {
                if (cluster.clusterOf(i * p) != cluster.clusterOf(i * p + j)) {
                    StdOut.println("Error!");
                    return;
                }
            }
        }

        // Successfully created clusters
        StdOut.println("Clustering completed successfully.");
    }
}
