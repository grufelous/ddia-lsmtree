package com.grufelous.ddia.lsmtree.memtable;

import java.util.NavigableMap;

public interface MemTable {
    public void put(String key, String value);
    public String get(String key);
    public void clear();
    public int size();
    public NavigableMap<String, String> getSnapshot();
}
