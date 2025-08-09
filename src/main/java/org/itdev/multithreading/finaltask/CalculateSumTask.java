package org.itdev.multithreading.finaltask;

import java.util.List;
import java.util.concurrent.Callable;

public class CalculateSumTask implements Callable<Integer> {

    private List<Integer> intsToSum;
    private final String name;

    public CalculateSumTask(List<Integer> intsToSum, String name) {
        this.intsToSum = intsToSum;
        this.name = name;
    }


    @Override
    public Integer call() {
        System.out.println("Задачу " + name + " выполняет поток " + Thread.currentThread().getName());
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
        return intsToSum.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}
