package interface_adapter.Sophia.create_goal;

import interface_adapter.Angela.today_so_far.TodaySoFarController;
import use_case.goalManage.create_goal.CreateGoalOutputBoundary;
import use_case.goalManage.create_goal.CreateGoalOutputData;

public class CreateGoalPresenter implements CreateGoalOutputBoundary {
    private TodaySoFarController todaySoFarController;
    
    public void setTodaySoFarController(TodaySoFarController controller) {
        this.todaySoFarController = controller;
    }
    
    @Override
    public void presentSuccess(CreateGoalOutputData outputData) {
        // Update UI with success message
        System.out.println("Success: " + outputData.getStatusMessage());
        
        // Refresh Today So Far panel if controller is available
        if (todaySoFarController != null) {
            System.out.println("DEBUG: CreateGoalPresenter calling todaySoFarController.refresh()");
            todaySoFarController.refresh();
        } else {
            System.out.println("DEBUG: CreateGoalPresenter - todaySoFarController is null!");
        }
    }

    @Override
    public void presentError(String error) {
        // Show error in UI
        System.err.println("Error: " + error);
    }
}