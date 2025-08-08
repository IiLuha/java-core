package org.itdev.multithreading.counter;

public class VolatileCounter  implements SiteVisitCounter{

    private volatile int visitCount;

    @Override
    public void incrementVisitCount() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        visitCount++;
    }

    @Override
    public int getVisitCount() {
        return visitCount;
    }
}
