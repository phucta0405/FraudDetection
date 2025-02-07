Programming Assignment 7: Fraud Detection

/* *****************************************************************************
 *  Describe how you implemented the Clustering constructor
 **************************************************************************** */
I first added the edges in with the weight is the squared distance between
each entry to each other. Then in order to have a minimum spanning tree, I
constructed a Kruskal algorithm spanning tree, then I clustred them into k clusters
by the MST I constructed above. Then, since I want each of the member of the cluster
would have the same id cluster so I implemented a CC, such that I created a new
array and paste in there .id of them (works the same logic as leaders in union-find)

/* *****************************************************************************
 *  Describe how you implemented the WeakLearner constructor
 **************************************************************************** */
- I run through all dimensions to transfer their vp featurevalues into a single array,
then for the first Vp. I sorted the array after that in order to achieve the desired
time complexity for the assignemnt, I will explain. First, I scan through everything
to get the corrected weight of the line below every point, then for later,
I will just update it by moving up vpIndex in the array and updates by this principle:
used to be incorrectly classified by the previous line: add, used to be correctly classified: deduct
So that I compute the weight in nlogn time.

/* *****************************************************************************
 *  Consider the large_training.txt and large_test.txt datasets.
 *  Run the boosting algorithm with different values of k and T (iterations),
 *  and calculate the test data set accuracy and plot them below.
 *
 *  (Note: if you implemented the constructor of WeakLearner in O(kn^2) time
 *  you should use the small_training.txt and small_test.txt datasets instead,
 *  otherwise this will take too long)
 **************************************************************************** */

      k          T         test accuracy       time (seconds)
   --------------------------------------------------------------------------
       2        50           0.60125               0.111
       5        100          0.6625                0.337
       10       150          0.9625                0.624
       15       200          0.9725                0.958
       20       250          0.97                  1.352
       25       300          0.97125               1.726
       30       400          0.9725                2.543
       50       500          0.9775                4.632
       75       750          0.9713                9.07
       100      1000         0.9488                14.901

/* *****************************************************************************
 *  Find the values of k and T that maximize the test data set accuracy,
 *  while running under 10 second. Write them down (as well as the accuracy)
 *  and explain:
 *   1. Your strategy to find the optimal k, T.
 *   2. Why a small value of T leads to low test accuracy.
 *   3. Why a k that is too small or too big leads to low test accuracy.
 **************************************************************************** */
1.
Possibly first you have to increment both k and T until you get the highest percentage
without passing 10s, then you can start testing different k with the same iterations to see
the optimal k that follows this priority order (1. highest accurary, 2. least time)

2.
You are not having that much weak learners that trained your data to predict correctly.
Weaklearner provides an insight on how the weight of the experts in the previous
learners has done, and therefore optimize the next move.

3.
k too small means that all points are in one (or small number) of clusters.
That will lead to ineffective reduction since everything are in the same cluster which
creates little representation effect for Clustering to capture effectively.
High k means leads to every point might have its own cluster, leading to the fact that
the points would contribute to the noise of data, causing overfitting
so that the model can only predicts the data itself but fails in applying to predict new data points

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
Pretty annoying weakleaners
