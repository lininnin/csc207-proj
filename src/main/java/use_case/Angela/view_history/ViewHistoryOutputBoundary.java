package use_case.Angela.view_history;

import java.time.LocalDate;
import java.util.List;

/**
 * Output boundary interface for the view history use case.
 */
public interface ViewHistoryOutputBoundary {
    /**
     * Prepares the success view with history data.
     * @param outputData The history data to display
     */
    void prepareSuccessView(ViewHistoryOutputData outputData);
    
    /**
     * Prepares the failure view with an error message.
     * @param error The error message to display
     */
    void prepareFailView(String error);
    
    /**
     * Updates the list of available history dates.
     * @param availableDates List of dates that have history data
     */
    void presentAvailableDates(List<LocalDate> availableDates);
    
    /**
     * Presents the export success message.
     * @param filePath The path where the file was exported
     */
    void presentExportSuccess(String filePath);
    
    /**
     * Presents the export failure message.
     * @param error The error message
     */
    void presentExportFailure(String error);
}