package com.grufelous.ddia.lsmtree.sstable;

public interface Index {
    public void addPointer(String key, long pointer);
    public long getFloorPointer(String key);
}
