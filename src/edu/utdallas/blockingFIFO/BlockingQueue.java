package edu.utdallas.blockingFIFO;

import edu.utdallas.taskExecutor.Task;
import edu.utdallas.taskExecutorImpl.TaskImpl;
import javafx.beans.binding.ObjectExpression;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Anton on 3/22/2016.
 *
 * http://www.programcreek.com/2009/02/notify-and-wait-example/
 */




public class BlockingQueue
{

    private static Object monitor;
    private ArrayBlockingQueue<Task> blockingQueue;

    private static BlockingQueue instance;

    private BlockingQueue() throws InterruptedException {
        this.monitor = new Object();
        blockingQueue = new ArrayBlockingQueue<Task>(5);
    }


    public static BlockingQueue getInstance() throws InterruptedException {
        if(instance == null){
            instance = new BlockingQueue();
        }
        return instance;
    }

    public void put(Task task) throws InterruptedException
    {
        this.blockingQueue.add(task);
        synchronized(monitor) {
            monitor.notify();
        }
    }

    public Task take() throws InterruptedException
    {
        synchronized(monitor) {
            monitor.wait();
        }
        Task task = this.blockingQueue.take();

        return task;

    }
}
