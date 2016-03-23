package edu.utdallas.taskExecutorImpl;

import edu.utdallas.taskExecutor.Task;

/**
 * Created by Anton on 3/22/2016.
 */
public class TaskImpl implements Task
{
    private String name;

    public TaskImpl(){}

    public TaskImpl(String name) {
        this.name = name;
    }

    @Override
    public void execute() {
        System.out.println("Executing Task:" + this.name);
    }

    @Override
    public String getName() {
        return this.name;
    }
}
