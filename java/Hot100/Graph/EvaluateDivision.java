package Hot100.Graph;

import java.util.*;

/**
 * You are given an array of variable pairs equations and an array of real numbers values,
 * where equations[i] = [Ai, Bi] and values[i] represent the equation Ai / Bi = values[i].
 * Each Ai or Bi is a string that represents a single variable.
 * <p>
 * You are also given some queries,
 * where queries[j] = [Cj, Dj] represents the jth query where you must find the answer for Cj / Dj = ?.
 * <p>
 * Return the answers to all queries. If a single answer cannot be determined, return -1.0.
 * <p>
 * Note: The input is always valid.
 * You may assume that evaluating the queries will not result in division by zero and that there is no contradiction.
 * <p>
 * Note: The variables that do not occur in the list of equations are undefined,
 * so the answer cannot be determined for them.
 */
public class EvaluateDivision {
    /**
     * DFS
     */
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        Map<String, Map<String, Double>> graph = new HashMap<>();

        for (int i = 0; i < values.length; i++) {
            List<String> equation = equations.get(i);
            String first = equation.get(0);
            String second = equation.get(1);
            double value = values[i];

            graph.putIfAbsent(first, new HashMap<>());
            graph.putIfAbsent(second, new HashMap<>());

            graph.get(first).put(second, value);
            graph.get(second).put(first, 1.0 / value);
        }

        double[] res = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            List<String> query = queries.get(i);
            String first = query.get(0);
            String second = query.get(1);

            if (!graph.containsKey(first) || !graph.containsKey(second)) {
                res[i] = -1.0;
            } else if (first.equals(second)) {
                res[i] = 1.0;
            } else {
                res[i] = dfs(first, second, 1.0, graph, new HashSet<>());
            }
        }
        return res;
    }

    private double dfs(String current, String target, double product,
                       Map<String, Map<String, Double>> graph, Set<String> visited) {
        if (current.equals(target)) {
            return product;
        }

        visited.add(current);

        Map<String, Double> neighbors = graph.get(current);
        for (Map.Entry<String, Double> entry : neighbors.entrySet()) {
            String next = entry.getKey();
            double weight = entry.getValue();

            if (visited.contains(next)) {
                continue;
            }

            double result = dfs(next, target, product * weight, graph, visited);
            if (result != -1.0) {
                return result;
            }
        }

        return -1.0;
    }
}
