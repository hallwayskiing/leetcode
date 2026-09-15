package Hot100.Graph;

import java.util.*;

/**
 * There are n cities numbered from 0 to n - 1 and n - 1 roads,
 * such that there is only one way to travel between two different cities (this network form a tree).
 * Last year, The ministry of transport decided to orient the roads in one direction because they are too narrow.
 * <p>
 * Roads are represented by connections where connections[i] = [ai, bi] represents a road from city ai to city bi.
 * <p>
 * This year, there will be a big event in the capital (city 0), and many people want to travel to this city.
 * <p>
 * Your task consists of reorienting some roads such that each city can visit the city 0.
 * Return the minimum number of edges changed.
 * <p>
 * It's guaranteed that each city can reach city 0 after reorder.
 */
public class ReorderRoutesToMakeAllPathsLeadToTheCityZero {
    public int minReorder(int n, int[][] connections) {
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }

        for (int[] conn : connections) {
            adj.get(conn[0]).add(new int[]{conn[1], 1});
            adj.get(conn[1]).add(new int[]{conn[0], 0});
        }

        int count = 0;
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new ArrayDeque<>();

        queue.offer(0);
        visited[0] = true;

        while (!queue.isEmpty()) {
            int curr = queue.poll();
            for (int[] neighbor : adj.get(curr)) {
                int nextCity = neighbor[0];
                int cost = neighbor[1];

                if (!visited[nextCity]) {
                    visited[nextCity] = true;
                    count += cost;
                    queue.offer(nextCity);
                }
            }
        }

        return count;
    }
}
