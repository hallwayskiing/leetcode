package Hot100.LinkedList;

public class ListNode {
    int val;
    public ListNode next;

    ListNode(){}

    public ListNode(int x) {
        val = x;
        next = null;
    }

    public ListNode(int x, ListNode next){
        val=x;
        this.next=next;
    }
}
