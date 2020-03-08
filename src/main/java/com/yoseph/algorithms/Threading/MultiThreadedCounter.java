package com.yoseph.algorithms.Threading;

public class MultiThreadedCounter {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        CounterThread[] threads = createThreadPools(4, counter);
        for(CounterThread thread: threads){
            thread.start();
            thread.join();
        }

        for(CounterThread thread: threads){
            System.out.println("Thread status is : " + thread.getName() + thread.isAlive());
        }

    }

    private static CounterThread[] createThreadPools(int size, Counter counter){
        CounterThread[] counterThreads = new CounterThread[size] ;
        for(int k=0 ; k < counterThreads.length ; k++){
            counterThreads[k] = new CounterThread(counter) ;
        }
        return counterThreads;
    }
}
