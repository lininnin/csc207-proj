package interface_adapter.feedback_entry;

import entity.feedback_entry.FeedbackEntryInterf;

public interface FeedbackEntryView {
    /**
     * Display the inputted feedback entry to the user.
     * @param entry a feedback entry from feedback repository
     */
    void displayEntry(FeedbackEntryInterf entry);
}
