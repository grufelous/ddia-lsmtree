package com.grufelous.ddia.lsmtree.sstable;

public interface Index {
    public void addPointer(String key, Long pointer);
    public Long getFloorPointer(String key);
}
