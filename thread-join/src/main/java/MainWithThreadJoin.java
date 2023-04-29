import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainWithThreadJoin {


    public static void main(String[] args) throws InterruptedException {
        // 아래 리스트에 나온 숫자들로 팩토리얼 계산을 할 예정. 각 계산은 각각의 스레드들에게 할당되어 병렬로 실행시킬 예정이다.
        List<Long> inputNumbers = Arrays.asList(100000L, 3435L, 35435L, 2324L, 456L, 23L, 2435L, 5566L);

        List<MainWithThreadJoin.FactorialThread> threads = new ArrayList<>();

        for (long inputNumber : inputNumbers) {
            threads.add(new MainWithThreadJoin.FactorialThread(inputNumber));
        }
        // FactorialThread와 main 스레드가 동시에 실행됨.
        for (Thread thread : threads) {
            thread.setDaemon(true);
            thread.start();
        }

        //join을 이용해서 스레드들이 작업을 마칠 때 까지 메인 스레드를 기다리게 한다.
        for(Thread thread:threads){
            thread.join(2000);//<- 모든 스레드의 thread.join()메서드는 스레드가 종료되야만 반환된다. join() 안에 시간을 설정하면, 해당 시간 이후에도 스레드가 종료 안되면 그 스레드를 강제 종료시킨다.
        }

        for (int i = 0; i < inputNumbers.size(); i++) {
            MainWithThreadJoin.FactorialThread factorialThread = threads.get(i);

            if (factorialThread.isFinished()) {
                System.out.println("Factorial of" + inputNumbers.get(i) + " is " + factorialThread.getResult());
            } else {
                System.out.println("The calculation for " + inputNumbers.get(i) + " is still in progress");
            }
        }

    }

    public static class FactorialThread extends Thread {
        private long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        public FactorialThread(long inputNumber) {
            this.inputNumber = inputNumber;
        }

        @Override
        public void run() {
            this.result = factorial(inputNumber);
            this.isFinished = true;
        }

        private BigInteger factorial(long n) {
            BigInteger tempResult = BigInteger.ONE;

            for (long i = n; i > 0; i--) {
                tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));
            }
            return tempResult;
        }

        public BigInteger getResult() {
            return result;
        }

        public boolean isFinished() {
            return isFinished;
        }
    }
}
