package com.ghost.network;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;


public class FastFileReader {
    public static String getHeaderFast(File file) {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r");
             FileChannel channel = raf.getChannel()) {
            
            // Map the file to memory - High performance NIO
            long size = Math.min(channel.size(), 16);
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
            
            byte[] header = new byte[(int) size];
            buffer.get(header);
            
            return HexUtils.bytesToHex(header);
        } catch (Exception e) {
            return "";
        }
    }
}