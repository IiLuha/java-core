package org.itdev.integration.multithreading.synchronize;

import org.itdev.multithreading.synchronize.counter.AtomicIntegerCounter;
import org.itdev.multithreading.synchronize.counter.ReentrantLockCounter;
import org.itdev.multithreading.synchronize.counter.SiteVisitCounter;
import org.itdev.multithreading.synchronize.counter.SynchronizedBlockCounter;
import org.itdev.multithreading.synchronize.counter.UnsynchronizedCounter;
import org.itdev.multithreading.synchronize.counter.VolatileCounter;
import org.itdev.multithreading.synchronize.visitor.MultithreadingSiteVisitor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/* Результат вывода:
Volatile10 time = 0
count = 10
Lock10 time = 0
count = 10
Atomic10 time = 0
count = 10
Unsynch10 time = 0
count = 10
Synch10 time = 0
count = 10
Lock100 time = 0
count = 100
Volatile100 time = 0
count = 100
Unsynch100 time = 0
count = 100
Synch100 time = 0
count = 100
Atomic100 time = 0
count = 100

"Проанализируйте и объясните результаты: время выполнения, точность счетчика, влияние разных
методов синхронизации.
Почему какие-то реализации счетчика быстрее/медленнее?
Почему какие-то реализации работают неправильно?"

Различий нет никаких, все варианты отработали правильно.
В теории volatile должен был отработать неправильно на большом кол-ве операций, так как он не спасает от Race condition,
и, соответственно, вариант без добавления синхронизаций тоже мог бы сработать неправильно.
Также вполне вероятно метод с блоком Synchronized должен отрабатывать медленнее чем варианты без синхронизации, волатайл
и атомик. Атомик работает напрямую с памятью, через объект Unsafe методы которого написаны на C (или С++, не помню), что
позволяет ему быстро работать, а в вариантах волатайл и без синхронизации просто нет дорогостоющих операций
синхронизации, потому они быстрее. Лок наверно должен работать по времени немного быстрее синхронайзд блока, но точно не
знаю.

После изменения классов результат такой:
Volatile10 time = 161
count = 10
Lock10 time = 2
count = 10
Atomic10 time = 105
count = 10
Unsynch10 time = 108
count = 6
Synch10 time = 1042
count = 10
Atomic100 time = 120
count = 100
Lock100 time = 9
count = 100
Volatile100 time = 110
count = 93
Unsynch100 time = 108
count = 95
Synch100 time = 10979
count = 100

Как и ожидалось volatile и не синхронизированный варианты были посчитанны с ошибкой, так как никак не предотвращают Race
condition. Вариант с блоком synchronized оказался самым долгим, при этом лок отработал очень быстро, гораздо быстрее.

Увидел, что в локе не туда вставил слип, переписал ещё раз.
Третий результат:
Volatile10 time = 104
count = 9
Lock10 time = 1110
count = 10
Synch10 time = 1057
count = 10
Atomic10 time = 107
count = 10
Unsynch10 time = 109
count = 3
Lock100 time = 11025
count = 100
Volatile100 time = 111
count = 91
Unsynch100 time = 109
count = 95
Synch100 time = 10961
count = 100
Atomic100 time = 110
count = 100

Теперь лок по времени почти такой же, как synchronized блок.
 */
public class ThreadsIT {

    private static final int TEN = 10;
    private static final int HUNDRED = 100;

    private SiteVisitCounter counter;
    private MultithreadingSiteVisitor visitor;

    @Test
    public void Unsynch10ThreadTest() {
        counter = new UnsynchronizedCounter();
        visitor = new MultithreadingSiteVisitor(counter);
        visitor.visitMultithread(TEN);
        assertDoesNotThrow(visitor::waitUntilAllVisited);
        long time = visitor.getTotalTimeOfHandling();
        System.out.println("Unsynch10 time = " + time);
        System.out.println("count = " + counter.getVisitCount());
    }

