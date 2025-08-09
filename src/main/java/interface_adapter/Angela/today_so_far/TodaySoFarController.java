package interface_adapter.Angela.today_so_far;

import use_case.Angela.today_so_far.TodaySoFarInputBoundary;

/**
 * Controller for the Today So Far panel.
 */
public class TodaySoFarController {
    
    private final TodaySoFarInputBoundary inputBoundary;
    
    public TodaySoFarController(TodaySoFarInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }
    
    /**
     * Refreshes the Today So Far panel with current data.
     */
    public void refresh() {
        inputBoundary.refreshTodaySoFar();
    }
}