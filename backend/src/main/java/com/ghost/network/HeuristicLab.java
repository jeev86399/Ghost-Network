package com.ghost.network;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;


public class HeuristicLab {
    public static String identifyByContent(File file) {
        try (FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.READ)) {
            // Read a larger chunk (1KB) to analyze patterns
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer);
            byte[] data = buffer.array();

            int codeChars = 0; // { } ; ( )
            int plainChars = 0; // a-z A-Z 0-9

            for (byte b : data) {
                char c = (char) b;
                if (c == '{' || c == '}' || c == ';' || c == '(' || c == ')') codeChars++;
                if (Character.isLetterOrDigit(c) || Character.isWhitespace(c)) plainChars++;
            }

            // Heuristic Logic: If many curly braces/semicolons, it's likely code
            if (codeChars > 5) return "SOURCE_CODE (C++/Java/JS)";
            if (plainChars > 500) return "PLAIN_TEXT/DOCUMENT";
            
            return "ENCRYPTED/BINARY_BLOB";
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }
}