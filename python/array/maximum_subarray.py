# Given an integer array nums, find the subarray with the largest sum, and return its sum.
class Solution:
    def maxSubArray(self, nums: list[int]) -> int:
        n = len(nums)

        dp = [0] * n
        dp[0] = nums[0]
        res = dp[0]

        for i in range(1, n):
            dp[i] = max(dp[i - 1] + nums[i], nums[i])
            res = max(res, dp[i])

        return res