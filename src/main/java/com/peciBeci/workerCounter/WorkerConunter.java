package com.peciBeci.workerCounter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkerConunter {
    private static List<String> wordCountList = new LinkedList<>();
    private static Map<String, Integer> fileToWorker = new HashMap<>();
    private static List<String> filesThatDoesNotHaveWorker = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        WorkerConunter.listDirectories();
        // WorkerConunter.readFile();
        fileToWorker.forEach((key, value) -> {
            System.out.printf("\n %s \t %s \n", key, value);
        });
    }

    public static int readFileUsingApaceLib(String fileName) {
        File f = new File(fileName);
        String line = null;
        List<String> numberOfWorkers = new LinkedList<>();
        String matchedStr = "<Worker WID=";
        int occurance = 0;
        try {
            LineIterator it = FileUtils.lineIterator(f, "UTF-8");

            while (it.hasNext()) {
                line = it.nextLine();
                occurance += findWordUpgrade(line, matchedStr);
              //  System.out.print("\t" + occurance);
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return occurance;
    }

    public static String readFileWithOutKeepingInMemory(String fileName) throws IOException {
        FileInputStream inputStream = null;
        Scanner sc = null;
        inputStream = new FileInputStream(fileName);
        try {
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String found = findRegex(line);
                if (found != null) {
                    System.out.println(found);
                    return found;
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

    private static String findRegex(String line) {
        // Pattern pattern = Pattern.compile(":Worker_Count>(\\d*)");//</(peci:Worker_Count>)");
        String meatcher = "<Worker WID=";
        Pattern pattern = Pattern.compile(meatcher);
        String fouondLine = null;
        Matcher m = pattern.matcher(line);
        if (m.find()) {
            System.out.println("Found value: " + m.find());
            fouondLine = m.group(1);
        }
        return fouondLine;
    }

    public static int findWordUpgrade(String textString, String word) {
        List<Integer> indexes = new ArrayList<Integer>();
        StringBuilder output = new StringBuilder();
        String lowerCaseTextString = textString.toLowerCase();
        String lowerCaseWord = word.toLowerCase();
        int wordLength = 0;

        int index = 0;
        while (index != -1) {
            index = lowerCaseTextString.indexOf(lowerCaseWord, index + wordLength);  // Slight improvement
            if (index != -1) {
                indexes.add(index);
            }
            wordLength = word.length();
        }
        return indexes.size();
    }

    public static String readFile(String fileName) {
        String fouondLine = "";
        Pattern pattern = Pattern.compile("<Worker WID=");
        InputStreamReader streamReader;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Matcher m = pattern.matcher(line);
                if (m.find()) {
                    System.out.println("Found value: " + m.group());
                    fouondLine = m.group(1);
                    break;
                } else {
                    System.out.println("Not found ");
                    filesThatDoesNotHaveWorker.add(fileName);
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fouondLine;
    }

    public static void listDirectories() throws IOException {
        String dirLocation = "C:\\HallowinPerfRun\\03-03-2020\\INT024\\";
        //System.getProperty("user.dir") + "\\src\\main\\resources\\InputPeciBeci";
        System.out.println(dirLocation);
        File dir = new File(dirLocation);
        File[] files = new File[0];
        if (dir.isDirectory()) {
            files = dir.listFiles();
        }
        for (File f : files) {
            System.out.println(f.getName());
            String createdFileName = dirLocation + "\\" + f.getName();
            int number = readFileUsingApaceLib(createdFileName);
            //String found = readFile(createdFileName);
            //String found= readFileWithOutKeepingInMemory(createdFileName);
            fileToWorker.put(f.getName(), number);
        }
    }

}
