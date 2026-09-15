class Solution:
    def largestRectangleArea(self, heights: list[int]) -> int:
        heights.append(0)  # add a sentinel to pop all remaining bars in the end
        stack = [-1]       # add a sentinel to avoid empty stack checks
        max_area = 0

        for i in range(len(heights)):
            # keep stack monotonic increasing
            while len(stack) > 1 and heights[i] < heights[stack[-1]]:
                h = heights[stack.pop()]
                w = i - stack[-1] - 1
                max_area = max(max_area, h * w)
            stack.append(i)

        return max_area