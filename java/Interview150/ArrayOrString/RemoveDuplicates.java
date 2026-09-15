package Interview150.ArrayOrString;

/**
 * Given an integer array nums sorted in non-decreasing order, remove the duplicates in-place such that each unique element appears only once.
 * The relative order of the elements should be kept the same. Then return the number of unique elements in nums.
 * <p>
 * Consider the number of unique elements of nums to be k, to get accepted, you need to do the following things:
 * <p>
 * Change the array nums such that the first k elements of nums contain the unique elements in the order they were present in nums initially.
 * The remaining elements of nums are not important as well as the size of nums.
 * Return k.
 */
public class RemoveDuplicates {
    public int removeDuplicates(int[] nums) {
        if(nums==null || nums.length==0) return 0;

        int curr=1;
        for (int i = 1; i < nums.length; i++) {
            if(nums[i]==nums[curr-1]){
                nums[curr]=nums[i];
                curr++;
            }
        }
        return curr;
    }
}
