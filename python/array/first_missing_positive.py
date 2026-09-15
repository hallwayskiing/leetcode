# Given an unsorted integer array nums. Return the smallest positive integer that is not present in nums.
# You must implement an algorithm that runs in O(n) time and uses O(1) auxiliary space.
class Solution:
    def firstMissingPositive(self, nums: list[int]) -> int:
        n = len(nums)

        # Place each value x in its correct index x-1 if 1 <= x <= n.
        for i in range(n):
            while 1 <= nums[i] <= n and nums[nums[i] - 1] != nums[i]:
                target = nums[i] - 1
                nums[i], nums[target] = nums[target], nums[i]

        # The first index i where nums[i] != i + 1 is the answer.
        for i in range(n):
            if nums[i] != i + 1:
                return i + 1

        return n + 1
