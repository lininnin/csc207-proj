package entity.Angela.Task;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Info.Info;
public class TaskFactory implements TaskFactoryInterf {

    /**
     * Creates a new Task with required Info and BeginAndDueDates, and optional Priority.
     *
     * @param info The Info object containing name, description, category, etc.
     * @param beginAndDueDates The BeginAndDueDates object specifying start and end dates.
     * @param priority Optional priority for the task (can be null).
     * @return a new Task instance
     * @throws IllegalArgumentException if info or beginAndDueDates is null
     */
    public Task createTask(Info info, BeginAndDueDates beginAndDueDates, Task.Priority priority) {
        if (info == null) {
            throw new IllegalArgumentException("Info is required");
        }
        if (beginAndDueDates == null) {
            throw new IllegalArgumentException("BeginAndDueDates is required");
        }

        Task.Builder builder = new Task.Builder(info).beginAndDueDates(beginAndDueDates);
        if (priority != null) {
            builder.priority(priority);
        }
        return builder.build();
    }

    /**
     * Overload for creating a Task without setting a priority.
     *
     * @param info The Info object.
     * @param beginAndDueDates The BeginAndDueDates object.
     * @return a new Task instance
     */
    public Task createTask(Info info, BeginAndDueDates beginAndDueDates) {
        return createTask(info, beginAndDueDates, null);
    }
}

