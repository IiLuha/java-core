package org.itdev.multithreading.synchronize.counter;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockCounter  implements SiteVisitCounter{

    private int visitCount;
    private final ReentrantLock lock;

    public ReentrantLockCounter() {
        lock = new ReentrantLock();
    }

    @Override
    public void incrementVisitCount() {
        try {
            lock.lock();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
