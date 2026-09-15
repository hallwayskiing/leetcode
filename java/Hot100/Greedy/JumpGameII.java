package Hot100.Greedy;

/**
 * You are given a 0-indexed array of integers nums of length n. You are initially positioned at index 0.
 * <p>
 * Each element nums[i] represents the maximum length of a forward jump from index i.
 * In other words, if you are at index i, you can jump to any index (i + j) where:
 * <p>
 * 0 <= j <= nums[i] and
 * i + j < n
 * Return the minimum number of jumps to reach index n - 1.
 * The test cases are generated such that you can reach index n - 1.
 */
public class JumpGameII {
    public int jump(int[] nums) {
        int farthest = 0;
        int bound = 0;
        int steps = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);
            if (i == bound) {
                bound = farthest;
                steps++;
            }
        }
        return steps;
    }
}
