package Hot100.Substring;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * You are given an array of integers nums, there is a sliding window of size k which is moving from the very left of the array to the very right.
 * You can only see the k numbers in the window. Each time the sliding window moves right by one position.
 *
 * Return the max sliding window.
 */
public class SlidingWindowMaximum {
    public int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        int[] ans = new int[n - k + 1];
        Deque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            // remove out-of-window element
            if (deque.getFirst() <= i - k) {
                deque.removeFirst();
            }

            // remove smaller numbers to maintain monotone
            while (!deque.isEmpty() && nums[deque.getLast()] <= nums[i]) {
                deque.removeLast();
            }
            deque.addLast(i);

            // record
            if (i >= k - 1) {
                ans[i - k + 1] = nums[deque.getFirst()];
            }
        }
        return ans;
    }
}
