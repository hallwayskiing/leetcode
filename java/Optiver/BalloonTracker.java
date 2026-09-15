package Optiver;

import java.util.*;

public class BalloonTracker {
    private static final double ALT_MAX = Math.pow(2, 15);
    private static final double WIND_MAX = Math.pow(2, 5);
    private static final double THRESH = 15.0;
    private static final double RECOVER = 300.0;
    private static final double EPS = 1e-9;

    private Set<String> teamSet;
    private Map<Double, Double> anchors;
    private int windVersion;
    private Map<Double, CacheEntry> windCache;
    private Map<String, State> balloons;
    private Map<Double, Set<String>> altToBalloons;
    private PriorityQueue<RecoveryEvent> recoveryHeap;
    private PriorityQueue<CompEvent> compHeap;
    private double lastTs;

    static class State {
        boolean isTeam;
        boolean airborne;
        Double altitude;
        boolean stable;
        Double safeStart;
        int recVer;
        int compVer;

        public State(boolean isTeam) {
            this.isTeam = isTeam;
            this.airborne = false;
            this.altitude = null;
            this.stable = true;
            this.safeStart = null;
            this.recVer = 0;
            this.compVer = 0;
        }
    }

    static class CacheEntry {
        int version;
        double value;

        public CacheEntry(int version, double value) {
            this.version = version;
            this.value = value;
        }
    }

    static class RecoveryEvent implements Comparable<RecoveryEvent> {
        double dueTime;
        String name;
        int ver;

        public RecoveryEvent(double dueTime, String name, int ver) {
            this.dueTime = dueTime;
            this.name = name;
            this.ver = ver;
        }

        @Override
        public int compareTo(RecoveryEvent other) {
            return Double.compare(this.dueTime, other.dueTime);
        }
    }

    static class CompEvent implements Comparable<CompEvent> {
        double altitude;
        String name;
        int ver;

        public CompEvent(double altitude, String name, int ver) {
            this.altitude = altitude;
            this.name = name;
            this.ver = ver;
        }

        @Override
        public int compareTo(CompEvent other) {
            // 我们需要一个最大堆，所以按高度降序排列
            return Double.compare(other.altitude, this.altitude);
        }
    }

    private double windAt(double h) {
        CacheEntry cached = windCache.get(h);
        if (cached != null && cached.version == windVersion) {
            return cached.value;
        }
        double total = 0.0;
        for (Map.Entry<Double, Double> entry : anchors.entrySet()) {
            double A = entry.getKey();
            double s = entry.getValue();
            double d = (h - A) / 100.0;
            total += s / (1.0 + d * d);
        }
        windCache.put(h, new CacheEntry(windVersion, total));
        return total;
    }

    private void compInvalidateAndMaybePush(String name, State st) {
        if (st.isTeam) {
            return;
        }
        st.compVer++;
        if (st.airborne && st.stable && st.altitude != null) {
            compHeap.offer(new CompEvent(st.altitude, name, st.compVer));
        }
    }

    private Double getCompMaxAltitude() {
        while (!compHeap.isEmpty()) {
            CompEvent ev = compHeap.peek();
            State st = balloons.get(ev.name);
            if (st == null || st.isTeam || !st.airborne || !st.stable
                    || st.compVer != ev.ver || Math.abs(st.altitude - ev.altitude) > EPS) {
                compHeap.poll(); // 懒惰删除失效的堆节点
                continue;
            }
            return ev.altitude;
        }
        return null;
    }

    private void startSafePeriodIfNeeded(String name, State st, double now) {
        if (st.stable) return;
        if (st.safeStart == null) {
            st.safeStart = now;
            st.recVer++;
            recoveryHeap.offer(new RecoveryEvent(now + RECOVER, name, st.recVer));
        }
    }

    private void clearSafePeriod(State st) {
        st.safeStart = null;
    }

    private void advanceTime(double now) {
        while (!recoveryHeap.isEmpty() && recoveryHeap.peek().dueTime <= now + EPS) {
            RecoveryEvent ev = recoveryHeap.poll();
            State st = balloons.get(ev.name);
            if (st == null || !st.airborne || st.stable) continue;
            if (st.safeStart == null || st.recVer != ev.ver) continue;

            // 在当前时间点仍然安全才能恢复稳定性
            if (windAt(st.altitude) <= THRESH + EPS) {
                st.stable = true;
                st.safeStart = null;
                compInvalidateAndMaybePush(ev.name, st);
            } else {
                st.safeStart = null;
            }
        }
    }

    private boolean processTimestamp(double ts) {
        if (ts < lastTs - EPS) {
            return false;
        }
        advanceTime(ts);
        lastTs = ts;
        return true;
    }

    private boolean validAlt(Double a) {
        return a != null && a > 0 && a < ALT_MAX;
    }

    private boolean validWind(Double w) {
        return w != null && w >= 0 && w < WIND_MAX;
    }

