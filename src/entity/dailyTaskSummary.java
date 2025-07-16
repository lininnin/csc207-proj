import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Organizes tasks and goals into "Completed Today" and "To Do Today" lists.
 */
public class DailyTaskOrganizer {
    private final List<Task> completedToday = new ArrayList<>();
    private final List<Task> toDoToday = new ArrayList<>();
    private final LocalDate today = LocalDate.now();

    /**
     * Accepts a Task or Goal and sorts the internal lists accordingly.
     *
     * @param object Either a Task or a Goal
     */
    public void process(Object object) {
        if (object instanceof Task task) {
            handleTask(task);
        } else if (object instanceof Goal goal) {
            handleTask(goal.getTargetTask());
        }
    }

    /**
     * Organizes a task into completed or today's list.
     *
     * @param task Task to handle
     */
    private void handleTask(Task task) {
        BeginAndDueDates dates = task.getBeginAndDueDates();
        if (dates.getBeginDate().isEqual(today)) {
            if (task.isComplete()) {
                completedToday.add(task);
            } else {
                toDoToday.add(task);
            }
        }
    }

    /** @return List of tasks completed today */
    public List<Task> getCompletedToday() {
        return completedToday;
    }

    /** @return List of tasks to do today */
    public List<Task> getToDoToday() {
        return toDoToday;
    }
}

