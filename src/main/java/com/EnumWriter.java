package com;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

public class EnumWriter {
    private static List<String> costCenterList = new LinkedList<>();
    private static Map<String, String> fileToWorker = new HashMap<>();
    //AMZ_PU("PU Curse LLC"),
    private static String PREFIX = "AMZ";
    private static String MiddleVal = "";
    private static String leftBrace = "(";
    private static String rightBrance = ")";
    private static String SUFFIX = ",";
    private static String[] formatter = {
            "%s%s%s\"%s\"%s%s",
            "%s%s%s\"%s\"%s%s"
    };

    public static void main(String[] args) throws IOException {
        EnumWriter.readFileWithOutKeepingInMemory();
        //EnumWriter.prependUntilLengthSix("87867", 6);
    }

    private static String stringConcatinator(String prefix, String actualValue) {
        String value = PREFIX.toUpperCase() + "_" + prefix.toUpperCase();
        return String.format("%s%s\"%s\"%s%s%s%s%s", value, leftBrace, prefix,",\"", actualValue, "\"", rightBrance, SUFFIX);
    }

    private static Map<String, String> oldSupOrgs = new HashMap<>();
    private static HashMap<String, String> newHashValue = new HashMap<>();

    public static void hashFile() throws IOException {
        String fileLocation = System.getProperty("user.dir") + "\\src\\main\\resources\\EnumInputOutput\\comparefile";
        //readFileWithOutKeepingInMemory();
        //System.out.println(prependUntilLengthSix("570", 6));
        //readFileUsingApaceLib(fileLocation);
//        for(String value : oldSupOrgs.values()){
//            System.out.println(value);
//        }
    }

    public static String prependUntilLengthSix(String someValue, int length) {
        StringBuilder stringBuilder = new StringBuilder();
        String formatted = StringUtils.leftPad(someValue, length, '0');
        if(formatted.length() - 6 >= 0 ){
            stringBuilder.append(formatted);
            System.out.println(stringBuilder);
        }else{
//            stringBuilder.append(formatted);
//            System.out.println(stringBuilder);
        }
        return stringBuilder.toString();
    }

    public static String readFileUsingApaceLib(String fileName) {
        File f = new File(fileName);
        String line = null;
        try {
            LineIterator it = FileUtils.lineIterator(f, "UTF-8");
            while (it.hasNext()) {
                line = it.nextLine();
                if (!newHashValue.containsKey(line)) {
                    oldSupOrgs.put(line, line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    public static String readFileWithOutKeepingInMemory() throws IOException {
        String dirLocation = System.getProperty("user.dir") + "\\src\\main\\resources\\EnumInputOutput\\costCenter";
        String outputLocation = System.getProperty("user.dir") + "\\src\\main\\resources\\EnumInputOutput\\outputFile";
        System.out.println(dirLocation);
        FileInputStream inputStream = null;
       // FileOutputStream outputStream = null ;
        Scanner sc = null;
        inputStream = new FileInputStream(dirLocation);
       // FileWriter writer = new FileWriter(outputLocation, true);
        PrintStream fileStream = new PrintStream(new File(outputLocation));
        StringBuilder stringBuilder = new StringBuilder();

        try {
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                if (line != null) {
                    // System.out.println(line);
                    //  String found = splitAndReturValue(line) ;
                    // String splitOne = line.substring(0,4);
                    // String splitTwo = line.substring(4);
                    //sanitizeString(line);
                    //  String newValue = splits[0] + splits[1] ;
                    //String finalStr = stringConcatinator(line, line);
                    String[] founds = line.split(" ") ;
                    // System.out.format("%s ===== %s", founds[0], founds[1]);
                    String result = stringConcatinator(founds[0], line) ;
                    System.out.println(result);
                    // newHashValue.put(line, line);
//                    String output = prependUntilLengthSix(line, 6);
//                    fileStream.println(output);

                }
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } catch (IOException e) {
            ;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
        return "Not Found Dear";
    }

    private static String splitAndReturValue(String line) {
        String[] values = line.split("@");
        return stringConcatinator(values[0], values[1]);
    }

    private static boolean isAlphaNumericSpace(char ch) {
        return (ch >= '0' && ch <= '9') ||
                (ch >= 'a' && ch <= 'z') ||
                (ch >= 'A' && ch <= 'Z');
    }

    private static String createLocaations(String line) {
        String[] trims = line.split("#");
        String LOC_CODE = trims[0];
        String val = trims[1];
        // System.out.println(trims[1]);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < trims[1].length(); i++) {
            if (val.charAt(i) != '(') {
                stringBuilder.append(val.charAt(i));
                continue;
            }
            break;
        }
        //lets build the name
        String names = stringBuilder.toString().trim().replace("-", " ").replace(" ", "_").toUpperCase()
                .replace("__", "_");
        //System.out.println(names.replace("__", "_"));
        return names + "@" + LOC_CODE;
    }

    private static String sanitizeString(String line) {
        StringBuilder sb = new StringBuilder();
        char[] theLine = line.toCharArray();
        for (int i = 0; i < theLine.length; i++) {
            if (!isAlphaNumericSpace(theLine[i]) || theLine[i] == '\t' || theLine[i] == ' ') {
                {
                    sb.append("_");
                }
            } else {
                sb.append(theLine[i]);
            }
        }
        String cleanStr = sb.toString();
        cleanStr = cleanStr.replaceFirst("__", "_");
        // cleanStr = cleanStr.replaceFirst("_", "");
        cleanStr = cleanStr.replaceAll("___", "_");
        cleanStr = cleanStr.replaceAll("__", "_");

        return cleanStr;
    }

    private static String sanitizeStringTwo(String line) {
        String[] charsToRemove = {".", ",", "-", "'", "(", ")"};
        StringBuilder sb = new StringBuilder();
        // String[] words = line.split(" ") ;
        String s;
        for (String ch : charsToRemove) {
            if (line.contains(ch)) {
                s = line.replace(ch, "_").trim();
                // s = line.trim();
                if (ch != "_") {
                    sb.append(s).append("_");
                }
            }
        }
        s = line.replace(" ", "_");
        sb.append(s).append("_");

        return sb.toString();
    }

}