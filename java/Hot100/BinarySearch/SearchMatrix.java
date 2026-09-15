package Hot100.BinarySearch;

/**
 * You are given an m x n integer matrix matrix with the following two properties:
 * <p>
 * Each row is sorted in non-decreasing order.
 * The first integer of each row is greater than the last integer of the previous row.
 * Given an integer target, return true if target is in matrix or false otherwise.
 * <p>
 * You must write a solution in O(log(m * n)) time complexity.
 */
public class SearchMatrix {
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        int row = searchRow(matrix, target, 0, m - 1);
        if (row < 0 || row >= m) {
            return false;
        }
        return searchCol(matrix, row, target, 0, n - 1);
    }

    public int searchRow(int[][] matrix, int target, int begin, int end) {
        if (begin > end) {
            return matrix[begin][0] <= target ? begin : begin - 1;
        }

        int mid = begin + (end - begin) / 2;
        if (matrix[mid][0] == target) {
            return mid;
        }
        else if (matrix[mid][0] > target) {
            return searchRow(matrix, target, begin, mid - 1);
        }
        else {
            return searchRow(matrix, target, mid + 1, end);
        }
    }

    public boolean searchCol(int[][] matrix, int row, int target, int begin, int end) {
        if (begin > end) {
            return false;
        }

        int mid = begin + (end - begin) / 2;
        if (matrix[row][mid] == target) {
            return true;
        }
        else if (matrix[row][mid] > target) {
            return searchCol(matrix, row, target, begin, mid - 1);
        }
        else {
            return searchCol(matrix, row, target, mid + 1, end);
        }
    }

    public boolean searchMatrixII(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        int left = 0, right = m * n - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int value = matrix[mid / n][mid % n];

            if (value == target) return true;
            else if (value < target) left = mid + 1;
            else right = mid - 1;
        }
        return false;
    }

}
