package com.ivanalimin.concurrency_synchronizers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ComplexTaskExecutor {
    private final int value;
    private final CyclicBarrier barrier;

    public ComplexTaskExecutor(int numberOfTasks, int value) {
        this.value = value;
        this.barrier = new CyclicBarrier(numberOfTasks);
    }

    public void executeTasks(int numberOfTasks) {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfTasks);
        List<Future<Long>> futures = new ArrayList<>();
        long range = value / numberOfTasks;
        for (int i = 0; i < numberOfTasks; i++) {
            long from = i * range + 1;
            long to = (i + 1) * range;
            ComplexTask task = new ComplexTask(from, to);
            futures.add(executorService.submit(() -> {
                long result = task.execute();
                barrier.await();
                return result;
            }));
        }
        executorService.shutdown();

        long globalSum = futures.stream()
                .mapToLong(sum -> {
                    try {
                        return sum.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }).sum();
        System.out.println("Global sum of all digits of " + value + " is: " + globalSum);
    }
}
