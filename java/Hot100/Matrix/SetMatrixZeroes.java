package Hot100.Matrix;

import java.util.HashSet;
import java.util.Set;

/**
 * Given an m x n integer matrix matrix, if an element is 0, set its entire row and column to 0's.
 *
 * You must do it in place.
 */
public class SetMatrixZeroes {
    public void setZeroes(int[][] matrix) {
        int row= matrix.length;
        int col=matrix[0].length;
        Set<Integer> rowSet=new HashSet<>();
        Set<Integer> colSet=new HashSet<>();

        //record zeroes first, not to cover original zeroes
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int num=matrix[i][j];
                if(num==0){
                    rowSet.add(i);
                    colSet.add(j);
                }
            }
        }

        for (Integer i:rowSet) {
            for (int j = 0; j < col; j++) {
                matrix[i][j]=0;
            }
        }

        for (Integer i:colSet) {
            for (int j = 0; j < row; j++) {
                matrix[j][i]=0;
            }
        }
    }
}
