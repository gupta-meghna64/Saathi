package com.example.saathi;

/**
 * Created by HP on 20-05-2017.
 */

public class Task {

    private String TaskName;
    private String TaskNumber;

    public Task()
    {

    }

    public void setTaskName(String name)
    {
        this.TaskName = name;
    }

    public String getTaskName()
    {
        return TaskName;
    }

    public void setTaskNumber(String number)
    {
        this.TaskNumber = number;
    }

    public String getTaskNumber()
    {
        return TaskNumber;
    }
}
