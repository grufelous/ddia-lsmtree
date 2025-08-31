package com.grufelous.ddia.lsmtree.memtable;

import java.util.Collections;
import java.util.NavigableMap;
import java.util.TreeMap;

public class MemTableImpl implements  MemTable {
    private TreeMap<String, String> tree;

    public MemTableImpl() {
        this.tree = new TreeMap<String, String>();
    }
    
    public void put(String key, String value) {
        tree.put(key, value);
    }

    public String get(String key) {
        return tree.get(key);
    }

    public void clear() {
        tree.clear();
    }

    public int size() {
        return tree.size();
    }

    public NavigableMap<String, String> getSnapshot() {
        return Collections.unmodifiableNavigableMap(new TreeMap<>(tree));
    }
}
