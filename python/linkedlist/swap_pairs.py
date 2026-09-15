# Given a linked list, swap every two adjacent nodes and return its head.
# You must solve the problem without modifying the values in the list's nodes (i.e., only nodes themselves may be changed.)

from linkedlist.list_node import ListNode


class Solution:
    def swapPairs(self, head: ListNode) -> ListNode:
        if not head or not head.next:
            return head

        dummy = ListNode()
        dummy.next = head
        prev = dummy
        left = head

        while left and left.next:
            right = left.next
            next = right.next

            # Swap the nodes
            prev.next = right
            right.next = left
            left.next = next    

            # Move to the next pair
            prev = left
            left = next

        return dummy.next