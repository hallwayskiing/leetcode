package Hot100.Heap;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

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
        if(!backQueue.isEmpty()){
            int num = backQueue.poll();
            backSet.remove(num);
            return num;
        }
        return curr++;
    }

    public void addBack(int num) {
        if(num >= curr || backSet.contains(num)){
            return;
        }
        backQueue.offer(num);
        backSet.add(num);
    }
}
