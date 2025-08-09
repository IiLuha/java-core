package org.itdev.multithreading.synchronize.counter;

public class UnsynchronizedCounter implements SiteVisitCounter{

    private int visitCount;

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
