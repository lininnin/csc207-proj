package use_case.repository;

import java.time.LocalDate;
import java.util.List;

import entity.feedback_entry.FeedbackEntryInterf;

public interface FeedbackRepository {
    /**
     * Save the new feedback entry into the feedback repository.
     * @param entry entry to be saved into feedback repository
     */
    void save(FeedbackEntryInterf entry);

    /**
     * Retrieves the feedback entry generated on a specific date.
     * @param date the date of feedback entry to retrieve
     * @return the feedback entry generated on date.
     */
    FeedbackEntryInterf loadByDate(LocalDate date);

    /**
     * Retrieve all feedback generated.
     * @return A list containing all feedback entries generated
     */
    List<FeedbackEntryInterf> loadAll();
}
