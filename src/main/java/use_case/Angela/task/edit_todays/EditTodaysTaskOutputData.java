package use_case.Angela.task.edit_todays;

/**
 * Output data for the edit today's task use case.
 */
public class EditTodaysTaskOutputData {
    private final String taskId;
    private final String taskName;
    private final boolean statusChanged;
    private final boolean isNowComplete;

    public EditTodaysTaskOutputData(String taskId, String taskName,
                                    boolean statusChanged, boolean isNowComplete) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.statusChanged = statusChanged;
        this.isNowComplete = isNowComplete;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public boolean isStatusChanged() {
        return statusChanged;
    }

    public boolean isNowComplete() {
        return isNowComplete;
    }
}