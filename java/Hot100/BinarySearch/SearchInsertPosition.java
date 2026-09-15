package Hot100.BinarySearch;

/**
 * Given a sorted array of distinct integers and a target value, return the index if the target is found. If not, return the index where it would be if it were inserted in order.
 * <p>
 * You must write an algorithm with O(log n) runtime complexity.
 */
public class SearchInsertPosition {
    public int searchInsert(int[] nums, int target){
        int begin=0,end=nums.length-1;
        while (begin<=end){
            int mid = begin + (end - begin) / 2;
            if(nums[mid]==target) return mid;

            if(nums[mid]>target){
                end=mid-1;
            }
            else {
                begin=mid+1;
            }
        }
        return begin;
    }
}
