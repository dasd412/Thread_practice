import java.util.ArrayList;
import java.util.List;

public class Ex {

    public class MultiExecutor {

        // Add any necessary member variables here
        List<Thread>threads;

        /*
         * @param tasks to executed concurrently
         */
        public MultiExecutor(List<Runnable> tasks) {
            // Complete your code here
            this.threads=new ArrayList<>();

            for (Runnable task: tasks){
                threads.add(new Thread(new Runnable(){
                    @Override
                    public void run(){
                    }
                }));
            }
        }

        /**
         * Starts and executes all the tasks concurrently
         */
        public void executeAll() {
            // complete your code here
            for(Thread thread:threads){
                thread.start();
            }
        }
    }
}
