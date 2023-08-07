package org.example;

import java.util.Random;

public class DeadLockSolution {

    //가장 쉬운 방법은 순환 대기를 하지 못하도록 예방하는 것.

    public static void main(String[] args) {
        DeadLockSolution.Intersection intersection = new DeadLockSolution.Intersection();

        Thread trainA = new Thread(new DeadLockSolution.TrainA(intersection));
        Thread trainB = new Thread(new DeadLockSolution.TrainB(intersection));

        trainA.start();
        trainB.start();
    }

    public static class TrainA implements Runnable {
        private DeadLockSolution.Intersection intersection;
        private Random random = new Random();

        public TrainA(DeadLockSolution.Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while (true) {
                long sleepingTime = random.nextInt(5);

                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                intersection.takeRoadA();
            }
        }
    }

    public static class TrainB implements Runnable {
        private DeadLockSolution.Intersection intersection;
        private Random random = new Random();

        public TrainB(DeadLockSolution.Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while (true) {
                long sleepingTime = random.nextInt(5);

                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                intersection.takeRoadB();
            }
        }
    }

    public static class Intersection {
        private Object roadA = new Object();
        private Object roadB = new Object();

        public void takeRoadA() {
            synchronized (roadA) {
                System.out.println("Road A is locked by thread " + Thread.currentThread().getName());
                synchronized (roadB) {
                    System.out.println("Train is passing through road A");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        // locking 순서를 바꿈으로써 순환 대기 예방! (리소스를 동일한 순서로 잠그기만 하면 된다!)
        public void takeRoadB() {
            synchronized (roadA){
                System.out.println("Road B is locked by thread " + Thread.currentThread().getName());
                synchronized (roadB) {
                    System.out.println("Train is passing through road B");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
}
