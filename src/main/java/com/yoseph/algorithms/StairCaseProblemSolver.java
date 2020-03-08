package com.yoseph.algorithms;

import java.util.LinkedList;
import java.util.List;

public class StairCaseProblemSolver {
    /**
     * Given N stairs and a person standing at the bottom wants to reach the top.
     * He could climb any number of steps from the given array arr[] of positive integers.
     * The task is to find the count of all possible ways to reach the top.
     * Input: arr[] = {1, 3, 5}, N = 5
     */

    public static void findCountOfPaths(int N, int[] arr) throws Exception {
        if (N <= 0 || arr == null || arr.length == 0) {
            throw new Exception("Invalid situation to climb");
        }
        List<String> list = new LinkedList<>();
        findListOfPaths(N, arr, new StringBuilder(), list);
        int ct = 0 ;
        for (String path : list) {
            char[] chars = path.toCharArray() ;
            ct += chars.length ;
            for (char ch : chars) {
                System.out.print(ch + "-->");
            }
            System.out.println();
        }
        System.out.println("Count of steps possibly taken obtained from list is : " +  ct );
        Integer[] count = {0} ;
        findCountOfPaths(N, arr, count);
        //finding Paths using DP memoization
        System.out.println("Count of possible paths using DP  is : " +  StairCaseProblemSolver.findPossiblePathsDP(N , arr) );
        int counter = findCountOfPaths_Two(N, arr);
        System.out.println("Count of possible paths using a REFERENCE TYPE COUNTER : " +  counter );
        int numberOfSteps = findNumberOfPossibleSteps(N , arr, new Integer(0)) ;
       // System.out.println("Count of steps possibly taken is : " +  numberOfSteps );
    }

    /**
     * One way of solving the recursion with out returning a value but by just using a reference type as a memorized value
     * @param n
     * @param arr
     * @param count
     */
    private static void findCountOfPaths(int n, int[] arr, Integer[] count){
        if(n < 0){
            return;
        }
        if( n == 0){
            count[0] = count[0] + 1;
        }
        for(int setps : arr){
            findCountOfPaths(n - setps, arr, count);
        }
    }

    /**
     *  Below method solved stair-case problem by using a return type
     *
     */
    private static int findCountOfPaths_Two(int N, int[] arr ) throws Exception {
        if( N < 0){
            return 0;
        }
        if( N == 0){
            return 1;
        }
        int pathCount = 0 ;
        for ( int steps : arr) {
            pathCount = pathCount + findCountOfPaths_Two(N - steps, arr) ;
        }
        return  pathCount;
    }

    private static int findNumberOfPossibleSteps(int N, int[] arr, Integer value) {
        if(N == 0) {
            return value ;
        }
        int count = 0 ;
        for(int steps : arr){
            if(N -steps >= 0){
                System.out.print(N  + "-->");
                count = count + findNumberOfPossibleSteps(N - steps, arr, value + 1)  ;
            }
        }
        return count;
    }


    private static void findListOfPaths(int n, int[] arr, StringBuilder sb, List<String> paths) {
        if (n < 0) {
            return;
        }
        if (n == 0) {
            sb.append(n);
            paths.add(sb.reverse().toString());
        }

        for (int steps : arr) {
            StringBuilder sb1 = new StringBuilder(sb);
            sb1.append(n);
            findListOfPaths(n - steps, arr, sb1, paths);
        }
    }

    /**
     * finding possible paths of the stair case problem using DP
     * @param
     * @throws Exception
     */
    public static int findPossiblePathsDP(int n , int[] arr){
        //we are going to build a momoized array that can hold values that tells how paths are taken to raach to that point
        //taking any one of the steps in the array arr
        int[] momoizedArr = new int[n+1] ;
        //lets set the base cases
        momoizedArr[0] = 1;
        momoizedArr[1] = 1;
        for(int i=2; i < momoizedArr.length ; i++){
            for(int j = 0; j < arr.length ; j++){
                if( i - arr[j] >= 0){
                    momoizedArr[i] += momoizedArr[ i - arr[j]] ;
                }
            }
        }
        return momoizedArr[momoizedArr.length - 1] ;
    }

    public static void main(String[] args) throws Exception {
        int N = 5;
        int[] arr = {1, 2, 3};
        StairCaseProblemSolver.findCountOfPaths(N, arr);
    }
}
