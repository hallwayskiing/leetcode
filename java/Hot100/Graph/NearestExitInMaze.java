package Hot100.Graph;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * You are given an m x n matrix maze (0-indexed) with empty cells (represented as '.') and walls (represented as '+').
 * You are also given the entrance of the maze,
 * where entrance = [entrance_row, entrance_col] denotes the row and column of the cell you are initially standing at.
 * <p>
 * In one step, you can move one cell up, down, left, or right.
 * You cannot step into a cell with a wall, and you cannot step outside the maze.
 * Your goal is to find the nearest exit from the entrance.
 * An exit is defined as an empty cell that is at the border of the maze.
 * The entrance does not count as an exit.
 * <p>
 * Return the number of steps in the shortest path from the entrance to the nearest exit, or -1 if no such path exists.
 */
public class NearestExitInMaze {
    public int nearestExit(char[][] maze, int[] entrance) {
        int m = maze.length;
        int n = maze[0].length;

        Queue<int[]> queue = new ArrayDeque<>();

        queue.offer(entrance);
        maze[entrance[0]][entrance[1]] = '+';

        int[][] dirs = new int[][]{{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

        int steps = 0;
        while (!queue.isEmpty()) {
            steps++;
            int size = queue.size();
            while (size-- > 0) {
                int[] pos = queue.poll();
                int x = pos[0];
                int y = pos[1];
                for (int[] dir : dirs) {
                    int nextX = x + dir[0];
                    int nextY = y + dir[1];
                    if (nextX < 0 || nextX >= m || nextY < 0 || nextY >= n || maze[nextX][nextY] == '+') {
                        continue;
                    }
                    if (nextX == 0 || nextX == m - 1 || nextY == 0 || nextY == n - 1) {
                        return steps;
                    }
                    queue.offer(new int[]{nextX, nextY});
                    maze[nextX][nextY] = '+';
                }
            }
        }

        return -1;
    }
}
