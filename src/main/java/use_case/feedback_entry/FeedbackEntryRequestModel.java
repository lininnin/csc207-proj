package use_case.feedback_entry;

import java.time.LocalDate;

/**
 * Request model for viewing a specific feedback entry.
 */
public class FeedbackEntryRequestModel {
    private final LocalDate date;

    public FeedbackEntryRequestModel(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
}
