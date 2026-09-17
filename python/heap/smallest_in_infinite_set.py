# You have a set which contains all positive integers [1, 2, 3, 4, 5, ...].
# Implement the SmallestInfiniteSet class:
# SmallestInfiniteSet() Initializes the SmallestInfiniteSet object to contain all positive integers.
# int popSmallest() Removes and returns the smallest integer contained in the infinite set.
# void addBack(int num) Adds a positive integer num back into the infinite set, if it is not already in the infinite set.


import heapq


class SmallestInfiniteSet:
    def __init__(self):
        self.back_queue = []
        self.back_set = set()
        self.curr = 1

    def pop_smallest(self) -> int:
        if self.back_queue:
            val = heapq.heappop(self.back_queue)
            self.back_set.remove(val)
            return val

        res = self.curr
        self.curr += 1
        return res

    def add_back(self, num: int) -> None:
        if num >= self.curr or num in self.back_set:
            return

        heapq.heappush(self.back_queue, num)
        self.back_set.add(num)
