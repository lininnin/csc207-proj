package use_case.Angela.view_history;

import java.time.LocalDate;

/**
 * Input data for viewing historical "Today So Far" data.
 */
public class ViewHistoryInputData {
    private final LocalDate date;
    
    public ViewHistoryInputData(LocalDate date) {
        this.date = date;
    }
    
    public LocalDate getDate() {
        return date;
    }
}