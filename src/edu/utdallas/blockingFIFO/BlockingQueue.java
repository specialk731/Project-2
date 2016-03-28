package edu.utdallas.blockingFIFO;

import edu.utdallas.taskExecutor.Task;
import edu.utdallas.taskExecutorImpl.TaskImpl;
import javafx.beans.binding.ObjectExpression;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Anton on 3/22/2016.
 *
 */


public class BlockingQueue
{

    private boolean isFirstTask;
    private boolean isArrayActive;
    private int headIndex;     //first to out
    private int tailIndex;     //last to out , most recently added
    private int queueSize = 100;

    private static Object notfull ;
    private static Object notempty ;
    private static Object arrayInUse;

    //private static Object arrayOperationInProgress;


    private Task[] blockingQueue;

    private static BlockingQueue instance;

    public BlockingQueue() throws InterruptedException
    {
        this.isArrayActive = false;
        this.isFirstTask = true;
        this.headIndex=-1;
        this.tailIndex=-1;

        notfull = new Object();
        notempty = new Object();
        arrayInUse = new Object();  //lock for array operations may be not needed

        blockingQueue = new Task[queueSize];   //Instantiating queue (array type)
    }



    ////////////////////    PUT      /////////////////////////////

    public void put(Task task) throws InterruptedException {


        if ((this.tailIndex + 1) % this.queueSize == this.headIndex) {
            //System.out.println("Queue is full");
            synchronized (this.notfull) {
                this.notfull.wait();
            }
        }


        if (isArrayActive) {
            synchronized (this.arrayInUse) {
                this.arrayInUse.wait();

            }
        }
        this.isArrayActive = true;




        if (this.isQueueEmpty())  //head and tail will be the same if array was initially empty
        {
            this.headIndex++;
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


        synchronized (this.arrayInUse) {
            this.arrayInUse.notify();

        }
        this.isArrayActive=false;

    }




    ////////////////////    TAKE      /////////////////////////////


    public Task take() throws InterruptedException
    {
        Task returningTask = null;

        if (this.isQueueEmpty()) {
            //System.out.println("Queue is empty");
            synchronized(this.notempty) {
                this.notempty.wait();
            }
        }

        if (isArrayActive) {
            synchronized (this.arrayInUse) {
                this.arrayInUse.wait();
            }
        }
        this.isArrayActive = true;


        if ( this.headIndex == this.tailIndex) {
            returningTask = this.blockingQueue[ this.headIndex];
            this.headIndex = -1;
            this.tailIndex = -1;

        } else {
            returningTask = this.blockingQueue[ this.headIndex];
            this.headIndex=( this.headIndex+1)%this.queueSize;
        }

        synchronized (this.notfull)
        {
            this.notfull.notify();  //Task taken away - array is not full
        }



        synchronized (this.arrayInUse) {

            this.arrayInUse.notify();

        }
        this.isArrayActive=false;


        return returningTask;
    }

    private boolean isQueueEmpty()
    {
        return (this.headIndex == -1 && this.tailIndex == -1);

    }

}
