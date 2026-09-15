class Node:
    def __init__(self, key: int = 0, val: int = 0, next: 'Node' = None, prev: 'Node' = None):
        self.key = key
        self.val = val
        self.next = next
        self.prev = prev

class LRUCache:
    def __init__(self, capacity: int):
        self.capacity = capacity
        self.cache = {}
        self.head = Node()
        self.tail = Node()
        self.head.next = self.tail
        self.tail.prev = self.head

    def get(self, key: int) -> int:
        if key not in self.cache:
            return -1
        node = self.cache[key]
        self._move_to_head(node)
        return node.val

    def put(self, key: int, value: int) -> None:
        if key not in self.cache:
            node = Node(key, value)
            self._add_to_head(node)
            self.cache[key] = node
        else:
            node = self.cache[key]
            node.val = value
            self._move_to_head(node)

        if len(self.cache) > self.capacity:
            lru = self._pop_tail()
            del self.cache[lru.key]

    def _remove(self, node: Node) -> None:
        node.prev.next = node.next
        node.next.prev = node.prev

    def _add_to_head(self, node: Node) -> None:
        node.prev = self.head
        node.next = self.head.next
        self.head.next.prev = node
        self.head.next = node

    def _move_to_head(self, node: Node) -> None:
        self._remove(node)
        self._add_to_head(node)

    def _pop_tail(self) -> Node:
        lru = self.tail.prev
        self._remove(lru)
        return lru