package Hot100.Greedy;

import java.util.Arrays;

public class MaximumElementAfterDecrementingAndRearranging {
    public int maximumElementAfterDecrementingAndRearranging(int[] arr) {
        Arrays.sort(arr);
        int target = 1;
        for (int num:arr){
            if(num >= target){
                target++;
            }
        }
        return target-1;
    }
}
