package Hot100.LinkedList;

/**
 * Given a linked list, swap every two adjacent nodes and return its head.
 * You must solve the problem without modifying the values in the list's nodes (i.e., only nodes themselves may be changed.)
 */
public class SwapNodesInPair {
    public ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode prev = dummy;
        ListNode left = head;

        while (left != null && left.next != null) {
            ListNode right = left.next;
            ListNode next = right.next;

            prev.next = right;
            right.next = left;
            left.next = next;

            prev = left;
            left = next;
        }

        return dummy.next;
    }
}
