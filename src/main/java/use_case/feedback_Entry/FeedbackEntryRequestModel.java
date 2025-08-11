package use_case.feedback_Entry;

import java.time.LocalDate;

public class FeedbackEntryRequestModel {
    private final LocalDate date;
    public FeedbackEntryRequestModel(LocalDate date) { this.date = date; }
    public LocalDate getDate() { return date; }
}
