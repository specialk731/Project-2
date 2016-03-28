package edu.utdallas.taskExecutorImpl;

import edu.utdallas.blockingFIFO.BlockingQueue;
import edu.utdallas.taskExecutor.Task;

/**
 * Created by Anton on 3/22/2016.
 */
public class TaskRunner implements Runnable
{

    private BlockingQueue blockingQueue;
    @Override
    public void run() {
        while(true) {
            // take() blocks if queue is empty
            try {
                Task newTask = this.blockingQueue.take();

                newTask.execute();
            }
            catch(Throwable th)
            {
                System.out.println("Error: "+th.getMessage() + "  " + th.toString() + " " +th.getCause());
            }
        }

    }

    public void setBlockingQueue(BlockingQueue bq)
    {
        this.blockingQueue=bq;
    }

}
