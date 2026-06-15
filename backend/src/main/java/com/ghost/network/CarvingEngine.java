package com.ghost.network;

import java.util.*;

public class CarvingEngine {
    
    // Database of Footers (The "End" markers for Phase 2.0 Carving)
    private static final Map<String, String> footers = new HashMap<>();
    
    static {
        footers.put("JPG", "FFD9");
        footers.put("PNG", "49454E44AE426082");
        footers.put("PDF", "2525454F46"); // %%EOF
    }

    /**
     * Stitches the file from the header to the detected footer.
     * This removes "Ghost Data" hidden after the actual file end.
     */
    public static byte[] stitchFile(byte[] rawData, String type) {
        String footerHex = footers.get(type);
        
        // If we don't know the footer for this type, return original data
        if (footerHex == null || type.equals("UNKNOWN")) {
            return rawData;
        }

        byte[] footerBytes = hexToBytes(footerHex);
        int footerIndex = findSequence(rawData, footerBytes);
        
        if (footerIndex != -1) {
            // Calculate actual end point (index + length of the footer)
            int actualLength = footerIndex + footerBytes.length;
            System.out.println("[!] Carving match found. Trimming to " + actualLength + " bytes.");
            return Arrays.copyOfRange(rawData, 0, actualLength);
        }

        return rawData;
    }

    // Helper to find the byte sequence of a footer in the raw data
    private static int findSequence(byte[] data, byte[] sequence) {
        for (int i = 0; i < data.length - sequence.length; i++) {
            boolean match = true;
            for (int j = 0; j < sequence.length; j++) {
                if (data[i + j] != sequence[j]) {
                    match = false;
                    break;
                }
            }
            if (match) return i;
        }
        return -1;
    }

    // Converts the Hex string from our database into searchable bytes
    private static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}