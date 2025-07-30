package use_case.Angela.task.delete;

/**
 * Output data for the delete task use case.
 */
public class DeleteTaskOutputData {
    private final String deletedTaskId;
    private final String message;
    private final boolean deletedFromBoth;

    public DeleteTaskOutputData(String deletedTaskId, String message, boolean deletedFromBoth) {
        this.deletedTaskId = deletedTaskId;
        this.message = message;
        this.deletedFromBoth = deletedFromBoth;
    }

    public String getDeletedTaskId() {
        return deletedTaskId;
    }

    public String getMessage() {
        return message;
    }

    public boolean isDeletedFromBoth() {
        return deletedFromBoth;
    }
}