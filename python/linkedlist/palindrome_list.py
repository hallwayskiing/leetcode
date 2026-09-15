from linkedlist.list_node import ListNode


class Solution:
    def isPalindrome(self, head: ListNode) -> bool:
        arr = []
        while head:
            arr.append(head.val)
            head = head.next

        return arr == arr[::-1]

    def isPalindrome2(self, head: ListNode) -> bool:
        if not head:
            return True

        # Find the middle of the linked list
        slow = head
        fast = head
        while fast and fast.next:
            slow = slow.next
            fast = fast.next.next

        # Reverse the second half of the linked list
        prev = None
        while slow:
            next_node = slow.next
            slow.next = prev
            prev = slow
            slow = next_node

        # Compare the first half and the reversed second half
        left, right = head, prev
        while right:  # Only need to compare until the end of the shorter half
            if left.val != right.val:
                return False
            left = left.next
            right = right.next

        return True