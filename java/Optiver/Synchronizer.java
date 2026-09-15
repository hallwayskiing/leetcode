package Optiver;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Synchronizer {
    /**
     * Print 0-N sequentially using semaphores
     */
    public void syncSemaphore() {
        int N = 10;
        Semaphore[] semaphores = new Semaphore[N];
        for (int i = 0; i < N; i++) {
            semaphores[i] = new Semaphore(0);
        }
        for (int i = 0; i < N; i++) {
            int j = i;
            new Thread(() -> {
                try {
                    semaphores[j].acquire();
                    System.out.println(j);
                    if (j + 1 < N) {
                        semaphores[j + 1].release();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        semaphores[0].release();
    }

    /**
     * Print 0-N sequentially using lock and condition
     */
    public void syncLock() {
        int N = 10;
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        final int[] turn = {0};

        for (int i = 0; i < N; i++) {
            int threadId = i;
            new Thread(() -> {
                lock.lock();
                try {
                    while (turn[0] != threadId) {
                        condition.await();
                    }
                    System.out.println(threadId);
                    turn[0]++;
                    condition.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }).start();
        }
    }

    /**
     * Print 0 and 1 in turn
     */
    public void printInTurnInfinite() {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        int[] turn = new int[]{0};
        for (int i = 0; i < 2; i++) {
            int threadId = i;
            new Thread(() -> {
                while (true) {
                    lock.lock();
                    try {
                        if (turn[0] != threadId) {
                            condition.await();
                        }
                        System.out.println(threadId);
                        turn[0] = 1 - turn[0];
                        condition.signal();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            }).start();
        }
    }

    /**
     * Print 0-N sequentially with limited threads using lock and condition
     */
    public void printInTurn() {
        int N = 20;
        int threads = 3;
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        final int[] turn = {0};

        for (int i = 0; i < threads; i++) {
            int threadId = i;
            new Thread(() -> {
                while (true) {
                    lock.lock();
                    try {
                        if (turn[0] >= N) {
                            condition.signalAll();
                            break;
                        }

                        while (turn[0] % threads != threadId) {
                            condition.await();
                            if (turn[0] >= N) {
                                return;
                            }
                        }
                        System.out.println(turn[0]);
                        turn[0]++;
                        condition.signalAll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            }).start();
        }
    }

    /**
     * Run threads in turn using lock and condition
     */
    public void runInTurn() {
        int N = 5;
        int threads = 3;

        int total = N * threads;
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        final int[] turn = {0};

        for (int i = 0; i < threads; i++) {
            int j = i;
            new Thread(() -> {
                while (true) {
                    lock.lock();
                    try {
                        if (turn[0] >= total) {
                            condition.signalAll();
                            break;
                        }

                        while (turn[0] % threads != j) {
                            condition.await();
                            if (turn[0] >= total) {
                                return;
                            }
                        }
                        System.out.println(j);
                        turn[0]++;
                        condition.signalAll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        new Synchronizer().printInTurnInfinite();
    }
}
