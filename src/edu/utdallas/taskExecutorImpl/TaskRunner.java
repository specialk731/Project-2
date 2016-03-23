package edu.utdallas.taskExecutorImpl;

import edu.utdallas.blockingFIFO.BlockingQueue;
import edu.utdallas.taskExecutor.Task;

/**
 * Created by Anton on 3/22/2016.
 */
public class TaskRunner implements Runnable
{

    @Override
    public void run() {
        while(true) {
            // take() blocks if queue is empty
            try {
                Task newTask = BlockingQueue.getInstance().take();

                newTask.execute();

            }
            catch(Throwable th) {
                //TODO
                // Log (e.g. print exception’s message to console)
                // and drop any exceptions thrown by the task’s
                // execution.
            }
        }

    }
}
