package org.itdev.multithreading.counter;

public class VolatileCounter  implements SiteVisitCounter{

    private volatile int visitCount;

    @Override
    public void incrementVisitCount() {
        visitCount++;
    }

    @Override
    public int getVisitCount() {
        return visitCount;
    }
}
