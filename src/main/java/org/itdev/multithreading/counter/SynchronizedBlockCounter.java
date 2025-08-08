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
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            visitCount++;
        }
    }

    @Override
    public int getVisitCount() {
        return visitCount;
    }
}
