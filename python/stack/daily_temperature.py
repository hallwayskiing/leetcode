# Given an array of integers temperatures represents the daily temperatures,
# return an array answer such that answer[i] is the number of days you have to wait after the ith day to get a warmer temperature.
# If there is no future day for which this is possible, keep answer[i] == 0 instead.

class Solution:
    def dailyTemperatures(self, temperatures: list[int]) -> list[int]:
        n = len(temperatures)
        stack = []
        res = [0] * n

        for i in range(n):
            t = temperatures[i]
            while stack and t > temperatures[stack[-1]]:
                day = stack.pop()
                res[day] = i - day 
            stack.append(i)

        return res
