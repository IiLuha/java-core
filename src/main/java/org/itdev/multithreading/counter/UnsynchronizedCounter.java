package org.itdev.multithreading.counter;

public class UnsynchronizedCounter implements SiteVisitCounter{

    private int visitCount;

    @Override
    public void incrementVisitCount() {
        visitCount++;
    }

    @Override
    public int getVisitCount() {
        return visitCount;
    }
}
