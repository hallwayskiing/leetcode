class Solution:
    def isValid(self, s: str) -> bool:
        stack = []
        mapping = {"(": ")", "{": "}", "[": "]"}

        for char in s:
            if char in mapping:
                stack.append(mapping[char])
            elif not stack or char != stack.pop():
                return False

        return not stack