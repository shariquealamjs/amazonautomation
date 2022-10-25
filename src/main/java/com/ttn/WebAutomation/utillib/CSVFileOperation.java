package com.ttn.WebAutomation.utillib;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ttn.WebAutomation.base.BaseLib;
//import com.ttn.WebAutomation.base.BaseTest;


public class CSVFileOperation extends BaseLib {
    private static String downloadpath;

    public CSVFileOperation(String path) {
        this.downloadpath = path;
    }

    public static List<List<String>> getCSVData(String csvFileName) {
        List<List<String>> csvData = new ArrayList<>();
        int lineCount = 0;
        String line = "";

        try {
            if (!csvFileName.isEmpty()) {
                String filePath = downloadpath + System.getProperty("file.separator") + csvFileName;
                File file = new File(filePath);
                if (file.exists()) {
                    FileReader fr = new FileReader(file);
                    LineNumberReader lineNumberReader = new LineNumberReader(fr);
                    while ((line = lineNumberReader.readLine()) != null) {
                        csvData.add(new ArrayList<String>());
                        String[] lineData = line.split(",");
                        csvData.get(lineCount).addAll(Arrays.asList(lineData));
                        lineCount++;
                        
                    }
                    lineNumberReader.close();
                } else {
                    System.out.println("File does not exists");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvData;
    }

    public static void updateCSVData(String csvFileName, List<List<String>> csvData) {
        FileWriter fileWriter = null;
        try {
            if (!csvFileName.isEmpty()) {
                String filePath = downloadpath + System.getProperty("file.separator") + csvFileName;
                fileWriter = new FileWriter(filePath);
                for (List<String> csvLine : csvData) {
                    int colCount = csvLine.size();
                    for (int col = 0; col < colCount - 1; col++) {
                        fileWriter.append(csvLine.get(col));
                        fileWriter.append(",");
                    }
                    fileWriter.append(csvLine.get(colCount - 1));
                    fileWriter.append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (Exception e) {
                System.out.println("Error in saving the file!");
                e.printStackTrace();
            }
        }
    }
}
