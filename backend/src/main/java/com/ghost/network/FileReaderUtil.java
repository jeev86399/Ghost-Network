package com.ghost.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileReaderUtil {
    public static String getFileHeader(File file) {
        // Most file signatures are in the first 8-16 bytes
        byte[] header = new byte[16];
        
        try (FileInputStream fis = new FileInputStream(file)) {
            int bytesRead = fis.read(header);
            if (bytesRead == -1) return "EMPTY_FILE";
            
            return HexUtils.bytesToHex(header);
        } catch (IOException e) {
            return "ERROR_READING_FILE";
        }
    }
}