package Hot100.Backtrack;

/**
 * Given an m x n grid of characters board and a string word, return true if word exists in the grid.
 * <p>
 * The word can be constructed from letters of sequentially adjacent cells, where adjacent cells are horizontally or vertically neighboring.
 * The same letter cell may not be used more than once.
 */
public class WordSearch {
    int[][] dirs = new int[][]{{1, 0}, {0, 1}, {0, -1}, {-1, 0}};

    public boolean exist(char[][] board, String word) {
        int m = board.length, n = board[0].length;
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == word.charAt(0)) {
                    if (backtrack(board, visited, word, i, j, 1)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean backtrack(char[][] board, boolean[][] visited, String word, int i, int j, int n) {
        if (n == word.length()) {
            return true;
        }

        int h = board.length, w = board[0].length;

        visited[i][j] = true;

        for (int[] dir : dirs) {
            int x = i + dir[0];
            int y = j + dir[1];
            if (x >= 0 && x < h && y >= 0 && y < w && !visited[x][y] && board[x][y] == word.charAt(n)) {
                if (backtrack(board, visited, word, x, y, n + 1)) {
                    return true;
                }
            }
        }

        visited[i][j] = false;

        return false;
    }
}
