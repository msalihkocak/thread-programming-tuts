package com.example;

import java.util.concurrent.atomic.AtomicInteger;

public class StudyRunnableThreads {
    public static void main(String[] args) throws InterruptedException {
        //executeRacingThreads(); // Fastest but inconsistent
        executeSyncThreads(); // Slowest but consistent
        //executeAtomicIntegeredThreads(); // Middle point between fast and consistent
    }


    private static void executeSyncThreads() throws InterruptedException {
        LotterySyncTask task = new LotterySyncTask();
        startThreadsAndJoin(task);
    }

    private static void executeAtomicIntegeredThreads() throws InterruptedException {
        LotteryAtomicTask task = new LotteryAtomicTask();
        startThreadsAndJoin(task);
    }

    private static void executeRacingThreads() throws InterruptedException {
        LotteryTask task = new LotteryTask();
        startThreadsAndJoin(task);
    }

    private static void startThreadsAndJoin(LotteryTask task) throws InterruptedException {
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);
        Thread t4 = new Thread(task);
        long start = System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        long stop = System.nanoTime();
        System.err.println(task.getCounter() + " - " + (stop - start));
    }
}

class LotteryAtomicTask extends LotteryTask implements Runnable{

    private AtomicInteger atomicCounter = new AtomicInteger(0);

    @Override
    public void run() {
        for (int i = 0; i<100_000;i++) {
            atomicCounter.getAndIncrement();
        }
    }

    @Override
    public int getCounter() {
        return atomicCounter.get();
    }
}

class LotterySyncTask extends LotteryTask implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i<100_000;i++) {
            synchronized (this){
                super.setCounter(super.getCounter() + 1);
            }
        }
    }
}

class LotteryTask implements Runnable{

    private int counter;

    @Override
    public void run() {
        for (int i = 0; i<100_000;i++) {
                ++counter;
        }
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
