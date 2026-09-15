from linkedlist.list_node import ListNode


class Solution:
    def reverseList(self, head: ListNode) -> ListNode:
        prev = None
        next = None

        while head:
            next = head.next
            head.next = prev
            prev = head
            head = next

        return prev