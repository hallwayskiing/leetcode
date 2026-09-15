package Hot100.Substring;

import java.util.HashMap;
import java.util.Map;

/**
 * Given an array of integers nums and an integer k, return the total number of subarrays whose sum equals to k.
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 */
public class SubarraySumEqualsK {
    public int subarraySum(int[] nums, int k) {
        //current sum
        int preSum = 0;
        //record times of preSum
        Map<Integer, Integer> freq = new HashMap<>();
        freq.put(0, 1);
        int cnt = 0;

        for (int num : nums) {
            preSum += num;
            cnt += freq.getOrDefault(preSum - k, 0);
            freq.put(preSum, freq.getOrDefault(preSum, 0) + 1);
        }

        return cnt;
    }
}
