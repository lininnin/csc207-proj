package use_case.feedback_entry;

import java.time.LocalDate;

import entity.feedback_entry.FeedbackEntry;

public interface FeedbackEntryRepository {
    /**
     * Retrieve the feedback entry generated on date.
     * @param date date of feedback entry we wish to retrieve
     * @return feedback entry generated on corresponding date
     */
    FeedbackEntry getByDate(LocalDate date);
}
