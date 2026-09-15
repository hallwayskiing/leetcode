package Hot100.LinkedList;

public class ReverseLinkedList {
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode prev=null, next=null;
        while (head!=null){
            next=head.next;
            head.next=prev;
            prev=head;
            head=next;
        }

        return prev;
    }
}
