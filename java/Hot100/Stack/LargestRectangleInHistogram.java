package Hot100.Stack;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Given an array of integers heights representing the histogram's bar height where the width of each bar is 1,
 * return the area of the largest rectangle in the histogram
 */
public class LargestRectangleInHistogram {
    public int largestRectangleArea(int[] heights) {
        int n = heights.length;
        int max = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(-1); // sentinel

        for (int i = 0; i < n; i++) {
            while (stack.peek() != -1 && heights[i] < heights[stack.peek()]) {
                int h = heights[stack.pop()];
                int w = i - stack.peek() - 1; // width
                max = Math.max(max, h * w);
            }
            stack.push(i);
        }

        // process the rest
        while (stack.peek() != -1) {
            int h = heights[stack.pop()];
            int w = n - stack.peek() - 1;
            max = Math.max(max, h * w);
        }
        return max;
    }
}
