# You are given an array of integers nums, there is a sliding window of size k which is moving from the very left of the array to the very right.
# You can only see the k numbers in the window. Each time the sliding window moves right by one position.
#
# Return the max sliding window.

class Solution:
    def maxSlidingWindow(self, nums: list[int], k: int) -> list[int]:
        from collections import deque

        if not nums:
            return []

        result = []
        window = deque()

        for i, num in enumerate(nums):
            # remove numbers that are out of the current window
            if window[0] <= i - k:
                window.popleft()

            # remove smaller numbers from the back of the deque
            while window and nums[window[-1]] <= num:
                window.pop()
            window.append(i)

            # append the current max to the result list
            if i >= k - 1:
                result.append(nums[window[0]])

        return result