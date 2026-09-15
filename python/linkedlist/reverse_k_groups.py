from linkedlist.list_node import ListNode


class Solution:
    def reverseKGroup(self, head: ListNode, k: int) -> ListNode:
        start = end = head
        for _ in range(k):
            if not end:
                return head
            end = end.next

        new_head = self.reverse(start, end)
        start.next = self.reverseKGroup(end, k)
        return new_head

    def reverse(self, start: ListNode, end: ListNode):
        prev = None
        next = None 

        while start != end:
            next = start.next
            start.next = prev
            prev = start
            start = next

        return prev