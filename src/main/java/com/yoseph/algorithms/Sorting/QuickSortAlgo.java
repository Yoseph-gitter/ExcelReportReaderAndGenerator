package com.yoseph.algorithms.Sorting;

import java.util.Random;

public class QuickSortAlgo {
    public static void main(String[] args) {
        int[] arr = {};
        int[][] result = new int[10000][arr.length];
        long totalTimeInMills = 0 ;
        for (int k = 0; k < 10000; k++) {
            long start = System.nanoTime();
            quickSort(arr);
            long finish = System.nanoTime();
            totalTimeInMills += finish - start;
            printArray(arr);
            result[k] = arr;
            shuffle(arr);
        }
        System.out.println("Time taken by measurement is : "+ totalTimeInMills/10000 + " And time calculated is : " + arr.length*Math.log(arr.length));
    }

    public static void quickSort(int[] arr) {
        quickSortReccursive(arr, 0, arr.length - 1);
    }

    private static void printArray(int[] array) {
        for (int elt : array) {
            System.out.print(elt + "  ");
        }
        System.out.println();
    }

    private static void shuffle(int[] arr) {
        int rand = 0;
        for (int k = arr.length - 1; k > 0; k--) {
            rand = new Random().nextInt(k);
            swap(arr, rand, k);
        }
      //  printArray(arr);
    }

    public static void quickSortReccursive(int[] arr, int start, int end) {
        if (start >= end) {
            return;
        }
        int pivot = findPivot(arr, start, end);
        //at this point left is greater or equal to end
        quickSortReccursive(arr, start, pivot - 1);
        quickSortReccursive(arr, pivot + 1, end);

    }

    private static int findPivot(int[] arr, int start, int end) {
        //Select the PIVOT
        int pivot = new Random().nextInt(end - start) + start;
        swap(arr, pivot, end);
       // System.out.println("Pivot is : " + pivot);
        pivot = end;
        int runRight = start;
        int runLeft = end;

        while (runRight < runLeft) {
            //move pointer to the right while each elet is less than the pivot
            while (runRight < end && arr[runRight] < arr[pivot]) {
                runRight++;
            }
            //move left counter if left value is greater than pivot
            while (runLeft > 0 && arr[runLeft] > arr[pivot]) {
                runLeft--;
            }
            swap(arr, runRight, runLeft);
        }
        //pivot is exactly sorted now
        swap(arr, runLeft, pivot);

        return runLeft;
    }

    private static void swap(int[] arr, int start, int end) {
        int temp = arr[start];
        arr[start] = arr[end];
        arr[end] = temp;
    }
}
