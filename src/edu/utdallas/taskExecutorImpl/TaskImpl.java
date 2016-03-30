package edu.utdallas.taskExecutorImpl;

import edu.utdallas.taskExecutor.Task;

/**
 * Created by Anton Rogozhnikov, Kevin Greenwald
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
    }

    @Override
    public String getName() {
        return this.name;
    }
}
