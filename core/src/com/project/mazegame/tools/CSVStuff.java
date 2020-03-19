package com.project.mazegame.tools;// package com.project.mazegame.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CSVStuff {

    public static ArrayList<String> readCSVFile() {
        String fileRead = "android/csvFile.csv";
        BufferedReader buff = null;
        String line = "";
        ArrayList<String> output = new ArrayList<>();
        String split = ",";
        try {
            buff = new BufferedReader(new FileReader(fileRead));
            while((line = buff.readLine()) != null) {
                String[] outputData = line.split(split);
                for (int i = 0; i < outputData.length; i++) {
                    output.add(outputData[i]);
                }
            }
            return output;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeCSV(ArrayList<String> list) {
        try {

            String outPutCSV = "/Users/kevin/mazeHunt/android/csvFile.csv ";
            FileWriter writer = new FileWriter(outPutCSV);
            for (int i = 0; i < list.size(); i++) {
                writer.append(list.get(i));
                writer.append(",");
                writer.append("\n");

            }
            System.out.print("Written successfully");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
