package com.ghost.network;
import java.io.File;


public class ExtensionChecker {
    public static boolean isMismatched(File file, String detectedType) {
        String fileName = file.getName().toUpperCase();
        
        // If we don't know the type, we can't say it's a mismatch yet
        if (detectedType.equals("UNKNOWN")) return false;

        // Check if the filename ends with the detected type
        // e.g., if detected is "JPG", filename should end with ".JPG" or ".JPEG"
        return !fileName.endsWith("." + detectedType) && !fileName.endsWith(".JPEG");
    }
}