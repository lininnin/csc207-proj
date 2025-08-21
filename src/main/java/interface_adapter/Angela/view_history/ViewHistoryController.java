package interface_adapter.Angela.view_history;

import use_case.Angela.view_history.ViewHistoryInputBoundary;
import use_case.Angela.view_history.ViewHistoryInputData;

import java.time.LocalDate;

/**
 * Controller for the view history use case.
 */
public class ViewHistoryController {
    private final ViewHistoryInputBoundary interactor;
    
    public ViewHistoryController(ViewHistoryInputBoundary interactor) {
        this.interactor = interactor;
    }
    
    /**
     * Loads history for a specific date.
     * @param date The date to load history for
     */
    public void loadHistory(LocalDate date) {
        ViewHistoryInputData inputData = new ViewHistoryInputData(date);
        interactor.execute(inputData);
    }
    
    /**
     * Loads the list of available history dates.
     */
    public void loadAvailableDates() {
        interactor.loadAvailableDates();
    }
    
    /**
     * Exports history for a specific date.
     * @param date The date to export
     */
    public void exportHistory(LocalDate date) {
        ViewHistoryInputData inputData = new ViewHistoryInputData(date);
        interactor.exportHistory(inputData);
    }
}