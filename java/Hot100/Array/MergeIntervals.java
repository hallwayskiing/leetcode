package Hot100.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given an array of intervals where intervals[i] = [starti, endi],
 * merge all overlapping intervals, and return an array of the non-overlapping intervals that cover all the intervals in the input.
 */
public class MergeIntervals {
    public int[][] merge(int[][] intervals) {
        List<int[]>list=new ArrayList<>();
        Arrays.sort(intervals,(a,b)->a[0]-b[0]);
        int start=intervals[0][0],end=intervals[0][1];
        for(int i=1;i<intervals.length;i++){
            int i_start=intervals[i][0],i_end=intervals[i][1];
            if(i_start<=end){
                end=Math.max(end,i_end);
                continue;
            }
            list.add(new int[]{start,end});
            start=i_start;
            end=i_end;
        }
        list.add(new int[]{start,end});

        return list.toArray(new int[list.size()][]);
    }

    public static void main(String[] args) {
        int[][]intervals=new int[][]{{1,3},{2,6}};
        new MergeIntervals().merge(intervals);
    }
}
