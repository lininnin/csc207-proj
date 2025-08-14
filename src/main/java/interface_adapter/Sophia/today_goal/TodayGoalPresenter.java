package interface_adapter.Sophia.today_goal;

import interface_adapter.Angela.today_so_far.TodaySoFarController;
import use_case.goalManage.today_goal.TodayGoalOutputBoundary;
import use_case.goalManage.today_goal.TodayGoalOutputData;
import interface_adapter.Sophia.today_goal.TodayGoalsViewModel;

public class TodayGoalPresenter implements TodayGoalOutputBoundary {
    private final TodayGoalsViewModel todayGoalsViewModel;
    private TodaySoFarController todaySoFarController;

    public TodayGoalPresenter(TodayGoalsViewModel todayGoalsViewModel) {
        this.todayGoalsViewModel = todayGoalsViewModel;
    }
    
    public void setTodaySoFarController(TodaySoFarController controller) {
        this.todaySoFarController = controller;
    }

    @Override
    public void prepareSuccessView(TodayGoalOutputData outputData) {
        TodaysGoalsState currentState = todayGoalsViewModel.getState();
        currentState.setTodayGoals(outputData.getTodayGoals());
        todayGoalsViewModel.setState(currentState);
        todayGoalsViewModel.firePropertyChanged();
        
        // Refresh Today So Far panel if controller is available
        if (todaySoFarController != null) {
            todaySoFarController.refresh();
        }
    }

    @Override
    public void prepareFailView(String error) {
       System.err.println("Operation failed: " + error);

         }
}