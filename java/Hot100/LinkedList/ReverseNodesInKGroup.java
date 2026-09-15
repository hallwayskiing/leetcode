package Hot100.LinkedList;

public class ReverseNodesInKGroup {
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode prev = dummy; // end of last group
        ListNode end = dummy; // end of current group

        while (true) {
            for (int i = 0; i < k && end != null; i++) {
                end = end.next;
            }
            if (end == null) break;

            ListNode start = prev.next; // start of current group
            ListNode next = end.next; // start of next group
            end.next = null;

            prev.next = reverse(start);
            start.next = next;

            prev = end = start;
        }

        return dummy.next;
    }

    public ListNode reverse(ListNode head) {
        ListNode prev = null, next = null;
        while (head != null) {
            next = head.next;
            head.next = prev;
            prev = head;
            head = next;
        }
        return prev;
    }

    public ListNode reverseKGroupII(ListNode head, int k){
        if (head==null) return null;

        ListNode start=head, end=head;
        for (int i=0;i<k;i++){
            if(end==null) return head;
            end=end.next;
        }
        ListNode newHead = reverseII(start, end);
        start.next=reverseKGroupII(end, k);
        return newHead;
    }

    public ListNode reverseII(ListNode start, ListNode end){
        ListNode prev = null;
        ListNode next = start;
        while (start!=end){
            next = start.next;
            start.next=prev;
            prev = start;
            start = next;
        }
        return prev;
    }
}
