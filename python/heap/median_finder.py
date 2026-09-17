import heapq


class MedianFinder:
    def __init__(self):
        # max_heap 维护较小的一半元素（存相反数以实现大顶堆）
        self.max_heap = []
        # min_heap 维护较大的一半元素（小顶堆）
        self.min_heap = []

    def add_num(self, num: int) -> None:
        # 1. 先压入 max_heap，再将 max_heap 中的最大值弹出并放入 min_heap
        #    利用 -num 实现大顶堆逻辑
        val = -heapq.heappushpop(self.max_heap, -num)
        heapq.heappush(self.min_heap, val)

        # 2. 保证 max_heap 的数量不少于 min_heap（允许 max_heap 比 min_heap 多 1 个）
        if len(self.min_heap) > len(self.max_heap):
            heapq.heappush(self.max_heap, -heapq.heappop(self.min_heap))

    def find_median(self) -> float:
        if len(self.max_heap) > len(self.min_heap):
            return float(-self.max_heap[0])
        return (-self.max_heap[0] + self.min_heap[0]) / 2.0