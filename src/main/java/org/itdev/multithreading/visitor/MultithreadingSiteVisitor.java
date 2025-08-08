package org.itdev.multithreading.visitor;

import org.itdev.multithreading.counter.SiteVisitCounter;

import java.util.ArrayList;
import java.util.List;

public class MultithreadingSiteVisitor {

    private SiteVisitCounter siteVisitCounter;
    private List<Thread> threads;
    private long startTime;
    private long endTime;

    public MultithreadingSiteVisitor(SiteVisitCounter siteVisitCounter) {
        this.siteVisitCounter = siteVisitCounter;
        threads = new ArrayList<>();
    }

    public void visitMultithread(int numOfThreads) {
        for (int i = 0; i < numOfThreads; i++) {
            threads.add(new Thread(siteVisitCounter::incrementVisitCount));
        }
        startTime = System.currentTimeMillis();
        for (Thread thread : threads) {
            thread.start();
        }
    }

    public void waitUntilAllVisited() throws InterruptedException {
        for (Thread thread : threads) {
            thread.join();
        }
        endTime = System.currentTimeMillis();
    }

    public long getTotalTimeOfHandling() {
        return (endTime - startTime) / 1000;
    }
}
