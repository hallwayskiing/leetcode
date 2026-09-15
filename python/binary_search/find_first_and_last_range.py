# Given an array of integers nums sorted in non-decreasing order, find the starting and ending position of a given target value.
# If target is not found in the array, return [-1, -1].
# You must write an algorithm with O(log n) runtime complexity.

class Solution:
    def searchRange(self, nums: list[int], target: int) -> list[int]:
        if not nums:
            return [-1, -1]

        left = self.find_left(nums, target)
        if left == -1:
            return [-1, -1]

        right = self.find_right(nums, target)

        return [left, right]

    def find_left(self, nums: list[int], target: int) -> int:
        left, right = 0, len(nums) - 1
        while left <= right:
            mid = (left + right) // 2
            if nums[mid] >= target:
                right = mid - 1
            else:
                left = mid + 1

        # left stops at the first position where nums[left] >= target or len(nums) if not found
        if left < len(nums) and nums[left] == target:
            return left
        return -1

    def find_right(self, nums: list[int], target: int) -> int:
        left, right = 0, len(nums) - 1
        while left <= right:
            mid = (left + right) // 2
            if nums[mid] <= target:
                left = mid + 1
            else:
                right = mid - 1

        # right stops at the last position where nums[right] <= target or -1 if not found
        if right >= 0 and nums[right] == target:
            return right
        return -1