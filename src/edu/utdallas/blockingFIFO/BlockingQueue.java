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

    private int headIndex;     //first to out
    private int tailIndex;     //last to out , most recently added
    private int queueSize = 5;

    private static Object monitor;
    private Task[] blockingQueue;

    private static BlockingQueue instance;

    private BlockingQueue() throws InterruptedException {
        this.monitor = new Object();

        this.headIndex=-1;
        this.tailIndex=-1;

        blockingQueue = new Task[queueSize];
    }


    public static BlockingQueue getInstance() throws InterruptedException {
        if(instance == null){
            instance = new BlockingQueue();
        }
        return instance;
    }

    public void put(Task task) throws InterruptedException
    {
        if ((this.tailIndex+1)%this.queueSize==headIndex) {
            throw new IllegalStateException("Queue is full");
        }
         else if (this.isQueueEmpty())  //
        {
            this.headIndex++;  //
            this.tailIndex++;
            this.blockingQueue[this.tailIndex] = task;
        }
        else {
            this.tailIndex=(this.tailIndex+1)%this.queueSize;  //shifting tail +1 or moving to 0 if reached max size (0 index should be available)
            this.blockingQueue[this.tailIndex] = task;         //latest inserted task goes to tail
        }


        synchronized(monitor) {
            monitor.notify();
        }
    }

    public Task take() throws InterruptedException
    {
        synchronized(monitor) {
            monitor.wait();
        }

        Task returningTask = null;

        if (this.isQueueEmpty()) {
            throw new IllegalStateException("Queue is empty");
        } else if ( this.headIndex == this.tailIndex) {
            returningTask = this.blockingQueue[ this.headIndex];
            this.headIndex = -1;
            this.tailIndex = -1;

        } else {
            returningTask = this.blockingQueue[ this.headIndex];
            this.headIndex=( this.headIndex+1)%this.queueSize;

        }

        return returningTask;
    }

    private boolean isQueueEmpty() {
        return (this.headIndex == -1 && this.tailIndex == -1);
    }
}
