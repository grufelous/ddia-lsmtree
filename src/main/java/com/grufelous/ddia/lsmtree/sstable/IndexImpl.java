package com.grufelous.ddia.lsmtree.sstable;

import java.util.Map;
import java.util.TreeMap;

public class IndexImpl implements Index {
    public TreeMap<String, Long> keyToPointer;

    public IndexImpl() {
        keyToPointer = new TreeMap<String, Long>();
    }

    public void addPointer(String key, Long pointer) {
        keyToPointer.put(key, pointer);
    }

    public Long getFloorPointer(String key) {
        Map.Entry<String, Long> value = keyToPointer.floorEntry(key);
        if(value != null) {
            return value.getValue();
        }
        return null;
    }
}
