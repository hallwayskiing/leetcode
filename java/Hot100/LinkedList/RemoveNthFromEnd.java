package Hot100.LinkedList;

/**
 * Given the head of a linked list, remove the nth node from the end of the list and return its head.
 */
public class RemoveNthFromEnd {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy=new ListNode(0,head);
        ListNode fast=head,slow=dummy;
        //fast moves n steps first
        for (int i = 0; i < n; i++) {
            fast=fast.next;
        }
        //when fast reaches the end, slow reaches the len-n
        while (fast!=null){
            fast=fast.next;
            slow=slow.next;
        }
        slow.next=slow.next.next;

        return dummy.next;
    }
}
