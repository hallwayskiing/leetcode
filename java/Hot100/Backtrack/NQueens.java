package Hot100.Backtrack;

import java.util.*;

public class NQueens {
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> res = new ArrayList<>();
        int[] queens = new int[n]; // each represents a row
        Arrays.fill(queens, -1);
        boolean[] columns = new boolean[n];
        boolean[] diagonals1 = new boolean[n * 2]; // row - col + n
        boolean[] diagonals2 = new boolean[n * 2]; // row + col
        backtrack(n, 0, queens, columns, diagonals1, diagonals2, res);
        return res;
    }

    public void backtrack(int n, int row, int[] queens, boolean[] columns, boolean[] diagonals1, boolean[] diagonals2, List<List<String>> res) {
        if (row == n) {
            res.add(generateBoard(queens));
            return;
        }

        // Examine each column
        for (int col = 0; col < n; col++) {
            int diagonal1 = row - col + n;
            int diagonal2 = row + col;
            if (columns[col] || diagonals1[diagonal1] || diagonals2[diagonal2]) {
                continue;
            }

            queens[row] = col;
            columns[col] = diagonals1[diagonal1] = diagonals2[diagonal2] = true;
            backtrack(n, row + 1, queens, columns, diagonals1, diagonals2, res);
            queens[row] = -1;
            columns[col] = diagonals1[diagonal1] = diagonals2[diagonal2] = false;
        }
    }

    public List<String> generateBoard(int[] queens) {
        List<String> board = new ArrayList<>();
        int n = queens.length;

        for (int queen : queens) {
            char[] row = new char[n];
            Arrays.fill(row, '.');
            row[queen] = 'Q';
            board.add(new String(row));
        }
        return board;
    }
}
