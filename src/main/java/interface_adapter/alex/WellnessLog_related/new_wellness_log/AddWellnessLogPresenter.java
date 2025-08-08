package interface_adapter.alex.WellnessLog_related.new_wellness_log;

import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogViewModel;
import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogState;
import use_case.alex.WellnessLog_related.add_wellnessLog.AddWellnessLogOutputBoundary;
import use_case.alex.WellnessLog_related.add_wellnessLog.AddWellnessLogOutputData;

public class AddWellnessLogPresenter implements AddWellnessLogOutputBoundary {

    private final AddWellnessLogViewModel addViewModel;
    private final TodaysWellnessLogViewModel todaysViewModel;

    public AddWellnessLogPresenter(AddWellnessLogViewModel addViewModel,
                                   TodaysWellnessLogViewModel todaysViewModel) {
        this.addViewModel = addViewModel;
        this.todaysViewModel = todaysViewModel;
    }

    @Override
    public void prepareSuccessView(AddWellnessLogOutputData outputData) {
        AddWellnessLogState oldState = addViewModel.getState();
        AddWellnessLogState newState = new AddWellnessLogState();

        // 保留原有的 mood label 选项
        newState.setAvailableMoodLabels(oldState.getAvailableMoodLabels());
        newState.setSuccessMessage(outputData.getMessage());

        addViewModel.setState(newState);
        addViewModel.firePropertyChanged(AddWellnessLogViewModel.WELLNESS_LOG_PROPERTY);

        // 更新 today's log
        TodaysWellnessLogState currentState = todaysViewModel.getState();
        currentState.addEntry(outputData.getSavedEntry());

        todaysViewModel.setState(currentState);
        todaysViewModel.firePropertyChanged(TodaysWellnessLogViewModel.TODAYS_WELLNESS_LOG_PROPERTY);
    }

    @Override
    public void prepareFailView(String error) {
        AddWellnessLogState oldState = addViewModel.getState();
        AddWellnessLogState newState = new AddWellnessLogState();

        // 保留原有的 mood label 选项
        newState.setAvailableMoodLabels(oldState.getAvailableMoodLabels());
        newState.setErrorMessage(error);

        addViewModel.setState(newState);
        addViewModel.firePropertyChanged(AddWellnessLogViewModel.WELLNESS_LOG_PROPERTY);
    }
}



