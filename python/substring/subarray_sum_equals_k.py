# Given an array of integers nums and an integer k, return the total number of subarrays whose sum equals to k.
# A subarray is a contiguous non-empty sequence of elements within an array.

class Solution:
    def subarraySum(self, nums: list[int], k: int) -> int:
        prefix_sum = 0
        freq_map = {0: 1}  # Initialize with prefix sum 0 having one occurrence
        count = 0

        for num in nums:
            prefix_sum += num
            # Check if there is a prefix sum that when subtracted from the current prefix sum equals k
            count += freq_map.get(prefix_sum - k, 0)
            # Update the frequency map with the current prefix sum
            freq_map[prefix_sum] = freq_map.get(prefix_sum, 0) + 1

        return count
       