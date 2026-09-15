from linkedlist.list_node import ListNode


class Solution:
    def removeNthFromEnd(self, head: ListNode, n: int) -> ListNode:
        fast = slow = dummy = ListNode(0, head)

        for _ in range(n+1): # Move fast pointer n+1 steps ahead to maintain a gap of n between slow and fast
            fast = fast.next

        while fast:
            fast = fast.next
            slow = slow.next

        slow.next = slow.next.next

        return dummy.next