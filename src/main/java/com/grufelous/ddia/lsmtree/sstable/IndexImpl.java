package com.grufelous.ddia.lsmtree.sstable;

import java.util.TreeMap;

public class IndexImpl implements Index {
    public TreeMap<String, Long> keyToPointer;

    public void addPointer(String key, long pointer) {
        keyToPointer.put(key, pointer);
    }

    public long getFloorPointer(String key) {
        return keyToPointer.floorEntry(key).getValue();
    }
}
