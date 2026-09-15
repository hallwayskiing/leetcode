package Optiver;

import java.util.HashMap;
import java.util.Map;

/**
 * An underground railway system is keeping track of customer travel times between different stations.
 * They are using this data to calculate the average time it takes to travel from one station to another.
 * <p>
 * Implement the UndergroundSystem class:
 * <p>
 * void checkIn(int id, string stationName, int t)
 * A customer with a card ID equal to id, checks in at the station stationName at time t.
 * A customer can only be checked into one place at a time.
 * <p>
 * void checkOut(int id, string stationName, int t)
 * A customer with a card ID equal to id, checks out from the station stationName at time t.
 * <p>
 * double getAverageTime(string startStation, string endStation)
 * Returns the average time it takes to travel from startStation to endStation.
 * The average time is computed from all the previous traveling times from startStation to endStation that happened directly,
 * meaning a check in at startStation followed by a check out from endStation.
 * The time it takes to travel from startStation to endStation may be different from the time it takes to travel from endStation to startStation.
 * There will be at least one customer that has traveled from startStation to endStation before getAverageTime is called.
 * <p>
 * You may assume all calls to the checkIn and checkOut methods are consistent.
 * If a customer checks in at time t1 then checks out at time t2, then t1 < t2.
 * All events happen in chronological order.
 */
public class UndergroundSystem {
    static class CheckIn {
        String stationName;
        int t;

        public CheckIn(String stationName, int t) {
            this.stationName = stationName;
            this.t = t;
        }
    }

    static class Record {
        int totalTime;
        int n;

        public Record() {
        }

        public void insert(int time) {
            totalTime += time;
            n++;
        }

        public double getAverage() {
            return (double) totalTime / n;
        }
    }

    private final Map<Integer, CheckIn> checkIns;
    private final Map<String, Record> records;

    public UndergroundSystem() {
        checkIns = new HashMap<>();
        records = new HashMap<>();
    }

    public void checkIn(int id, String stationName, int t) {
        if (checkIns.containsKey(id)) {
            return;
        }

        checkIns.put(id, new CheckIn(stationName, t));
    }

    public void checkOut(int id, String stationName, int t) {
        CheckIn checkIn = checkIns.remove(id);
        if (checkIn == null) {
            return;
        }

        records.computeIfAbsent(checkIn.stationName + "_" + stationName, k -> new Record()).insert(t - checkIn.t);
    }

    public double getAverageTime(String startStation, String endStation) {
        return records.get(startStation + "_" + endStation).getAverage();
    }
}
