package com.yoseph.algorithms.Threading;

public class CounterThread extends Thread {
    private Counter counter;
    public CounterThread(Counter counter){
        this.counter = counter ;
    }
    @Override
    public void run() {
        while (counter.getCounter() < 100) {
            System.out.format("%s Thread  Before incrementing count was : %d \n", currentThread().getName(), counter.getCounter());
            this.counter.setCounter(1);
            System.out.format("%s Thread  After incrementing count is : %d \n", currentThread().getName(), counter.getCounter());
        }
    }
}
