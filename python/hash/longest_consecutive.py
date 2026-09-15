class Solution:
    def longestConsecutive(self, nums: list[int]) -> int:
        num_set = set(nums)
        longest = 0

        for num in num_set:
            if num - 1 not in num_set:  # Check if it's the start of a sequence
                curr = num
                length = 1

                while curr + 1 in num_set:
                    curr += 1
                    length += 1

                longest = max(longest, length)

        return longest