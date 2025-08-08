package org.itdev.multithreading.counter;

public class SynchronizedBlockCounter  implements SiteVisitCounter{

    private int visitCount;
    private final Object monitor;

    public SynchronizedBlockCounter() {
        monitor = new Object();
    }

    @Override
    public void incrementVisitCount() {
        synchronized(monitor) {
            visitCount++;
        }
    }

    @Override
    public int getVisitCount() {
        return visitCount;
    }
}
