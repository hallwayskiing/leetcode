class Solution:
    def findKthLargest(self, nums: list[int], k: int) -> int:
        import heapq

        min_heap = []
        for num in nums:
            heapq.heappush(min_heap, num)
            if len(min_heap) > k:
                heapq.heappop(min_heap)

        return min_heap[0]

    def findKthLargestII(self, nums: list[int], k: int) -> int:
        return self.quick_select(nums, 0, len(nums) - 1, len(nums) - k)

    def quick_select(self, nums: list[int], left: int, right: int, k: int) -> int:
        if left == right:
            return nums[left]

        pivot_index = self.partition(nums, left, right)   

        if k == pivot_index:
            return nums[k]
        elif k < pivot_index:
            return self.quick_select(nums, left, pivot_index - 1, k)
        else:
            return self.quick_select(nums, pivot_index + 1, right, k)

    def partition(self, nums: list[int], left: int, right: int) -> int:
        pivot = nums[left]
        i = left + 1
        j = right

        while True:
            while i <= j and nums[i] < pivot:
                i += 1
            while i <= j and nums[j] > pivot:
                j -= 1

            if i >= j:
                break

            nums[i], nums[j] = nums[j], nums[i]
            i += 1
            j -= 1

        nums[left], nums[j] = nums[j], nums[left]       
        return j
        