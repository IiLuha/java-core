package org.itdev.multithreading.counter;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerCounter  implements SiteVisitCounter{

    private AtomicInteger visitCount;

    public AtomicIntegerCounter() {
        visitCount = new AtomicInteger(0);
    }

    @Override
    public void incrementVisitCount() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        visitCount.incrementAndGet();
    }

    @Override
    public int getVisitCount() {
        return visitCount.get();
    }
}
