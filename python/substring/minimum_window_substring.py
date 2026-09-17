# Given two strings s and t of lengths m and n respectively,
# return the minimum window substring of s such that every character in t (including duplicates) is included in the window.
# If there is no such substring, return the empty string "".
# The testcases will be generated such that the answer is unique.

from collections import Counter


class Solution:

    def minWindow(self, s: str, t: str) -> str:
        need = Counter(t)
        remain = len(t)
        start, end = 0, float("inf")
        left = 0

        for right, char in enumerate(s):
            # 如果当前字符仍在 need 中（>0 说明是有效命中）
            if need[char] > 0:
                remain -= 1
            need[char] -= 1

            # 窗口已经包含 t 的所有字符，开始收缩左边界
            if remain == 0:
                while need[s[left]] < 0:
                    need[s[left]] += 1
                    left += 1

                # 更新最优区间
                if right - left < end - start:
                    start, end = left, right

                # 弹出 left 对应的必要字符，准备寻找下一个可能解
                need[s[left]] += 1
                remain += 1
                left += 1

        return "" if end == float("inf") else s[start : end + 1]
        