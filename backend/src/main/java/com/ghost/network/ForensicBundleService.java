package com.ghost.network;

import java.io.*;
import java.nio.file.Files; // FIXED: Missing import for Files
import java.util.List;      // FIXED: Missing import for List
import java.util.zip.*;

public class ForensicBundleService {
    
    public static void createBundle(List<File> evidenceFiles, File report) throws IOException {
        File zipFile = new File("GHOST_EVIDENCE_BUNDLE.zip");
        
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            // 1. Add the Forensic Report to the bundle
            if (report != null && report.exists()) {
                addToZip(report, zos);
            }
            
            // 2. Add all Reconstructed evidence files
            for (File file : evidenceFiles) {
                if (file != null && file.exists()) {
                    addToZip(file, zos);
                }
            }
        }
    }

    private static void addToZip(File file, ZipOutputStream zos) throws IOException {
        // ZipEntry creates the "file inside the zip"
        zos.putNextEntry(new ZipEntry(file.getName()));
        
        // Files.copy streams the data from disk into the zip stream
        Files.copy(file.toPath(), zos);
        
        zos.closeEntry();
    }
}