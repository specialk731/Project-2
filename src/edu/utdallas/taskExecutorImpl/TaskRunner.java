package edu.utdallas.taskExecutorImpl;

import edu.utdallas.blockingFIFO.BlockingQueue;
import edu.utdallas.taskExecutor.Task;

/**
 * Created by Anton Rogozhnikov, Kevin Greenwald
 */
public class TaskRunner implements Runnable
{
    private BlockingQueue blockingQueue;

    @Override
    public void run() {
        while(true) {

            try {
                Task newTask = this.blockingQueue.take();

                newTask.execute();
            }
            catch(Throwable th)
            {
                //System.out.println("Error: "+th.getMessage() + "  " + th.toString() + " " +th.getCause());
            }
        }

    }

    public void setBlockingQueue(BlockingQueue bq)
    {
        this.blockingQueue=bq;
    }

}
