package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/*
여러 개의 스레드로 작업을 동시에 실행할 경우,
가끔은 작업을 조율하여 작업의 일부가 나머지 작업이 실행되기 전에
모든 스레드에 의해 완료될 수 있도록 만들고 싶은 경우가 발생할 수 있습니다.
 */
public class SemaphoreEx {
    public static void main(String [] args) throws InterruptedException {
        int numberOfThreads = 200; //or any number you'd like

        List<Thread> threads = new ArrayList<>();

        Barrier barrier = new Barrier(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new Thread(new CoordinatedWorkRunner(barrier)));
        }

        for(Thread thread: threads) {
            thread.start();
        }
    }

    /*
    세마포어를 0으로 초기화해서, 세마포어를 획득하려는 모든 스레드가 블록될 수 있도록 합니다.
    그리고 "numberOfWorkers - 1" 스레드는 세마포어에서 블록된 상태이므로,
    barrier에 도달한 마지막 스레드가 numberOfWorkers - 1 세마포어를 릴리스합니다.
     */
    public static class Barrier {
        private final int numberOfWorkers;
        private final Semaphore semaphore = new Semaphore(0);
        private int counter = 0;
        private final Lock lock = new ReentrantLock();

        public Barrier(int numberOfWorkers) {
            this.numberOfWorkers = numberOfWorkers;
        }

        public void waitForOthers() throws InterruptedException {
            lock.lock();
            boolean isLastWorker = false;
            try {
                counter++;

                if (counter == numberOfWorkers) {
                    isLastWorker = true;
                }
            } finally {
                lock.unlock();
            }

            if (isLastWorker) {
                semaphore.release(numberOfWorkers-1);
            } else {
                semaphore.acquire();
            }
        }
    }

    public static class CoordinatedWorkRunner implements Runnable {
        private final Barrier barrier;

        public CoordinatedWorkRunner(Barrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                task();
            } catch (InterruptedException e) {
            }
        }

        //다음 작업은 여러 개의 스레드에 의해 동시에 수행됩니다.
        //해당 작업을 동시에 실행하는 3개의 스레드가 있는 경우, 결괏값이 다음과 같아지도록 하려고 합니다.
        /*
        thread1 part 1 of the work is finished
        thread2 part 1 of the work is finished
        thread3 part 1 of the work is finished

        thread2 part 2 of the work is finished
        thread1 part 2 of the work is finished
        thread3 part 2 of the work is finished

        각 파트의 실행 순서는 중요하지 않습니다.
        단,  다른 스레드가 계속해서 part2를 수행하기 전에, 모든 스레드가 반드시 part1을 완료하도록 만들려고 합니다.
         */
        private void task() throws InterruptedException {
            // Performing Part 1
            System.out.println(Thread.currentThread().getName()
                    + " part 1 of the work is finished");

            barrier.waitForOthers();

            // Performing Part2
            System.out.println(Thread.currentThread().getName()
                    + " part 2 of the work is finished");
        }
    }

}