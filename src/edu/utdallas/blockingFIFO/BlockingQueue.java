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
    private int queueSize = 100;

    private static Object notfull ;
    private static Object notempty ;
    private Task[] blockingQueue;

    private static BlockingQueue instance;

    private BlockingQueue() throws InterruptedException {


        this.headIndex=-1;
        this.tailIndex=-1;

        notfull = new Object();
        notempty = new Object();

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
        if ((this.tailIndex+1)%this.queueSize==this.headIndex) {
            //System.out.println("Queue is full");
            synchronized (this.notfull)
            {
                this.notfull.wait();
            }
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
        synchronized(this.notempty) {
            this.notempty.notify();
        }
    }

    public Task take() throws InterruptedException
    {
        Task returningTask = null;

        if (this.isQueueEmpty()) {
            //System.out.println("Queue is empty");
            synchronized(this.notempty) {
                this.notempty.wait();
            }
        } else if ( this.headIndex == this.tailIndex) {
            returningTask = this.blockingQueue[ this.headIndex];
            this.headIndex = -1;
            this.tailIndex = -1;

        } else {
            returningTask = this.blockingQueue[ this.headIndex];
            this.headIndex=( this.headIndex+1)%this.queueSize;

        }

        synchronized (this.notfull)
        {
            this.notfull.notify();  //Task taken away array is not full
        }

        return returningTask;
    }

    private boolean isQueueEmpty() {
        return (this.headIndex == -1 && this.tailIndex == -1);
    }
}
