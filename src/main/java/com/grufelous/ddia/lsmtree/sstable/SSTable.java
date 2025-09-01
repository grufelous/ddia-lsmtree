package com.grufelous.ddia.lsmtree.sstable;

import java.util.NavigableMap;

public interface SSTable {
    public String get(String key);
    public void create(NavigableMap<String, String> data);
    public void initializeTableFromFile();
}
