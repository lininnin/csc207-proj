package interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.edit_wellnessLog;

import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogState;
import interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogViewModel;
import use_case.Alex.WellnessLog_related.todays_wellnessLog_module.edit_wellnesslog.EditWellnessLogDataAccessInterf;
import use_case.Alex.WellnessLog_related.todays_wellnessLog_module.edit_wellnesslog.EditWellnessLogOutputBoundary;
import use_case.Alex.WellnessLog_related.todays_wellnessLog_module.edit_wellnesslog.EditWellnessLogOutputData;

import java.util.List;

/**
 * Presenter for the EditWellnessLog use case.
 * Updates both EditWellnessLogViewModel and TodaysWellnessLogViewModel.
 */
public class EditWellnessLogPresenter implements EditWellnessLogOutputBoundary {

    private final EditWellnessLogViewModel editViewModel;
    private final TodaysWellnessLogViewModel todaysViewModel;
    private final EditWellnessLogDataAccessInterf dataAccess;

    public EditWellnessLogPresenter(EditWellnessLogViewModel editViewModel,
                                    TodaysWellnessLogViewModel todaysViewModel,
                                    EditWellnessLogDataAccessInterf dataAccess) {
        this.editViewModel = editViewModel;
        this.todaysViewModel = todaysViewModel;
        this.dataAccess = dataAccess;
    }

    @Override
    public void prepareSuccessView(EditWellnessLogOutputData outputData) {
        // ✅ 更新 Edit VM
        EditWellnessLogState newEditState = new EditWellnessLogState();
        newEditState.setLogId(outputData.getLogId());
        newEditState.setErrorMessage("");
        editViewModel.setState(newEditState);
        editViewModel.fireStateChanged();

        // ✅ 同步更新 Today VM
        List<WellnessLogEntry> updatedList = dataAccess.getTodaysWellnessLogEntries();
        TodaysWellnessLogState newTodayState = new TodaysWellnessLogState();
        newTodayState.setEntries(updatedList);
        todaysViewModel.setState(newTodayState);
        todaysViewModel.fireStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        EditWellnessLogState newEditState = new EditWellnessLogState();
        newEditState.setErrorMessage(errorMessage);
        editViewModel.setState(newEditState);
        editViewModel.fireStateChanged();
    }
}


