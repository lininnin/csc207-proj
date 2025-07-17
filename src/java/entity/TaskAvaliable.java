package entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents the list of tasks available to be added to this day.
 */
public class TaskAvaliable {
    private List<Info> taskAvaliable;

    /**
     * Constructs a TaskAvaliable object with the given list of tasks.
     *
     * @param taskAvaliable the initial list of available tasks
     */
    public TaskAvaliable(List<Info> taskAvaliable) {
        this.taskAvaliable = taskAvaliable;
    }

    /**
     * Returns the list of tasks available to be added.
     *
     * @return the list of available tasks
     */
    public List<Info> getTaskAvaliable() {
        return taskAvaliable;
    }

    /**
     * Adds a task to the list of available tasks.
     *
     * @param taskLabel the task to be added
     */
    public void add(Info taskLabel) {
        this.taskAvaliable.add(taskLabel);
    }
}
