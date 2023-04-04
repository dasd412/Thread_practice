public class Main2 {
    public static void main(String[] args) {

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException("Some exception");
            }
        });

        thread.setName("misbehaving thread");

        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("a critical error happened in thread "+t.getName()+"the error message is "+e.getMessage());
            }
        });

        thread.start();
    }
}
