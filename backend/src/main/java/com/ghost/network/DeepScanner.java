package com.ghost.network;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class DeepScanner {
    public static String extractVisibleText(File file) {
        StringBuilder result = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file)) {
            int b;
            while ((b = fis.read()) != -1) {
                if (b >= 32 && b <= 126) {
                    result.append((char) b);
                } else if (result.length() > 0 && result.charAt(result.length()-1) != ' ') {
                    result.append(" ");
                }
            }
        } catch (IOException e) {
            return "Error";
        }
        String text = result.toString().trim();
        return text.length() > 100 ? text.substring(0, 100) + "..." : text;
    }
}