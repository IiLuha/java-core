package org.itdev.multithreading.finaltask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.Executors.*;

public class DataProcessor {

    private final AtomicInteger taskCount;
    private final ExecutorService threadPool;
    private final Map<String, Integer> results;

    public DataProcessor() {
        this(new AtomicInteger(0),
                newCachedThreadPool(),
                new HashMap<>());
    }

    public DataProcessor(AtomicInteger taskCount, ExecutorService poolThread,
                         Map<String, Integer> results) {
        this.taskCount = taskCount;
        this.threadPool = poolThread;
        this.results = results;
    }

    public String CalculateSumTask(List<Integer> intsToSum) {
        String name = "task " + taskCount.incrementAndGet();
        CompletableFuture.supplyAsync(new CalculateSumTask(intsToSum, name)::call, threadPool)
                .thenAccept(sum -> {
                    synchronized (results) {
                        results.put(name, sum);
                    }
                    taskCount.decrementAndGet();
                });
        return name;
    }

    public Optional<Integer> getTaskResult(String taskName) {
        synchronized (results) {
            return Optional.ofNullable(results.get(taskName));
        }
    }

    public int getTaskCountInt() {
        return taskCount.get();
    }

    public void shutdown() {
        threadPool.shutdown();
        try {
            boolean isTerminated = threadPool.awaitTermination(10, TimeUnit.MILLISECONDS);
            if (!isTerminated) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
            throw new RuntimeException(e);
        }
    }
}
