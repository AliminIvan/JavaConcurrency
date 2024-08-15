package com.ivanalimin.concurrency_blocking_queue;

import java.util.Random;

/*
Предположим, у вас есть пул потоков, и вы хотите реализовать блокирующую очередь для передачи задач между потоками.
Создайте класс BlockingQueue, который будет обеспечивать безопасное добавление и извлечение элементов между
производителями и потребителями в контексте пула потоков.

Класс BlockingQueue должен содержать методы enqueue() для добавления элемента в очередь и dequeue() для извлечения
элемента. Если очередь пуста, dequeue() должен блокировать вызывающий поток до появления нового элемента.

Очередь должна иметь фиксированный размер.

Используйте механизмы wait() и notify() для координации между производителями и потребителями. Реализуйте метод size(),
который возвращает текущий размер очереди.


 */
public class BlockingQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new BlockingQueue<>(5);
        new Thread(new Producer(queue)).start();
        Thread.sleep(1000);
        new Thread(new Consumer(queue)).start();
    }

    static class Producer implements Runnable {
        private final BlockingQueue<Integer> queue;

        public Producer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            System.out.println("Producer run");

            for (int i = 0; i < 10; i++) {
                try {
                    queue.enqueue(produce());
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        private Integer produce() {
            Integer i = new Random().nextInt(100);
            System.out.println("Producer produce: " + i);
            return i;
        }
    }

    static class Consumer implements Runnable {
        private final BlockingQueue<Integer> queue;

        public Consumer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            System.out.println("Consumer run");

            for (int i = 0; i < 10; i++) {
                try {
                    consume();
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void consume() throws InterruptedException {
            Integer i = queue.dequeue();
            System.out.println("Consumer consumed: " + i);
        }
    }
}
