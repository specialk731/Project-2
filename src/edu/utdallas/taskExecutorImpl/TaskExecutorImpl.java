package edu.utdallas.taskExecutorImpl;

import edu.utdallas.blockingFIFO.BlockingQueue;
import edu.utdallas.taskExecutor.Task;
import edu.utdallas.taskExecutor.TaskExecutor;

import java.util.ArrayList;
import java.util.List;


public class TaskExecutorImpl implements TaskExecutor
{

	private BlockingQueue blockingQueue;
	private List<Thread> runnerPool;

	public TaskExecutorImpl(int poolSize) {

		try {
			blockingQueue = new BlockingQueue();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		runnerPool = new ArrayList<Thread>(poolSize);

		for(int i = 0; i < poolSize; i++)
		{
			TaskRunner taskRunner =  new TaskRunner();
			taskRunner.setBlockingQueue(blockingQueue);

			Thread t = new Thread(taskRunner);
			runnerPool.add(t);
			runnerPool.get(i).start();
		}
	}

	@Override
	public void addTask(Task task)
	{
		try {

			this.blockingQueue.put(task);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
