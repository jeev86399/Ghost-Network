package com.ghost.network;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Scanner {
    /**
     * Recursively scans a directory for files, skipping common build/dependency folders
     * to ensure the scan remains fast and relevant.
     */
    public static List<File> getAllFiles(File directory) {
        List<File> fileList = new ArrayList<>();
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                // SKIP NOISY FOLDERS: node_modules (JS), .git (Version Control), target (Java Build)
                if (file.isDirectory()) {
                    String folderName = file.getName();
                    if (folderName.equals("node_modules") || 
                        folderName.equals(".git") || 
                        folderName.equals("target")) {
                        continue; // Jump to the next file/folder in the loop
                    }
                    
                    // If it's a normal folder, go deeper (Recursion)
                    fileList.addAll(getAllFiles(file));
                } else {
                    // If it's a file, add it to our list
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }
}