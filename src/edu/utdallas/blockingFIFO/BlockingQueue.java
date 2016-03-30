package edu.utdallas.blockingFIFO;

import edu.utdallas.taskExecutor.Task;

/**
 * Created by Anton Rogozhnikov, Kevin Greenwald
 */

public class BlockingQueue
{

    private boolean isArrayActive; //to indicate whether another thread is making changes to array
    private int headIndex;         //first to out
    private int tailIndex;         //last to out , most recently added
    private int queueSize = 50;    //FifoQueue size is 50

    private static Object notfull ;     //monitor to indicate that task can be added
    private static Object notempty ;    //monitor to indicate that task can be removed
    private static Object arrayInUse;   //monitor lock for array operations

    private Task[] blockingQueue;

    private static BlockingQueue instance;

    public BlockingQueue() throws InterruptedException
    {
        this.isArrayActive = false;
        this.headIndex=-1;
        this.tailIndex=-1;

        notfull = new Object();     //monitor to indicate that task can be added
        notempty = new Object();    //monitor to indicate that task can be removed
        arrayInUse = new Object();  //lock for array operations

        blockingQueue = new Task[queueSize];   //Instantiating queue (array type)
    }

    public void put(Task task) throws InterruptedException {

        //case when queue is full - if tail index incremented by 1 it will go to head index
        if ((this.tailIndex + 1) % this.queueSize == this.headIndex) {
            synchronized (this.notfull) {
                this.notfull.wait();
            }
        }

        //locking array access to a single Thread
        if (isArrayActive) {
            synchronized (this.arrayInUse) {
                this.arrayInUse.wait();
            }
            this.isArrayActive = true;
        }

        if (this.isQueueEmpty()) {  //head and tail will be the same if array was initially empty
            this.headIndex++;
            this.tailIndex++;
            this.blockingQueue[this.tailIndex] = task;
        }
        else {
            this.tailIndex=(this.tailIndex+1)%this.queueSize;  //shifting tail +1 or moving to 0 if reached max size (0 index should be available)
            this.blockingQueue[this.tailIndex] = task;         //latest inserted task goes to tail
        }

        //unlocking array access
        if (isArrayActive) {
            synchronized (this.arrayInUse) {
                this.arrayInUse.notify();
            }
            this.isArrayActive=false;
        }

        //Task have been added to the queue - queue is not empty
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
        }

        //locking array access toa single Thread
        if (isArrayActive) {
            synchronized (this.arrayInUse) {
                this.arrayInUse.wait();
            }
            this.isArrayActive = true;
        }

        if ( this.headIndex == this.tailIndex) {
            returningTask = this.blockingQueue[ this.headIndex];
            this.headIndex = -1;
            this.tailIndex = -1;

        } else {
            returningTask = this.blockingQueue[ this.headIndex];
            this.headIndex=( this.headIndex+1)%this.queueSize;
        }

        //unlocking array access
        if (isArrayActive) {
            synchronized (this.arrayInUse) {
                this.arrayInUse.notify();
            }
            this.isArrayActive=false;
        }

        synchronized (this.notfull)
        {
            this.notfull.notify();  //Task taken away - array is not full
        }

        return returningTask;
    }

    //if both tail and head points to non existing index - queue is empty
    private boolean isQueueEmpty()
    {
        return (this.headIndex == -1 && this.tailIndex == -1);
    }

}
