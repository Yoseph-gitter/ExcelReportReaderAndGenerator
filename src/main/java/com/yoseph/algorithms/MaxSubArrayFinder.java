package com.yoseph.algorithms;

public class MaxSubArrayFinder {

    public static void main(String[] args) throws Exception {
        // int[] arr = { -1, 2, 3 , -2, 1} ;
        int[] arr = { -20, 2, 3 ,1, 300 ,-132, -12, 15, -7,-500, 1000, 4, 6};
        for (int a : arr) {
            System.out.print(a + ",");
        }
        System.out.println();
        long start = System.currentTimeMillis() ;
        System.out.println("Max sub array sum is Using Recursively : " + MaxSubArrayFinder.findMaxSubArrRecursive(arr));
        long finish = System.currentTimeMillis() ;
        long diff = finish - start ;

        start = System.currentTimeMillis() ;
        System.out.println("Max sub array sum is  Using Iteratively : " + MaxSubArrayFinder.findMaxSum(arr));
        finish = System.currentTimeMillis() ;

        System.out.println("Time taken for Recursive is : " + (diff) );
        System.out.println("Time taken for Iteratively is : " + (finish - start) );
    }

    /**
     * Given an array arr of N integers. Find the contiguous sub-array with maximum sum.
     */
    public static int findMaxSum(int[] intArray) throws Exception {
        if (intArray == null || intArray.length == 0) {
            throw new Exception("Empty or Null Array");
        }
        int maxSum = Integer.MIN_VALUE;
        for (int i = 0; i < intArray.length; i++) {
            int maxLocal = findMaxSum(intArray, i);
            maxSum = Math.max(maxSum, maxLocal);
        }
        return maxSum;
    }

    private static int findMaxSum(int[] arr, int start) {
        int max = Integer.MIN_VALUE;
        int sum = 0;
        System.out.println();
        for (int width = 1; width <= arr.length - start; width++) {
           // printElementsWithTheGivenWidth(arr, start, width);
            for (int j = start; j < arr.length ; j++) {
                for (int i = j; i < width ; i++) {
                    sum += arr[i];
                }
                max = Math.max(max, sum);
                sum = 0;
            }
        }
        return max;
    }

    public static int findMaxSubArrRecursive(int[] arr) throws Exception {
        if (arr == null || arr.length == 0) {
            throw new Exception("Empty Array");
        }
        int max = Integer.MIN_VALUE;
        //calculate a max from each element
        for (int i = 0; i < arr.length; i++) {
            int tempMax = findMaxSumSubArrRecursive(arr, i, arr.length - 1);
            max = Math.max(max, tempMax);
        }
        return max ;
    }

    private static int findMaxSumSubArrRecursive(int[] arr, int start, int end) {
        if (start == end) {
            return arr[start];
        }
        System.out.print(arr[start] + "-->");
        int inclusiveSumEnd = arr[start] + findMaxSumSubArrRecursive(arr, start + 1, end);
        int exclusiveSumEnd = findMaxSumSubArrRecursive(arr, start, end - 1);
        System.out.println();
        return Math.max(inclusiveSumEnd, exclusiveSumEnd);
    }

    private static void printElementsWithTheGivenWidth(int[] arr, int start, int width) {
        for (int i = start; i < start + width && i < arr.length; i++) {
            System.out.print(arr[i] + "-->");
        }
        System.out.println();
    }
}