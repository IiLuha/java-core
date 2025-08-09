package org.itdev.integration.multithreading.finaltask;

import org.itdev.multithreading.finaltask.DataProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Integer.MAX_VALUE;

public class FinalTaskIT {

    public static final int ZERO = 0;
    public static final int N_TASKS = 100;
    public static final int N_INTS = 10;
    public static final int N_THREADS = 10;

    private Random random;
    private DataProcessor dataProcessor;

    @BeforeEach
    public void init() {
        random = new Random();
    }

    @Test
    public void testCachedThreadPool() {
        dataProcessor = new DataProcessor();
        List<List<Integer>> intsOfTasks = new ArrayList<>(N_TASKS);
        List<String> taskNames = new ArrayList<>(N_TASKS);
        for (int i = 0; i < N_TASKS; i++) {
            List<Integer> ints = new ArrayList<>(N_INTS);
            intsOfTasks.add(ints);
            for (int j = 0; j < N_INTS; j++) {
                ints.add(random.nextInt(MAX_VALUE/ N_INTS));
            }
        }
        intsOfTasks.stream()
                .map(dataProcessor::CalculateSumTask)
                .forEach(taskNames::add);
        while (dataProcessor.getTaskCountInt() > 0) {
            try {
                Thread.sleep(0L,1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(dataProcessor.getTaskCountInt());
        }
        taskNames.stream()
                .map(dataProcessor::getTaskResult)
                .forEach(System.out::println);
    }

    @Test
    public void testFixedThreadPool() {
        dataProcessor = new DataProcessor(
                new AtomicInteger(ZERO),
                Executors.newFixedThreadPool(N_THREADS),
                new HashMap<>(N_TASKS)
        );
        List<List<Integer>> intsOfTasks = new ArrayList<>(N_TASKS);
        List<String> taskNames = new ArrayList<>(N_TASKS);
        for (int i = 0; i < N_TASKS; i++) {
            List<Integer> ints = new ArrayList<>(N_INTS);
            intsOfTasks.add(ints);
            for (int j = 0; j < N_INTS; j++) {
                ints.add(random.nextInt(MAX_VALUE/ N_INTS));
            }
        }
        intsOfTasks.stream()
                .map(dataProcessor::CalculateSumTask)
                .forEach(taskNames::add);
        while (dataProcessor.getTaskCountInt() > 0) {
            try {
                Thread.sleep(0L, 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(dataProcessor.getTaskCountInt());
        }
        taskNames.stream()
                .map(dataProcessor::getTaskResult)
                .forEach(System.out::println);
    }

    @Test
    public void testSingleThreadExecutor() {
        dataProcessor = new DataProcessor(
                new AtomicInteger(ZERO),
                Executors.newSingleThreadExecutor(),
                new HashMap<>(N_TASKS)
        );
        List<List<Integer>> intsOfTasks = new ArrayList<>(N_TASKS);
        List<String> taskNames = new ArrayList<>(N_TASKS);
        for (int i = 0; i < N_TASKS; i++) {
            List<Integer> ints = new ArrayList<>(N_INTS);
            intsOfTasks.add(ints);
            for (int j = 0; j < N_INTS; j++) {
                ints.add(random.nextInt(MAX_VALUE/ N_INTS));
            }
        }
        intsOfTasks.stream()
                .map(dataProcessor::CalculateSumTask)
                .forEach(taskNames::add);
        while (dataProcessor.getTaskCountInt() > 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(dataProcessor.getTaskCountInt());
        }
        taskNames.stream()
                .map(dataProcessor::getTaskResult)
                .forEach(System.out::println);
    }
}
