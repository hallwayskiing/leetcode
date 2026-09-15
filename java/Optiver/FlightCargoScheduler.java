package Optiver;

import java.util.*;

public class FlightCargoScheduler {
    public record Flight(
            String flightId,
            Long departTime,
            Long arriveTime,
            double maxWeight,
            double cost
    ) {
    }

    public record Cargo(
            String cargoId,
            double weight,
            Long latestArriveTime,
            double revenue
    ) {
    }

    /**
     * Original code to be optimized
     */
    public void processBookings(List<Flight> flights, List<Cargo> cargoList) {
        List<Cargo> remainingCargos = new ArrayList<>(cargoList);
        for (Flight flight : flights) {
            double totalRevenue = 0;
            double totalWeight = 0;
            List<Cargo> manifest = new ArrayList<>();

            List<Cargo> eligibleCargos = remainingCargos.stream()
                    .filter(c -> c.latestArriveTime >= flight.arriveTime)
                    .toList();

            for (Cargo cargo : eligibleCargos) {
                if (cargo.weight + totalWeight <= flight.maxWeight) {
                    totalRevenue += cargo.revenue;
                    totalWeight += cargo.weight;
                    manifest.add(cargo);
                }
            }

            remainingCargos.removeAll(manifest);

            System.out.println("Flight:" + flight.flightId);
            System.out.println("Manifest:" + manifest);
            System.out.println("Profit:" + (totalRevenue - flight.cost));
        }
    }

    /**
     * Optimize by cancelling non-profit flights and sorting by value per weight
     */
    public void processBookingsOptimized(List<Flight> flights, List<Cargo> cargoList) {
        cargoList.sort(Comparator.comparingDouble((Cargo c) -> c.revenue / c.weight).reversed());

        flights.sort(Comparator.comparingLong(f -> f.departTime));

        Set<Cargo> booked = new HashSet<>();

        for (Flight flight : flights) {
            double totalRevenue = 0;
            double totalWeight = 0;
            List<Cargo> manifest = new ArrayList<>();

            List<Cargo> eligibleCargos = cargoList.stream()
                    .filter(c -> !booked.contains(c) && c.latestArriveTime >= flight.arriveTime)
                    .toList();

            for (Cargo cargo : eligibleCargos) {
                if (cargo.weight + totalWeight <= flight.maxWeight) {
                    totalRevenue += cargo.revenue;
                    totalWeight += cargo.weight;
                    manifest.add(cargo);
                }
            }


            if (totalRevenue - flight.cost > 0) {
                booked.addAll(manifest);
                System.out.println("Flight confirmed:" + flight.flightId);
                System.out.println("Manifest:" + manifest);
                System.out.println("Profit:" + (totalRevenue - flight.cost));
            } else {
                System.out.println("Flight cancelled:" + flight.flightId);
            }
        }
    }

    /**
     * Optimize by DP
     */
    public void processBookingsWithDP(List<Flight> flights, List<Cargo> cargoList) {
        flights.sort(Comparator.comparingLong(f -> f.departTime));

        Set<Cargo> booked = new HashSet<>();

        for (Flight flight : flights) {
            List<Cargo> eligibleCargos = cargoList.stream()
                    .filter(c -> !booked.contains(c) && c.latestArriveTime >= flight.arriveTime)
                    .toList();

            int maxWeight = (int) (flight.maxWeight * 100);
            double[] dp = new double[maxWeight + 1];
            boolean[][] keep = new boolean[eligibleCargos.size()][maxWeight + 1];

            for (int i = 0; i < eligibleCargos.size(); i++) {
                Cargo cargo = eligibleCargos.get(i);
                int w = (int) (cargo.weight * 100);
                for (int j = maxWeight; j >= w; j--) {
                    if (dp[j - w] + cargo.revenue > dp[j]) {
                        dp[j] = dp[j - w] + cargo.revenue;
                        keep[i][j] = true;
                    }
                }
            }

            double maxRevenue = dp[maxWeight];

            if (maxRevenue > flight.cost) {
                List<Cargo> manifest = new ArrayList<>();
                int tempW = maxWeight;
                for (int i = eligibleCargos.size() - 1; i >= 0; i--) {
                    if (keep[i][tempW]) {
                        Cargo c = eligibleCargos.get(i);
                        booked.add(c);
                        manifest.add(c);
                        tempW -= (int) (c.weight * 100);
                    }
                }

                System.out.println("Flight confirmed: " + flight.flightId);
                System.out.println("Profit: " + (maxRevenue - flight.cost));
                System.out.println("Manifest: " + manifest);
            } else {
                System.out.println("Flight cancelled: " + flight.flightId);
            }

        }
    }

    /**
     * Streaming processing
     */
    private final List<Flight> flights = new ArrayList<>();
    private final List<Cargo> cargos = new ArrayList<>();
    private double totalProfit;

    public void onFlight(Flight flight) {
        flights.add(flight);
    }

    public void onCargo(Cargo cargo) {
        cargos.add(cargo);
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void decide() {
        flights.sort(Comparator.comparingLong(f -> f.departTime));

        Set<Cargo> booked = new HashSet<>();

        for (Flight flight : flights) {
            List<Cargo> eligibleCargos = cargos.stream()
                    .filter(c -> !booked.contains(c) && c.latestArriveTime >= flight.arriveTime)
                    .toList();

            int maxWeight = (int) (flight.maxWeight * 100);
            double[] dp = new double[maxWeight + 1];
            boolean[][] keep = new boolean[eligibleCargos.size()][maxWeight + 1];

            for (int i = 0; i < eligibleCargos.size(); i++) {
                Cargo cargo = eligibleCargos.get(i);
                int w = (int) (cargo.weight * 100);
                for (int j = maxWeight; j >= w; j--) {
                    if (dp[j - w] + cargo.revenue > dp[j]) {
                        dp[j] = dp[j - w] + cargo.revenue;
                        keep[i][j] = true;
                    }
                }
            }

            double maxRevenue = dp[maxWeight];

            if (maxRevenue > flight.cost) {
                if (bookFlight(flight.flightId)) {
                    int tempW = maxWeight;
                    for (int i = eligibleCargos.size() - 1; i >= 0; i--) {
                        if (keep[i][tempW]) {
                            Cargo c = eligibleCargos.get(i);
                            booked.add(c);
                            tempW -= (int) (c.weight * 100);
                        }
                    }
                    totalProfit += maxRevenue - flight.cost;
                    System.out.println("Flight confirmed:" + flight.flightId);
                } else {
                    System.out.println("Flight booking failed" + flight.flightId);
                }
            } else {
                System.out.println("Flight cancelled:" + flight.flightId);
            }
        }
    }

    public boolean bookFlight(String flightId) {
        return true;
    }
}