    @Test
    public void Volatile10ThreadTest() {
        counter = new VolatileCounter();
        visitor = new MultithreadingSiteVisitor(counter);
        visitor.visitMultithread(TEN);
        assertDoesNotThrow(visitor::waitUntilAllVisited);
        long time = visitor.getTotalTimeOfHandling();
        System.out.println("Volatile10 time = " + time);
        System.out.println("count = " + counter.getVisitCount());
    }

    @Test
    public void Atomic10ThreadTest() {
        counter = new AtomicIntegerCounter();
        visitor = new MultithreadingSiteVisitor(counter);
        visitor.visitMultithread(TEN);
        assertDoesNotThrow(visitor::waitUntilAllVisited);
        long time = visitor.getTotalTimeOfHandling();
        System.out.println("Atomic10 time = " + time);
        System.out.println("count = " + counter.getVisitCount());
    }

    @Test
    public void Synch10ThreadTest() {
        counter = new SynchronizedBlockCounter();
        visitor = new MultithreadingSiteVisitor(counter);
        visitor.visitMultithread(TEN);
        assertDoesNotThrow(visitor::waitUntilAllVisited);
        long time = visitor.getTotalTimeOfHandling();
        System.out.println("Synch10 time = " + time);
        System.out.println("count = " + counter.getVisitCount());
    }

    @Test
    public void Lock10ThreadTest() {
        counter = new ReentrantLockCounter();
        visitor = new MultithreadingSiteVisitor(counter);
        visitor.visitMultithread(TEN);
        assertDoesNotThrow(visitor::waitUntilAllVisited);
        long time = visitor.getTotalTimeOfHandling();
        System.out.println("Lock10 time = " + time);
        System.out.println("count = " + counter.getVisitCount());
    }

    @Test
    public void Unsynch100ThreadTest() {
        counter = new UnsynchronizedCounter();
        visitor = new MultithreadingSiteVisitor(counter);
        visitor.visitMultithread(HUNDRED);
        assertDoesNotThrow(visitor::waitUntilAllVisited);
        long time = visitor.getTotalTimeOfHandling();
        System.out.println("Unsynch100 time = " + time);
        System.out.println("count = " + counter.getVisitCount());
    }

    @Test
    public void Volatile100ThreadTest() {
        counter = new VolatileCounter();
        visitor = new MultithreadingSiteVisitor(counter);
        visitor.visitMultithread(HUNDRED);
        assertDoesNotThrow(visitor::waitUntilAllVisited);
        long time = visitor.getTotalTimeOfHandling();
        System.out.println("Volatile100 time = " + time);
        System.out.println("count = " + counter.getVisitCount());
    }

    @Test
    public void Atomic100ThreadTest() {
        counter = new AtomicIntegerCounter();
        visitor = new MultithreadingSiteVisitor(counter);
        visitor.visitMultithread(HUNDRED);
        assertDoesNotThrow(visitor::waitUntilAllVisited);
        long time = visitor.getTotalTimeOfHandling();
        System.out.println("Atomic100 time = " + time);
        System.out.println("count = " + counter.getVisitCount());
    }

    @Test
    public void Synch100ThreadTest() {
        counter = new SynchronizedBlockCounter();
        visitor = new MultithreadingSiteVisitor(counter);
        visitor.visitMultithread(HUNDRED);
        assertDoesNotThrow(visitor::waitUntilAllVisited);
        long time = visitor.getTotalTimeOfHandling();
        System.out.println("Synch100 time = " + time);
        System.out.println("count = " + counter.getVisitCount());
    }

    @Test
    public void Lock100ThreadTest() {
        counter = new ReentrantLockCounter();
        visitor = new MultithreadingSiteVisitor(counter);
        visitor.visitMultithread(HUNDRED);
        assertDoesNotThrow(visitor::waitUntilAllVisited);
        long time = visitor.getTotalTimeOfHandling();
        System.out.println("Lock100 time = " + time);
        System.out.println("count = " + counter.getVisitCount());
    }
}
