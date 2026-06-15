package com.ghost.network;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class EntropyAnalyzer {

    /**
     * Phase 1.0 Logic: Calculates Shannon Entropy (0.0 to 8.0)
     * High entropy (>7.5) indicates encryption or compressed media.
     */
    public static double calculateEntropy(File file) {
        try {
            byte[] fileData = Files.readAllBytes(file.toPath());
            if (fileData.length == 0) return 0.0;

            int[] frequencies = new int[256];
            for (byte b : fileData) {
                frequencies[b & 0xFF]++;
            }

            double entropy = 0;
            double log2 = Math.log(2);
            for (int f : frequencies) {
                if (f > 0) {
                    double probability = (double) f / fileData.length;
                    entropy -= probability * (Math.log(probability) / log2);
                }
            }
            return entropy; 
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * Phase 2.0 Feature: Byte DNA Profiling (BFD)
     * Generates a statistical fingerprint of the file data.
     */
    public static Map<Integer, Double> getByteDNA(File file) {
        Map<Integer, Double> dnaProfile = new HashMap<>();
        try {
            byte[] fileData = Files.readAllBytes(file.toPath());
            if (fileData.length == 0) return dnaProfile;

            int[] counts = new int[256];
            for (byte b : fileData) {
                counts[b & 0xFF]++;
            }

            // Normalize counts into percentages (0.0 to 1.0)
            for (int i = 0; i < 256; i++) {
                if (counts[i] > 0) {
                    dnaProfile.put(i, (double) counts[i] / fileData.length);
                } else {
                    dnaProfile.put(i, 0.0);
                }
            }
        } catch (Exception e) {
            System.err.println("DNA Profiling Failed: " + e.getMessage());
        }
        return dnaProfile;
    }
}