package org.itdev.multithreading.cmpletablefuture.homework;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class HomeworkCompletableFutureDemo {

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(
                () -> factorial(random.nextInt(8)))
                .exceptionally(ex -> {
                    System.err.println("Oшибка: " + ex.getMessage());
                    return -1;
                });
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(
                        () -> factorial(random.nextInt(30)))
                .exceptionally(ex -> {
                    System.err.println("Oшибка: " + ex.getMessage());
                    return -1;
                });
        future1.thenCombine(future2, Math::max)
                .thenCompose(fact -> CompletableFuture.supplyAsync(() -> fact + 20))
                .thenAccept(System.out::println);
        Thread.sleep(2000000);
    }

    private static int factorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
            if (result > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("факториал числа " + n + " не поместился в int");
            }
        }
        return (int) result;
    }
}
