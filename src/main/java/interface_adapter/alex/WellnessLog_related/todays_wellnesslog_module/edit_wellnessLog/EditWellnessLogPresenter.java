package interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.edit_wellnessLog;

import entity.Alex.WellnessLogEntry.WellnessLogEntryInterf;
import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogState;
import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogViewModel;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import use_case.alex.wellness_log_related.todays_wellness_log_module.edit_wellnesslog.EditWellnessLogDataAccessInterf;
import use_case.alex.wellness_log_related.todays_wellness_log_module.edit_wellnesslog.EditWellnessLogOutputBoundary;
import use_case.alex.wellness_log_related.todays_wellness_log_module.edit_wellnesslog.EditWellnessLogOutputData;

import java.util.List;

/**
 * Presenter for the EditWellnessLog use case.
 * Updates both EditWellnessLogViewModel and TodaysWellnessLogViewModel.
 */
public class EditWellnessLogPresenter implements EditWellnessLogOutputBoundary {

    private final EditWellnessLogViewModel editViewModel;
    private final TodaysWellnessLogViewModel todaysViewModel;
    private final EditWellnessLogDataAccessInterf dataAccess;
    private TodaySoFarController todaySoFarController;

    public EditWellnessLogPresenter(EditWellnessLogViewModel editViewModel,
                                    TodaysWellnessLogViewModel todaysViewModel,
                                    EditWellnessLogDataAccessInterf dataAccess) {
        this.editViewModel = editViewModel;
        this.todaysViewModel = todaysViewModel;
        this.dataAccess = dataAccess;
    }
    
    public void setTodaySoFarController(TodaySoFarController controller) {
        this.todaySoFarController = controller;
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
        System.out.println("✅ Presenter: updating TodaysWellnessLogViewModel");
        List<WellnessLogEntryInterf> updatedList = dataAccess.getTodaysWellnessLogEntries();
        TodaysWellnessLogState newTodayState = new TodaysWellnessLogState();
        newTodayState.setEntries(updatedList);
        todaysViewModel.setState(newTodayState);
        todaysViewModel.fireStateChanged();
        
        // Refresh Today So Far panel if controller is available
        if (todaySoFarController != null) {
            todaySoFarController.refresh();
        }
    }

    @Override
    public void prepareFailView(String errorMessage) {
        EditWellnessLogState newEditState = new EditWellnessLogState();
        newEditState.setErrorMessage(errorMessage);
        editViewModel.setState(newEditState);
        editViewModel.fireStateChanged();
    }
}


