package com.project.mazegame.tools;// package com.project.mazegame.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
/**
 * <h1>CSV Stuff</h1>
 * Handles writing and reading from CSV
 * @Author James Bartlett
 */

public class CSVStuff {
    /**
     * Method for reading in CSV from a local file on computer
     * @return Returns arraylist of data from the csv file
     * @throws IOException
     * @throws FileNotFoundException
     */

    public static ArrayList<String> readCSVFile(String filename) {
//        String fileRead ="android\\"+ filename + ".csv";
        String fileRead ="/Users/kevin/mazeHunt/android/"+filename+".csv";
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
    /**
     * Method for writing to csv from array list
     * @param list
     * @throws IOException
     */
    public static void writeCSV(ArrayList<String> list, String filename) {
        try {
            String outPutCSV = "android\\"+ filename + ".csv";
            FileWriter writer = new FileWriter(outPutCSV);
            for (int i = 0; i < list.size(); i++) {
                writer.append(list.get(i));
                writer.append(",");
                writer.append("\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}