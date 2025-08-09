package org.itdev.multithreading.finaltask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.Executors.*;

public class DataProcessor {

    private final AtomicInteger taskCount;
    private final AtomicInteger currentTaskCount;
    private final ExecutorService poolThread;
    private final Map<String, Integer> results;

    public DataProcessor() {
        this(new AtomicInteger(0),
                new AtomicInteger(0),
                newCachedThreadPool(),
                new HashMap<>());
    }

    public DataProcessor(AtomicInteger taskCount,
                         AtomicInteger currentTaskCount, ExecutorService poolThread,
                         Map<String, Integer> results) {
        this.taskCount = taskCount;
        this.currentTaskCount = currentTaskCount;
        this.poolThread = poolThread;
        this.results = results;
    }

    public String CalculateSumTask(List<Integer> intsToSum) {
        String name = "task " + taskCount.incrementAndGet();
        poolThread.submit(() -> {
            currentTaskCount.incrementAndGet();
            Integer sum = new CalculateSumTask(intsToSum, name).call();
            synchronized (results) {
                results.put(name, sum);
            }
            currentTaskCount.decrementAndGet();
            taskCount.decrementAndGet();
        });
        return name;
    }

    public int getCurrentTaskCountInt() {
        return currentTaskCount.get();
    }

    public Optional<Integer> getTaskResult(String taskName) {
        return Optional.ofNullable(results.get(taskName));
    }

    public int getTaskCountInt() {
        return taskCount.get();
    }
}
