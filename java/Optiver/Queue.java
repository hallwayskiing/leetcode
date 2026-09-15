package Optiver;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class LinkedListQueue<T> {
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public LinkedListQueue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item);
        if (size == 0) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }

    public T dequeue() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        T data = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return data;
    }
}

class DynamicArrayQueue<T> {
    private Object[] array;
    private int head;
    private int tail;
    private static final int DEFAULT_CAPACITY = 10;

    public DynamicArrayQueue() {
        this.array = new Object[DEFAULT_CAPACITY];
        this.head = 0;
        this.tail = 0;
    }

    public void enqueue(T item) {
        if (tail == array.length) {
            if (head > 0) {
                compact();
            } else {
                resize(array.length * 2);
            }
        }
        array[tail++] = item;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        T data = (T) array[head];
        array[head] = null;
        head++;
        return data;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return (T) array[head];
    }

    public boolean isEmpty() {
        return head == tail;
    }

    private void resize(int newCapacity) {
        Object[] newArray = new Object[newCapacity];
        int size = tail - head;
        System.arraycopy(array, head, newArray, 0, size);
        array = newArray;
        head = 0;
        tail = size;
    }

    private void compact() {
        int size = tail - head;
        System.arraycopy(array, head, array, 0, size);
        for (int i = size; i < tail; i++) {
            array[i] = null;
        }
        head = 0;
        tail = size;
    }
}

class CircularBufferQueue<T> {
    private Object[] array;
    private int head;
    private int tail;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public CircularBufferQueue() {
        this.array = new Object[DEFAULT_CAPACITY];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    public void enqueue(T item) {
        if (size == array.length) {
            resize(array.length * 2);
        }
        array[tail] = item;
        tail = (tail + 1) % array.length;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        T data = (T) array[head];
        array[head] = null;
        head = (head + 1) % array.length;
        size--;
        return data;
    }

    private void resize(int newCapacity) {
        Object[] newArray = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[(head + i) % array.length];
        }
        array = newArray;
        head = 0;
        tail = size;
    }
}

class LockFreeQueue<T> {
    private static class Node<T> {
        final T data;
        final AtomicReference<Node<T>> next;

        Node(T data) {
            this.data = data;
            this.next = new AtomicReference<>(null);
        }
    }

    private final AtomicReference<Node<T>> head;
    private final AtomicReference<Node<T>> tail;

    public LockFreeQueue() {
        Node<T> dummy = new Node<>(null);
        this.head = new AtomicReference<>(dummy);
        this.tail = new AtomicReference<>(dummy);
    }

    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item);

        while (true) {
            Node<T> currentTail = tail.get();
            Node<T> tailNext = currentTail.next.get();

            if (currentTail == tail.get()) {
                if (tailNext == null) {
                    if (currentTail.next.compareAndSet(null, newNode)) {
                        tail.compareAndSet(currentTail, newNode);
                        return;
                    }
                } else {
                    tail.compareAndSet(currentTail, tailNext);
                }
            }
        }
    }

    public T dequeue() {
        while (true) {
            Node<T> currentHead = head.get();
            Node<T> currentTail = tail.get();
            Node<T> headNext = currentHead.next.get();

            if (currentHead == head.get()) {
                if (headNext == null) {
                    throw new NoSuchElementException("Queue is empty");
                }

                if (currentHead == currentTail) {
                    tail.compareAndSet(currentTail, headNext);
                } else {
                    T value = headNext.data;
                    if (head.compareAndSet(currentHead, headNext)) {
                        return value;
                    }
                }
            }
        }
    }

    public boolean isEmpty() {
        return head.get().next.get() == null;
    }
}

class BlockingQueue<T> {
    private final Object[] elements;
    private int head;
    private int tail;
    private int size;

    private final ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;

    public BlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.elements = new Object[capacity];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
        this.lock = new ReentrantLock();
        this.notEmpty = lock.newCondition();
        this.notFull = lock.newCondition();
    }

    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            while (size == elements.length) {
                notFull.await();
            }

            elements[tail] = item;
            tail = (tail + 1) % elements.length;
            size++;

            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (size == 0) {
                notEmpty.await();
            }

            T value = (T) elements[head];
            elements[head] = null;
            head = (head + 1) % elements.length;
            size--;

            notFull.signal();
            return value;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return size;
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }
}