package use_case.feedback_Entry;

import entity.Ina.FeedbackEntry;

import java.time.LocalDate;

public interface FeedbackEntryRepository {
    FeedbackEntry getByDate(LocalDate date);
}
