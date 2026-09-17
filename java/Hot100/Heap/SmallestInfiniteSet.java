package Hot100.Heap;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * You have a set which contains all positive integers [1, 2, 3, 4, 5, ...].
 * <p>
 * Implement the SmallestInfiniteSet class:
 * <p>
 * SmallestInfiniteSet() Initializes the SmallestInfiniteSet object to contain all positive integers.
 * int popSmallest() Removes and returns the smallest integer contained in the infinite set.
 * void addBack(int num) Adds a positive integer num back into the infinite set, if it is not already in the infinite set.
 */

public class SmallestInfiniteSet {
    private int curr;
    private final PriorityQueue<Integer> backQueue;
    private final Set<Integer> backSet;

    public SmallestInfiniteSet() {
        curr = 1;
        backQueue = new PriorityQueue<>();
        backSet = new HashSet<>();
    }

    public int popSmallest() {
        if (!backQueue.isEmpty()) {
            int num = backQueue.poll();
            backSet.remove(num);
            return num;
        }
        return curr++;
    }

    public void addBack(int num) {
        if (num >= curr || backSet.contains(num)) {
            return;
        }
        backQueue.offer(num);
        backSet.add(num);
    }
}
