import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainWithoutThreadJoin {

    public static void main(String[] args) {
        // 아래 리스트에 나온 숫자들로 팩토리얼 계산을 할 예정. 각 계산은 각각의 스레드들에게 할당되어 병렬로 실행시킬 예정이다.
        List<Long> inputNumbers = Arrays.asList(0L, 3435L, 35435L, 2324L, 456L, 23L, 2435L, 5566L);

        List<FactorialThread> threads = new ArrayList<>();

        for (long inputNumber : inputNumbers) {
            threads.add(new FactorialThread(inputNumber));
        }
        // FactorialThread와 main 스레드가 동시에 실행됨.
        for (Thread thread : threads) {
            thread.start();
        }

        //원래 목적: 메인 스레드는 FactorialThread 계산이 다 끝난 것을 취합해서 출력하고 싶다.
        // 그런데 실행해보면, 아래 결과에서 else문이 된 것이 있다. 그 이유는 메인스레드가 해당 스레드들보다 더 빨리 끝났기 때문이다.
        for (int i = 0; i < inputNumbers.size(); i++) {
            FactorialThread factorialThread = threads.get(i);

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
