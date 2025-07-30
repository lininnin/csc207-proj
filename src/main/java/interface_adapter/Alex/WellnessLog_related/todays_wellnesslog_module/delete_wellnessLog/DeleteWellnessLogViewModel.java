package interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.delete_wellnessLog;

import interface_adapter.ViewModel;

/**
 * ViewModel for deleting wellness log entries.
 * Notifies views when a log entry is successfully deleted or if there's an error.
 */
public class DeleteWellnessLogViewModel extends ViewModel<DeleteWellnessLogState> {

    public static final String DELETE_WELLNESS_LOG_PROPERTY = "deleteWellnessLog";

    public DeleteWellnessLogViewModel() {
        super("DeleteWellnessLogView");
        this.setState(new DeleteWellnessLogState());
    }
}

