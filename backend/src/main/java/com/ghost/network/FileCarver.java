package com.ghost.network;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.UUID;

public class FileCarver {
    /**
     * Surgically extracts the raw data and saves it as a new reconstructed file.
     */
    public static String reconstruct(File source, String targetType) {
        try {
            byte[] rawData = Files.readAllBytes(source.toPath());
            String extension = targetType.contains("SOURCE") ? ".java" : ".txt";
            
            String outputName = "RECONSTRUCTED_" + UUID.randomUUID().toString().substring(0, 8) + extension;
            File outputFile = new File(source.getParent(), outputName);

            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(rawData);
            }
            return outputFile.getName();
        } catch (Exception e) {
            return "CARVE_FAILED";
        }
    }
}