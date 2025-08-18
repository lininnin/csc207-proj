package entity.feedback_entry;

import java.time.LocalDate;

public interface FeedbackEntryFactoryInterf {
    /**
     * Create a new feedback entry.
     * @param date the date that this entry is created (some Monday)
     * @param analysis the analysis content generated for the week before entry is created
     * @param correlationJson the correlation content generated for the week before entry is created
     * @param recommendations the recommendations for user wellness for future
     * @return a new feedback entry containing date, analysis, correlation and recommendations
     */
    FeedbackEntryInterf create(LocalDate date, String analysis, String correlationJson, String recommendations);
}
