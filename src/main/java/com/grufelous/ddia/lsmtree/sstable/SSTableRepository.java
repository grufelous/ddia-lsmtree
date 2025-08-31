package com.grufelous.ddia.lsmtree.sstable;

import com.grufelous.ddia.lsmtree.constants.Files;

import java.util.List;
import java.util.NavigableMap;

public class SSTableRepository {
    private List<SSTable> ssTables;
    private int fileCount;

    public String getValue(String key) {
        for(int i = ssTables.size() - 1; i >= 0; i--) {
            String curValue = ssTables.get(i).get(key);
            if(curValue != null) {
                return curValue;
            }
        }
        return null;
    }

    public void addSSTable(NavigableMap<String, String> memtableData) {
        SSTable table = new SSTableImpl();
        table.create(getNextFileName(), memtableData);
        ssTables.add(table);
    }

    private String getNextFileName() {
        return Files.SSTABLE_FILE_PREFIX + fileCount + Files.SSTABLE_FILE_SUFFIX;
    }
}
