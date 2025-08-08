package org.itdev.multithreading.counter;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockCounter  implements SiteVisitCounter{

    private int visitCount;
    private final ReentrantLock lock;

    public ReentrantLockCounter() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock = new ReentrantLock();
    }

    @Override
    public void incrementVisitCount() {
        try {
            lock.lock();
            visitCount++;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getVisitCount() {
        return visitCount;
    }
}
