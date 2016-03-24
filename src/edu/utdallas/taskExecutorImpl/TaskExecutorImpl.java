package edu.utdallas.taskExecutorImpl;

import edu.utdallas.blockingFIFO.BlockingQueue;
import edu.utdallas.taskExecutor.Task;
import edu.utdallas.taskExecutor.TaskExecutor;

import java.util.ArrayList;
import java.util.List;


public class TaskExecutorImpl implements TaskExecutor
{

	private List<Thread> runnerPool;

	public TaskExecutorImpl(int poolSize)
	{
		runnerPool = new ArrayList<Thread>(poolSize);

		for(int i = 0; i < poolSize; i++)
		{
			Thread t = new Thread(new TaskRunner());
			runnerPool.add(t);
			runnerPool.get(i).start();
		}

	}

	@Override
	public void addTask(Task task)
	{
		try {
			BlockingQueue.getInstance().put(task);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
