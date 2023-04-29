import java.math.BigInteger;
import java.util.*;

public class ComplexCalculation {

    public static void main(String[] args) throws InterruptedException {
        System.out.println(calculateResult(BigInteger.TEN,BigInteger.TEN,BigInteger.TWO,BigInteger.TEN));
    }

    public static BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) throws InterruptedException {
        BigInteger result=BigInteger.ZERO;
        /*
            Calculate result = ( base1 ^ power1 ) + (base2 ^ power2).
            Where each calculation in (..) is calculated on a different thread
        */
        List<PowerCalculatingThread> threads=Arrays.asList(new PowerCalculatingThread(base1,power1), new PowerCalculatingThread(base2,power2));


        for(Thread thread:threads){
            thread.start();
        }

        for(Thread thread:threads){
            thread.join();
        }

        for (PowerCalculatingThread thread : threads) {
            System.out.println(thread.getResult());
            result=result.add(thread.getResult());
        }



        return result;
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
           /*
           Implement the calculation of result = base ^ power
           */
            BigInteger i=power;

            while (i.compareTo(BigInteger.ZERO)>0){
                this.result=this.result.multiply(this.base);
                i=i.subtract(BigInteger.ONE);
            }
        }

        public BigInteger getResult() { return this.result; }
    }
}