    /**
     * 主处理函数
     * @param yourBalloonNames 自己团队的气球名称列表
     * @param operations 操作序列，每个元素为一个 Object 数组，表示操作的参数
     * 例如: new Object[]{"BalloonAscended", 0.0, "A", 1000.0}
     * @return 返回对应每次操作输出的 List (Boolean 或 List<String>)
     */
    public List<Object> solution(List<String> yourBalloonNames, List<Object[]> operations) {
        teamSet = new HashSet<>(yourBalloonNames);
        anchors = new HashMap<>();
        windVersion = 0;
        windCache = new HashMap<>();
        balloons = new HashMap<>();
        altToBalloons = new HashMap<>();
        recoveryHeap = new PriorityQueue<>();
        compHeap = new PriorityQueue<>();
        lastTs = Double.NEGATIVE_INFINITY;

        List<Object> out = new ArrayList<>();

        for (Object[] op : operations) {
            String kind = (String) op[0];

            if ("BalloonAscended".equals(kind)) {
                double ts = (Double) op[1];
                String name = (String) op[2];
                double alt = (Double) op[3];

                if (!processTimestamp(ts) || !validAlt(alt)) {
                    out.add(false);
                    continue;
                }

                State st = balloons.computeIfAbsent(name, k -> new State(teamSet.contains(name)));

                if (st.airborne && Math.abs(st.altitude - alt) <= EPS) {
                    out.add(false);
                    continue;
                }

                // 若改变高度，先从旧的高度桶中移除
                if (st.airborne) {
                    Set<String> s = altToBalloons.get(st.altitude);
                    if (s != null) {
                        s.remove(name);
                        if (s.isEmpty()) {
                            altToBalloons.remove(st.altitude);
                        }
                    }
                }

                st.airborne = true;
                st.altitude = alt;
                st.stable = true;
                st.safeStart = null;
                st.recVer++;

                altToBalloons.computeIfAbsent(alt, k -> new HashSet<>()).add(name);

                // 立即评估风速
                if (windAt(alt) > THRESH + EPS) {
                    st.stable = false;
                    st.safeStart = null;
                }
                compInvalidateAndMaybePush(name, st);

                out.add(true);

            } else if ("BalloonDescended".equals(kind)) {
                double ts = (Double) op[1];
                String name = (String) op[2];

                if (!processTimestamp(ts)) {
                    out.add(false);
                    continue;
                }

                State st = balloons.get(name);
                if (st == null || !st.airborne) {
                    out.add(false);
                    continue;
                }

                Set<String> s = altToBalloons.get(st.altitude);
                if (s != null) {
                    s.remove(name);
                    if (s.isEmpty()) {
                        altToBalloons.remove(st.altitude);
                    }
                }

                st.airborne = false;
                st.altitude = null;
                st.stable = true;
                st.safeStart = null;
                st.recVer++;
                compInvalidateAndMaybePush(name, st);

                out.add(true);

            } else if ("SetWindSpeed".equals(kind)) {
                double ts = (Double) op[1];
                double alt = (Double) op[2];
                double w = (Double) op[3];

                if (!processTimestamp(ts) || !validAlt(alt) || !validWind(w)) {
                    out.add(false);
                    continue;
                }

                anchors.put(alt, w);
                windVersion++;
                windCache.clear();

                // 遍历所有有气球的高度，重新评估安全性
                for (Map.Entry<Double, Set<String>> entry : altToBalloons.entrySet()) {
                    double a = entry.getKey();
                    Set<String> names = entry.getValue();
                    double ws = windAt(a);
                    boolean safe = ws <= THRESH + EPS;

                    for (String nm : names) {
                        State st = balloons.get(nm);
                        if (st.stable) {
                            if (!safe) {
                                st.stable = false;
                                clearSafePeriod(st);
                                compInvalidateAndMaybePush(nm, st);
                            }
                        } else {
                            if (safe) {
                                startSafePeriodIfNeeded(nm, st, ts);
                            } else {
                                clearSafePeriod(st);
                            }
                        }
                    }
                }

                out.add(true);

            } else if ("InspectBalloons".equals(kind)) {
                double ts = (Double) op[1];

                if (!processTimestamp(ts)) {
                    out.add(new ArrayList<String>());
                    continue;
                }

                Double compMax = getCompMaxAltitude();
                if (compMax == null) {
                    out.add(new ArrayList<String>());
                    continue;
                }

                List<String> res = new ArrayList<>();
                for (String nm : yourBalloonNames) {
                    State st = balloons.get(nm);
                    if (st != null && st.airborne && st.stable && st.altitude >= compMax - EPS) {
                        res.add(nm);
                    }
                }
                Collections.sort(res);
                out.add(res);

            } else {
                throw new IllegalArgumentException("Unknown operation type: " + kind);
            }
        }

        return out;
    }
}
