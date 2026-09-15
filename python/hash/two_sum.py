class Solution:
    def twoSum(self, nums: list[int], target: int) -> list[int]:
        map = {}

        for i in range(len(nums)):
            num = nums[i]
            if target - num in map:
                return [i, map[target-num]]
            map[num] = i

        return None