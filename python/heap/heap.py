class Heap:
    def min_heapify(self, arr: list[int], i: int, n: int) -> None:
        smallest = i
        left = 2 * i + 1
        right = 2 * i + 2

        if left < n and arr[left] < arr[smallest]:
            smallest = left

        if right < n and arr[right] < arr[smallest]:
            smallest = right

        if smallest != i:
            arr[i], arr[smallest] = arr[smallest], arr[i]
            self.min_heapify(arr, smallest, n)

    def build_min_heap(self, arr: list[int]) -> None:
        n = len(arr)

        for i in range(n // 2 - 1, -1, -1):
            self.min_heapify(arr, i, n)

    def heap_sort(self, arr: list[int]) -> None:
        """
        Sorts the array in the descending order.
        """
        n = len(arr)
        self.build_min_heap(arr)

        for i in range(n - 1, 0, -1):
            arr[0], arr[i] = arr[i], arr[0]
            self.min_heapify(arr, 0, i)