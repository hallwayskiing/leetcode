package Optiver;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class LogSystem {

    public static class Log {
        long id;
        long timestamp;
    }

    private final TreeMap<Long, Log> logs = new TreeMap<>();

    public void add(Log log){
        logs.put(log.id, log);
    }

    public List<Log> query(long start, long end){
        return new ArrayList<>(logs.subMap(start, end).values());
    }
}
