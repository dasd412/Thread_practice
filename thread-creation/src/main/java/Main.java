public class Main {

    public static void main(String[] args) throws InterruptedException {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                //새로운 스레드에서 실행될 코드
                System.out.println("we are now in thread : "+Thread.currentThread().getName());
                System.out.println("current thread priority is "+Thread.currentThread().getPriority());
            }
        });

        thread.setName("new worker thread");

        thread.setPriority(Thread.MAX_PRIORITY);

        System.out.println("We are in thread : "+Thread.currentThread().getName()+" before starting a new thread");
        thread.start();
        System.out.println("We are in thread : "+Thread.currentThread().getName()+" after starting a new thread");
    }
}
