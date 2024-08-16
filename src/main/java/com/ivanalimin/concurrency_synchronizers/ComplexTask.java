package com.ivanalimin.concurrency_synchronizers;

import java.util.concurrent.Callable;

public class ComplexTask implements Callable<Long> {
    private final long from;
    private final long to;

    public ComplexTask(long from, long to) {
        this.from = from;
        this.to = to;
    }

    public Long execute() {
        return call();
    }

    @Override
    public Long call() {
        long sum = 0;
        for (long i = from; i <= to; i++) {
            sum += i;
        }
        System.out.println("Sum from " + from + " to " + to + " = " + sum);
        return sum;
    }
}
