class TimeMap:
    def __init__(self):
        self.store = {}

    def set(self, key: str, value: str, timestamp: int) -> None:
        if key not in self.store:
            self.store[key] = []
        self.store[key].append((value, timestamp))

    def get(self, key: str, timestamp: int) -> str:
        if key not in self.store:
            return ""

        records = self.store[key]
        # Binary search for the largest timestamp less than or equal to the given timestamp
        left, right = 0, len(records) - 1
        while left <= right:
            mid = (left + right) // 2
            if records[mid][1] <= timestamp:
                left = mid + 1
            else:
                right = mid - 1

        return records[right][0] if right >= 0 else ""