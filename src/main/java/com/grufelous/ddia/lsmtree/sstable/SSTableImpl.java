package com.grufelous.ddia.lsmtree.sstable;

import com.grufelous.ddia.lsmtree.constants.Thresholds;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.NavigableMap;

public class SSTableImpl implements SSTable {
    private Index index;
    private String fileName;

    public SSTableImpl(String fileName) {
        this.fileName = fileName;
        index = new IndexImpl();
    }

    public void create(NavigableMap<String, String> data) {
        try {
            RandomAccessFile file = new RandomAccessFile(fileName, "rw");
            int writtenIndex = 0;
            for(Map.Entry<String, String> entry : data.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                int keySize = key.length();
                int valueSize = value.length();
                long currentPointer = file.getFilePointer();
                file.writeInt(keySize);
                file.writeBytes(key);
                file.writeInt(valueSize);
                file.writeBytes(value);
                writtenIndex++;
                if(writtenIndex % Thresholds.INDEX_OFFSET == 0) {
                    index.addPointer(key, currentPointer);
                }
            }

            file.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Error opening sstable file");
            System.err.println(ex.toString());
        } catch (IOException ex) {
            System.err.println("Error reading pointer of sstable file");
            System.err.println(ex.toString());
        }
    }

    public void initializeTableFromFile() {
        try {
            RandomAccessFile file = new RandomAccessFile(fileName, "rw");
        } catch (FileNotFoundException ex) {
            System.err.println("Error opening sstable file");
            System.err.println(ex.toString());
        }
    }

    public String get(String key) {
        try (RandomAccessFile fileToRead = new RandomAccessFile(fileName, "r")) {
            fileToRead.seek(index.getFloorPointer(key));
            while(fileToRead.getFilePointer() < fileToRead.length()) {
                int keySize = fileToRead.readInt();
                byte[] keyBytes = new byte[keySize];
                fileToRead.readFully(keyBytes);
                String readKey = new String(keyBytes, StandardCharsets.UTF_8);
                int valueSize = fileToRead.readInt();
                byte[] valueBytes = new byte[valueSize];
                fileToRead.readFully(valueBytes);
                String value = new String(valueBytes, StandardCharsets.UTF_8);
                System.out.printf("%s:\t%s\n", key, value);
                if(readKey.equals(key)) {
                    return value;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error opening sstable file to read");
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println("Error while reading sstable file");
            System.err.println(e.toString());
        }
        return null;
    }

    public void readFile() {
        try (RandomAccessFile fileToRead = new RandomAccessFile(fileName, "r")) {
            fileToRead.seek(0);
            while(fileToRead.getFilePointer() < fileToRead.length()) {
                int keySize = fileToRead.readInt();
                byte[] keyBytes = new byte[keySize];
                fileToRead.readFully(keyBytes);
                String key = new String(keyBytes, StandardCharsets.UTF_8);
                int valueSize = fileToRead.readInt();
                byte[] valueBytes = new byte[valueSize];
                fileToRead.readFully(valueBytes);
                String value = new String(valueBytes, StandardCharsets.UTF_8);
                System.out.printf("%s:\t%s\n", key, value);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error opening sstable file to read");
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println("Error while reading sstable file");
            System.err.println(e.toString());
        }
    }
}
