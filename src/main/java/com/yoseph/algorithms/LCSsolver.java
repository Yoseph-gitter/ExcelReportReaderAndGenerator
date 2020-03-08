package com.yoseph.algorithms;

import java.util.LinkedList;
import java.util.List;

public class LCSsolver {
    public static void main(String[] args) throws Exception {
        String subOne = "Text diff to are the ses";
        String subTwo = "Diffing diffing tools";
        StringBuffer sb = new StringBuffer();
//        StringBuffer result = LCSsolver.lcsCalc(subOne, subTwo, subOne.length() - 1, subTwo.length() - 1, sb);
//        System.out.println("Length LCS :" + result.reverse().toString());
//        int[][] tab = LCSsolver.lcsCalcWithTabulation(subOne, subTwo);
//        System.out.println(LCSsolver.printLcsTabular(tab, subOne, subTwo));
        List<StringBuilder> stringBuilders = new LinkedList<>();
        LCSsolver.findAllLcsOf("ABC", "ADCB", stringBuilders);
        for (StringBuilder sbs : stringBuilders){
            System.out.println("-->" + sbs);
        }

    }

    public static StringBuffer lcsCalc(String subSequenceOne, String subsequenceTwo, int lenOne, int lenTwo, StringBuffer sb) {
        if (subSequenceOne.length() <= 0 || subsequenceTwo.length() <= 0) {
            return sb;
        }

        StringBuffer stringBuffer = new StringBuffer(sb);
        if (subSequenceOne.charAt(lenOne) == subsequenceTwo.charAt(lenTwo)) {
            stringBuffer.append(subSequenceOne.charAt(lenOne));
            lcsCalc(subSequenceOne.substring(0, lenOne), subsequenceTwo.substring(0, lenTwo), lenOne - 1, lenTwo - 1, stringBuffer);
        }
        StringBuffer withOne = lcsCalc(subSequenceOne, subsequenceTwo.substring(0, lenTwo), lenOne, lenTwo - 1, stringBuffer);
        StringBuffer withTwo = lcsCalc(subSequenceOne.substring(0, lenOne), subsequenceTwo, lenOne - 1, lenTwo, stringBuffer);
        return withOne.length() > withTwo.length() ? withOne.reverse() : withTwo.reverse();
    }

    public static int[][] lcsCalcWithTabulation(String lenOne, String lenTwo) {
        int[][] tabular = new int[lenOne.length()][lenTwo.length()];

        for (int row = 0; row < lenOne.length(); row++) {
            for (int col = 0; col < lenTwo.length(); col++) {
                boolean b = lenOne.charAt(row) == lenTwo.charAt(col);
                if (row == 0 || col == 0) {
                    if (b) {
                        tabular[row][col] = 1;
                    } else {
                        tabular[row][col] = 0;
                    }
                } else {
                    //first row and col us updated with
                    int diag = tabular[row - 1][col - 1];
                    int left = tabular[row][col - 1];
                    int top = tabular[row - 1][col];
                    int max = Math.max(Math.max(diag, left), top);
                    if (b) {
                        tabular[row][col] = max + 1;
                    } else {
                        tabular[row][col] = max;
                    }
                }
            }
        }
        return tabular;
    }

    public static void findAllLcsOf(String seqOne, String seqTwo, List<StringBuilder> lcsSet){
        if(seqOne.length() == 0 || seqTwo.length() == 0){
            return ;
        }
        if(seqOne.charAt(0) == seqTwo.charAt(0)){
            StringBuilder sb = new StringBuilder() ;
            String ch = seqOne.substring(0,1) ;
            lcsSet.add(sb.append(ch));
            for(StringBuilder s : lcsSet){
                s.append(sb);
            }
            findAllLcsOf(seqOne.substring(1), seqTwo.substring(1), lcsSet);
        }else {
           // List<StringBuilder> stringBuilderSets = new LinkedList<>();
            findAllLcsOf(seqOne.substring(1), seqTwo, lcsSet);
            findAllLcsOf(seqOne, seqTwo.substring(1), lcsSet);
        }
    }

    public static String printLcsTabular(int[][] tabular, String lenOne, String lenTwo) {
        int len1 = lenOne.length() - 1;
        int len2 = lenTwo.length() - 1;
        System.out.println();
        StringBuffer stringBuffer = new StringBuffer();
        while (len1 >= 0 && len2 >= 0) {
            if (lenOne.charAt(len1) == lenTwo.charAt(len2)) {
               // System.out.print(lenOne.charAt(len1));
                stringBuffer.append(lenOne.charAt(len1));
                len1--;
                len2--;
            } else {
                if (((len2 -1 >= 0) && (len1 -1 >=0) && tabular[len1][len2 - 1] > tabular[len1 - 1][len2])) {
                    len2--;
                } else {
                    len1--;
                }
            }
        }
        return stringBuffer.reverse().toString();

    }

    private static class ArrRotator {
        public static void driverMethod() throws Exception {
            int[] arr = {1, 2, 3, 4, 5, 6, 7};//rotate by 2 clockwise -->
            int[] arr2 = {1, 2, 3, 4, 5, 6, 7};//rotate by 2 clockwise -->
            long start = System.nanoTime();
            ArrRotator.rotateClockWistWithNoSwap(arr, 6);
            long finish = System.nanoTime();
            long diff = finish - start;
            printMetrics(start, finish);
            start = System.nanoTime();
            ArrRotator.rotateClockwise(arr2, 6);
            finish = System.nanoTime();
            long diff2 = finish - start;
            printMetrics(start, finish);
            System.out.println("Ratio is : " + (diff2 * 100 / diff));
        }

        private static void printMetrics(long start, long finish) {
            System.out.println("Time used is :" + (finish - start));
        }

        /**
         * Given an unsorted array arr[] of size N, rotate it by D elements (clockwise).
         */
        public static void rotateClockwise(int[] arr, int rotateBy) throws Exception {
            if (arr == null || arr.length == 0) {
                throw new Exception("There is nothing to rotate");
            }
            printArr(arr);
            //1,2,3 ,4,5,6,7 rotate by 2 clockwise -->
            for (int i = 0; i < rotateBy; i++) {
                for (int j = 0; j + 1 < arr.length; j++) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
            printArr(arr);
        }

        public static void rotateClockWistWithNoSwap(int[] arr, int rotateBy) {
            int start = rotateBy;
            int[] newArr = new int[arr.length];
            printArr(arr);
            for (int j = 0; j < arr.length; j++) {
                newArr[j] = arr[start % arr.length];
                start++;
            }
            arr = newArr;
            printArr(arr);
        }

        private static void printArr(int[] arr) {
            for (int i : arr) {
                System.out.print(i + "-->");
            }
            System.out.println();
        }
    }
}
