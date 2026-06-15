package com.ghost.network;
import java.util.HashMap;
import java.util.Map;


public class SignatureMatcher {
    private static final Map<String, String> signatures = new HashMap<>();

    // Initialize the "Magic Numbers" database
    static {
        signatures.put("FFD8FF", "JPG");
        signatures.put("89504E47", "PNG");
        signatures.put("25504446", "PDF");
        signatures.put("504B0304", "ZIP/DOCX");
        signatures.put("4D5A", "EXE");
        // Add this one - common for some web images
        signatures.put("0000001C66747970", "JPG/MOV"); 
        signatures.put("FFD8FFE0", "JPG");
        signatures.put("FFD8FFE1", "JPG");
    }

    public static String detectType(String hexHeader) {
        for (Map.Entry<String, String> entry : signatures.entrySet()) {
            // Check if the file's hex starts with a known signature
            if (hexHeader.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "UNKNOWN";
    }
}