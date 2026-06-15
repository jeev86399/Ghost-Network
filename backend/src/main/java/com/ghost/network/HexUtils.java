package com.ghost.network;

import java.io.File;
import java.nio.file.Files;
import java.security.MessageDigest;

public class HexUtils {

    /**
     * Phase 1.0 Logic: Converts raw bytes to a readable Hex string.
     * Essential for viewing headers and signatures.
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    /**
     * Phase 3.0 Logic: Generates a SHA-256 Digital Fingerprint.
     * This ensures the integrity of the forensic evidence.
     */
    public static String generateHash(File file) {
        try {
            // SHA-256 is the industry standard for forensic integrity
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Read all bytes and generate the unique hash
            byte[] encodedhash = digest.digest(Files.readAllBytes(file.toPath()));
            
            // Convert to Hex and return a shortened version for the UI table
            String fullHash = bytesToHex(encodedhash);
            return fullHash.substring(0, 16); 
        } catch (Exception e) {
            System.err.println("Hashing Error: " + e.getMessage());
            return "HASH_ERROR";
        }
    }
    public static double calculateThreatProbability(double entropy, String status) {
    double score = 0.0;

    // Weight 1: Entropy (Compressed/Encrypted files are higher risk)
    if (entropy > 7.5) score += 40.0;
    else if (entropy > 5.0) score += 20.0;

    // Weight 2: Status from Signature Matcher
    if (status.equals("SPOOFED") || status.equals("SUSPICIOUS")) score += 50.0;
    
    // Cap at 99%
    return Math.min(score + (Math.random() * 9), 99.9);
}
}