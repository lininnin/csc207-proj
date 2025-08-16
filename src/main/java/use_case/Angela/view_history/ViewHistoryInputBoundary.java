package use_case.Angela.view_history;

/**
 * Input boundary interface for the view history use case.
 */
public interface ViewHistoryInputBoundary {
    /**
     * Executes the view history use case.
     * @param inputData Contains the date to view history for
     */
    void execute(ViewHistoryInputData inputData);
    
    /**
     * Loads the list of available history dates.
     */
    void loadAvailableDates();
    
    /**
     * Exports the history data for a specific date.
     * @param date The date to export
     */
    void exportHistory(ViewHistoryInputData date);
}