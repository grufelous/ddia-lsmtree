package com.grufelous.ddia.lsmtree.services;

import com.grufelous.ddia.lsmtree.constants.Thresholds;
import com.grufelous.ddia.lsmtree.memtable.MemTable;
import com.grufelous.ddia.lsmtree.sstable.SSTableRepository;

public class LSMService {
    private MemTable memtable;
    private SSTableRepository ssTableRepository;

    public void put(String key, String entry) {
        memtable.put(key, entry);
        if(memtable.size() > Thresholds.MEM_TABLE_SIZE_THRESHOLD) {
            this.flushMemTable();
        }
    }

    public String get(String key) {
        String value = memtable.get(key);
        if(value == null) {
            value = ssTableRepository.getValue(key);
        }
        return value;
    }

    private void flushMemTable() {
        ssTableRepository.addSSTable(memtable.getSnapshot());
        memtable.clear();
    }
}
