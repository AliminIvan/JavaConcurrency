package com.ivanalimin.concurrency_blocking_queue;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue<T> {
    private final int capacity;
    private final Queue<T> queue = new LinkedList<>();

    public BlockingQueue() {
        this.capacity = 10;
    }

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void enqueue(T element) throws InterruptedException {
        while (this.queue.size() == this.capacity) {
            System.out.println("Queue is full, waiting until space is free");
            wait();
        }
        this.queue.add(element);
        notifyAll();
    }

    public synchronized T dequeue() throws InterruptedException {
        while (this.queue.isEmpty()) {
            System.out.println("Queue is empty, waiting until some element is put");
            wait();
        }
        notifyAll();
        return this.queue.poll();
    }

    public synchronized int size() {
        return queue.size();
    }
}
