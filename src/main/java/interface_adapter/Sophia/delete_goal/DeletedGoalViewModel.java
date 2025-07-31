package interface_adapter.Sophia.delete_goal;

import view.ViewModel;

public class DeletedGoalViewModel extends ViewModel {
    public static final String TITLE_LABEL = "Delete Goal";
    public static final String CONFIRM_MESSAGE = "Confirm Deletion";

    private DeletedGoalState state = new DeletedGoalState();

    public DeletedGoalViewModel() {
        super("delete goal");
    }

    public DeletedGoalState getState() {
        return state;
    }

    public void setState(DeletedGoalState state) {
        this.state = state;
    }
}