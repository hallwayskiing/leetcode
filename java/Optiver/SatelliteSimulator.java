package Optiver;

import java.util.*;

/**
 * Forwarding latency:
 * It takes exactly 10 seconds for a satellite to forward a message to one direct neighbor.
 * A sender can forward to only one neighbor at a time (sequential), each attempt consuming 10 seconds.
 * Forwarding to neighbors is synchronous and atomic per sender.
 * //
 * Forwarding order:
 * Each satellite forwards to all of its direct neighbors it has not already notified (from its own perspective),
 * strictly in increasing SatelliteId order.
 * //
 * Single effective notify:
 * Once a receiver is notified (by any source), it is considered notified; further attempts do not change its state.
 * However, concurrent or later attempts to the same receiver still consume 10 seconds of the sender’s time.
 * //
 * No immediate back-edge:
 * A satellite never forwards to the specific neighbor that most recently notified it (its effective notifier).
 * For initial satellites at t=0, there is no prior notifier, so nothing is excluded.
 * //
 * Processing and reporting delay:
 * After a satellite completes all of its own forwarding attempts
 * (per the ordered list it performs, with the above back-edge exclusion),
 * it waits 30 seconds, then reports back to Earth.
 * //
 * Tiebreaking:
 * Reporting: If two satellites finish processing at the same time, the smaller SatelliteId reports first.
 * Simultaneous notify arrivals: If multiple notify attempts arrive to the same satellite at the exact same time,
 * the attempt from the smaller sender SatelliteId is deemed the effective notifier.
 */
public class SatelliteSimulator {

    public static class Report {
        int satelliteId;
        int reportTimeSeconds;

        public Report(int satelliteId, int reportTimeSeconds) {
            this.satelliteId = satelliteId;
            this.reportTimeSeconds = reportTimeSeconds;
        }

        public int getSatelliteId() {
            return satelliteId;
        }

        public int getReportTimeSeconds() {
            return reportTimeSeconds;
        }
    }

    private static class Event {
        int time;
        int senderId;
        int receiverId;

        public Event(int time, int senderId, int receiverId) {
            this.time = time;
            this.senderId = senderId;
            this.receiverId = receiverId;
        }
    }

    /**
     * graph[u] = all direct neighbors of u, kept in ascending order
     */
    private final Map<Integer, TreeSet<Integer>> satelliteMap = new HashMap<>();

    /**
     * Add the satellite node to the network if not already present.
     */
    public void satelliteConnected(int satelliteId) {
        satelliteMap.computeIfAbsent(satelliteId, k -> new TreeSet<>());
    }

    /**
     * Add an undirected connection between the two satellites.
     * Maintain neighbors in strictly increasing SatelliteId order.
     */
    public void onRelationshipEstablished(int satelliteId1, int satelliteId2) {
        if (!satelliteMap.containsKey(satelliteId1) || !satelliteMap.containsKey(satelliteId2)) {
            return;
        }

        satelliteMap.get(satelliteId1).add(satelliteId2);
        satelliteMap.get(satelliteId2).add(satelliteId1);
    }

    /**
     * Treat the given satellites as simultaneously notified at time t=0.
     * Return all reports ordered by report time ascending, then satelliteId ascending.
     */
    public List<Report> onMessageReceived(List<Integer> satelliteIds) {
        List<Report> reports = new ArrayList<>();
        if (satelliteIds == null) return reports;

        // 1. Event Queue
        PriorityQueue<Event> eventQueue = new PriorityQueue<>((a, b) -> {
            if (a.time != b.time) return Integer.compare(a.time, b.time);
            return Integer.compare(a.senderId, b.senderId);
        });

        // 2. Initialization (t=0)
        for (int id : satelliteIds) {
            if (satelliteMap.containsKey(id)) {
                eventQueue.offer(new Event(0, -1, id));
            }
        }

        Set<Integer> notified = new HashSet<>();

        while (!eventQueue.isEmpty()) {
            Event event = eventQueue.poll();

            // Single effective notify
            if (notified.contains(event.receiverId)) {
                continue;
            }
            notified.add(event.receiverId);

            int currentSatId = event.receiverId;
            int startTime = event.time;
            int notifier = event.senderId;

            TreeSet<Integer> neighbors = satelliteMap.get(currentSatId);
            int forwardCount = 0;

            for (int neighborId : neighbors) {
                // No immediate back-edge
                if (neighborId == notifier) {
                    continue;
                }
                forwardCount++;
                int arrivalTime = startTime + (forwardCount * 10);
                eventQueue.offer(new Event(arrivalTime, currentSatId, neighborId));
            }


            // Processing and reporting delay
            int reportTime = startTime + (forwardCount * 10) + 30;
            reports.add(new Report(currentSatId, reportTime));
        }

        // Final sort
        reports.sort(Comparator.comparingInt(Report::getReportTimeSeconds)
                .thenComparingInt(Report::getSatelliteId));

        return reports;
    }
}