package Hot100.Skills;

/**
 * Given an array of integers nums containing n + 1 integers where each integer is in the range [1, n] inclusive.
 * <p>
 * There is only one repeated number in nums, return this repeated number.
 * <p>
 * You must solve the problem without modifying the array nums and using only constant extra space.
 */
public class DuplicateNumber {
    public int findDuplicate(int[] nums) {
        // regard the array as a ring
        // slow and fast will meet at a certain point.
        int slow = 0, fast = 0;

        do {
            slow = nums[slow];
            fast = nums[nums[fast]];
        } while (slow != fast);

        // put slow to start point, the next meet point is the entrance of the ring i.e., the duplicate number.
        slow = 0;
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }

        return slow;
    }
}
