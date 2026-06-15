package com.ghost.network;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class ReportGenerator {
    public static void generateCSV(String outputPath, List<String[]> data) {
        try (FileWriter writer = new FileWriter(outputPath)) {
            // Write CSV Header
            writer.append("File Name,Detected Type,Status,Deep Scan Preview\n");

            for (String[] row : data) {
                writer.append(String.join(",", row)).append("\n");
            }
            System.out.println("[+] Report saved successfully to: " + outputPath);
        } catch (IOException e) {
            System.out.println("[-] Error generating report: " + e.getMessage());
        }
    }
}