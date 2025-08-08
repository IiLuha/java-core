package org.itdev.multithreading.counter;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerCounter  implements SiteVisitCounter{

    private AtomicInteger visitCount;

    public AtomicIntegerCounter() {
        visitCount = new AtomicInteger(0);
    }

    @Override
    public void incrementVisitCount() {
        visitCount.incrementAndGet();
    }

    @Override
    public int getVisitCount() {
        return visitCount.get();
    }
}
