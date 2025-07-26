package interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.delete_wellnessLog;

import interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogState;
import interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogViewModel;
import use_case.Alex.WellnessLog_related.todays_wellnessLog_module.delete_wellnesslog.DeleteWellnessLogOutputBoundary;
import use_case.Alex.WellnessLog_related.todays_wellnessLog_module.delete_wellnesslog.DeleteWellnessLogOutputData;
import use_case.Alex.WellnessLog_related.todays_wellnessLog_module.delete_wellnesslog.DeleteWellnessLogDataAccessInterf;

/**
 * Presenter for the DeleteWellnessLog use case.
 * Updates both DeleteWellnessLogViewModel and TodaysWellnessLogViewModel.
 */
public class DeleteWellnessLogPresenter implements DeleteWellnessLogOutputBoundary {

    private final DeleteWellnessLogViewModel deleteViewModel;
    private final TodaysWellnessLogViewModel todaysViewModel;
    private final DeleteWellnessLogDataAccessInterf dataAccess;

    public DeleteWellnessLogPresenter(DeleteWellnessLogViewModel deleteViewModel,
                                      TodaysWellnessLogViewModel todaysViewModel,
                                      DeleteWellnessLogDataAccessInterf dataAccess) {
        this.deleteViewModel = deleteViewModel;
        this.todaysViewModel = todaysViewModel;
        this.dataAccess = dataAccess;
    }

    @Override
    public void prepareSuccessView(DeleteWellnessLogOutputData outputData) {
        // 更新 Delete 状态
        DeleteWellnessLogState deleteState = new DeleteWellnessLogState();
        deleteState.setDeletedLogId(outputData.getDeletedLogId());
        deleteState.setDeleteError("");
        deleteViewModel.setState(deleteState);
        deleteViewModel.firePropertyChanged(DeleteWellnessLogViewModel.DELETE_WELLNESS_LOG_PROPERTY);

        // 同时刷新今日列表
        TodaysWellnessLogState newState = new TodaysWellnessLogState();
        newState.setEntries(dataAccess.getTodaysWellnessLogEntries());
        todaysViewModel.setState(newState);
        todaysViewModel.firePropertyChanged(TodaysWellnessLogViewModel.TODAYS_WELLNESS_LOG_PROPERTY);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        DeleteWellnessLogState errorState = new DeleteWellnessLogState();
        errorState.setDeleteError(errorMessage);
        deleteViewModel.setState(errorState);
        deleteViewModel.firePropertyChanged(DeleteWellnessLogViewModel.DELETE_WELLNESS_LOG_PROPERTY);
    }
}



