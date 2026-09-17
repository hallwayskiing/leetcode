package Hot100.Heap;

import java.util.PriorityQueue;

class MedianFinder {
    private PriorityQueue<Integer> maxHeap; // 维护较小的一半元素（大顶堆）
    private PriorityQueue<Integer> minHeap; // 维护较大的一半元素（小顶堆）

    public MedianFinder() {
        // 大顶堆：自定义 Comparator 实现降序
        maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
        // 小顶堆：默认升序
        minHeap = new PriorityQueue<>();
    }

    public void addNum(int num) {
        // 1. 先入大顶堆，将大顶堆的最大值筛选并压入小顶堆
        maxHeap.offer(num);
        minHeap.offer(maxHeap.poll());

        // 2. 平衡数量：确保 maxHeap 的元素数量不少于 minHeap
        if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    public double findMedian() {
        if (maxHeap.size() > minHeap.size()) {
            return maxHeap.peek();
        }
        return (maxHeap.peek() + minHeap.peek()) / 2.0;
    }
}
