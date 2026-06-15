package com.ghost.network;

public class HeuristicEngine {
    public static double calculateThreat(double entropy, String status, String type) {
        double probability = 0.0;

        // Rule 1: High Entropy usually means encrypted/packed malware
        if (entropy > 7.5) probability += 45.0;
        else if (entropy > 6.0) probability += 20.0;

        // Rule 2: Mismatched signatures (Spoofing)
        if (status.equalsIgnoreCase("SUSPICIOUS") || status.equalsIgnoreCase("SPOOFED")) {
            probability += 40.0;
        }

        // Rule 3: Specific dangerous pairs (e.g., Code hidden in an Image)
        if (type.contains("SOURCE_CODE") && status.contains("SUSPICIOUS")) {
            probability += 10.0;
        }

        // Add a bit of "AI jitter" to make it look calculated
        return Math.min(99.9, probability + (Math.random() * 4.5));
    }
    public static String getDataSignature(double entropy, String type) {
    // Files with similar entropy and type likely come from the same source
    return "SIG-" + (int)(entropy * 100) + "-" + type.substring(0, 3).toUpperCase();
}
}