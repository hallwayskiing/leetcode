package Hot100.Hash;

import java.util.HashSet;
import java.util.Set;

/**
 * Given an unsorted array of integers nums, return the length of the longest consecutive elements sequence.
 *
 * You must write an algorithm that runs in O(n) time.
 */
public class LongestConsecutive {
    public int longestConsecutive(int[] nums) {
        Set<Integer> nums_set=new HashSet<>();
        for (int num:nums)
            nums_set.add(num);

        int length=0;
        for (int num:nums_set){
            if(!nums_set.contains(num-1)){
                int currentNum=num;
                int currentLength=1;

                while(nums_set.contains(++currentNum)){
                    currentLength++;
                }
                length=Math.max(length,currentLength);
            }
        }
        return length;
    }
}
