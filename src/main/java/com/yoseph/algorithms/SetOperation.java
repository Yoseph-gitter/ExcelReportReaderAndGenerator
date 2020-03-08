package com.yoseph.algorithms;

import java.util.HashSet;
import java.util.Map;

public class SetOperation {
    public static void main(String[] args){
      String subset =  SetOperation.getUnionOf("geek", "ekedgrt");
      System.out.println(subset);
    }

    /**
     * Shortest Common Supersequence
     * Given two strings str1 and str2, find the shortest string that has both str1 and str2
     * EX : Input:   str1 = "geek",  str2 = "eke"
     *      Output: "geeke"
     *
     */
    public static String getUnionOf(String first , String second){
        StringBuilder stringBuilder = new StringBuilder();
        String shortest = first.length() > second.length() ? second : first ;
        String longest = second.length() > first.length() ? second : first ;

        HashSet<Character> set = new HashSet<>();
        for(int index=0; index < shortest.length() ; index++){
            set.add(shortest.charAt(index));
            stringBuilder.append(shortest.charAt(index));
        }
        for(int j=0; j < longest.length() ; j++){
            if(!set.contains(longest.charAt(j))){
               stringBuilder.append(longest.charAt(j));
            }
        }
        return stringBuilder.toString();
    }
    public static String getShortestCommonSuerSequence(String first, String second){
        return "";
    }
    /**
     * String S is called a supersequence of a string T if S can be obtained from T by inserting zero or more symbol
     */

    public static String findSuperSequenceOf(String S, String T){
        // find S/T
        //find S intersection T
        // find T/SnT
        //concatinate S/T + T/SnT
      //  Map<Character> SwithOutT =
        return "";

    }
}
