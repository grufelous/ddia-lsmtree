package com.grufelous.ddia.lsmtree.sstable;

import java.util.NavigableMap;

public interface SSTable {
    public String get(String key);
    public void create(String fileName, NavigableMap<String, String> data);
    public void initializeTableFromFile(String fileName);
}
