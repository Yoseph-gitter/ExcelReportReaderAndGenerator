package com.yoseph.algorithms;
import java.util.LinkedList;
import java.util.List;
import java.lang.* ;

public class SubsetProblemSolver {
    public static void main(String[] args) throws Exception {
        int[] arr = {1, 2, 4};
        String sumValue = SubsetProblemSolver.findSumOf(SubsetProblemSolver.findSubsetIteratively(arr), 5);
        String sumValue2 = SubsetProblemSolver.findSumOf(SubsetProblemSolver.findSubsetWithSumRecursively(arr), 5);
        System.out.printf("Iteratively {%s} and Recursively {%s}", sumValue, sumValue2);
        String sumValue3 = SubsetProblemSolver.findSumOf(SubsetProblemSolver.findSubsetUsingBits(arr), 5);
        System.out.printf("Iteratively {%s} and subsets {%s}", "sumValue", sumValue3);
    }

    /**
     * Iterative way of finding a subset with a given sum
     * @param arr
     */
    public static List<StringBuilder> findSubsetIteratively(int[] arr) {
        StringBuilder sb = new StringBuilder();
        List<StringBuilder> subsets = new LinkedList<>();
        for (int pointer = 0; pointer < arr.length; pointer++) {
            List<StringBuilder> prevSubset = new LinkedList<>();
            sb.append(arr[pointer]);
            prevSubset.add(sb);
            prevSubset.addAll(subsets);
            for (StringBuilder stringBuilder : subsets) {
                sb = new StringBuilder(stringBuilder);
                sb.append(arr[pointer]);
                prevSubset.add(sb);
                System.out.println(sb + "<>");
            }
            subsets = prevSubset;
            sb = new StringBuilder();
        }
        return subsets;
    }

    private static String findSumOf(List<StringBuilder> subsets, int SumValue) {
        int sum = 0;
        System.out.println("SUM is :" + sum);
        for (StringBuilder stringBuilder : subsets) {
            for (int i = 0; i < stringBuilder.length(); i++) {
                sum += Integer.valueOf(stringBuilder.charAt(i)) - '0';
                System.out.println("SUM is :" + sum);
                if (sum == SumValue) {
                    return stringBuilder.toString();
                }
            }
            sum = 0;
        }
        return null;
    }

    /**
     * Given a set of non-negative integers, and a value sum,
     * determine if there is a subset of the given set with sum equal to given sum.
     */
    public static List<StringBuilder> findSubsetWithSumRecursively(int[] arr) throws Exception {
        if (arr == null || arr.length == 0) {
            throw new Exception("Empty Array");
        }
        List<StringBuilder> subsets = new LinkedList<>();
        findSubsetWithSum(arr, 0, subsets);
        return subsets;
    }

    public static void findSubsetWithSum(int[] arr, int start, List<StringBuilder> subsets) {
        if (start == arr.length) {
            return;
        }
        if( start == 0){
            StringBuilder s = new StringBuilder(arr[start] + "") ;
            subsets.add(s);
            findSubsetWithSum(arr, start + 1, subsets) ;
            return;
        }
        List<StringBuilder> localSubset = new LinkedList<>(subsets);
        localSubset.add(new StringBuilder(arr[start] + "") );

        for (StringBuilder sub : localSubset) {
            StringBuilder newSb = new StringBuilder(sub);
            newSb.append(arr[start] + "");
            subsets.add(newSb);
            System.out.println(newSb);
        }
        findSubsetWithSum(arr, start + 1, subsets);
    }

    /**
     * By Just enumerating the number from 1 to Math.pow(2,n) - 1 we can generate the subset of all the n- elements
     * @param arr
     * @return
     */
    public static List<StringBuilder> findSubsetUsingBits(int[] arr){
        List<StringBuilder> subsets = new LinkedList<>();
        StringBuilder binaryDigits= null ;
        for(int index =1 ; index <= Math.pow(2,arr.length) -1 ; index++){
            binaryDigits = convertToBinary(index);
            System.out.println(binaryDigits);
            StringBuilder sb = new StringBuilder();
            for(int j=0 ; j < binaryDigits.length() ; j++){
                if(binaryDigits.charAt(j) == '1'){
                    sb.append(arr[j]);
                }
            }
            subsets.add(sb);
        }
        return subsets ;
    }

    private static StringBuilder convertToBinary(int index) {
        int remender = 0 ;
        StringBuilder sb= new StringBuilder();
        int quotient = index ;
        while (quotient!=0){
            remender = quotient%2 ;
            quotient = quotient/2 ;
            sb.append(remender);
        }
        return sb.reverse();
    }
}
