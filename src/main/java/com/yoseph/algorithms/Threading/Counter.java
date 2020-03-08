package com.yoseph.algorithms.Threading;

public class Counter {
    private int counter = 0;

    public int getCounter() {
        return counter;
    }

    public synchronized void setCounter(int counter) {
        this.counter += counter;
    }
}
