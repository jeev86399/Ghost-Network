package com.ghost.network;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        // ... (Header text remains the same)
        
        String myPath = System.getProperty("user.home") + "/Desktop/GhostTest"; 
        File testFolder = new File(myPath);
        
        // This list must be synchronized for multi-threading!
        List<String[]> reportData = Collections.synchronizedList(new ArrayList<>());

        if (testFolder.exists() && testFolder.isDirectory()) {
            List<File> foundFiles = Scanner.getAllFiles(testFolder);
            System.out.println("[*] Files found for analysis: " + foundFiles.size());

            long startTime = System.currentTimeMillis();

            ParallelEngine engine = new ParallelEngine();
            // Passing BOTH arguments now fixes the red error
            engine.startParallelScan(foundFiles, reportData);

            long endTime = System.currentTimeMillis();
            
            System.out.println("----------------------------------------------");
            System.out.println("[+] Parallel Analysis Complete in: " + (endTime - startTime) + "ms");
            
            // Generate the CSV
            String reportPath = "output/advanced_forensic_report.csv";
            ReportGenerator.generateCSV(reportPath, new ArrayList<>(reportData));
            
        } else {
            System.out.println("ERROR: Path not found!");
        }
    }
}