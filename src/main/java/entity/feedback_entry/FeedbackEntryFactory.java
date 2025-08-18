package entity.feedback_entry;

import java.time.LocalDate;

/**
 * Default factory for {@link FeedbackEntryInterf}.
 */
public class FeedbackEntryFactory implements FeedbackEntryFactoryInterf {

    /**
     * Creates a new feedback entry for a given week.
     *
     * @param date the Monday date associated with the entry (must not be null)
     * @param analysis analysis text (null becomes "")
     * @param correlationJson correlation JSON (null becomes "")
     * @param recommendations recommendations text (null becomes "")
     * @return a new {@link FeedbackEntryInterf}
     * @throws IllegalArgumentException if {@code date} is null
     */
    @Override
    public FeedbackEntryInterf create(
            final LocalDate date,
            final String analysis,
            final String correlationJson,
            final String recommendations) {

        if (date == null) {
            throw new IllegalArgumentException("date must not be null");
        }

        final String safeAnalysis = analysis == null ? "" : analysis;
        final String safeCorrelationJson = correlationJson == null ? "" : correlationJson;
        final String safeRecommendations = recommendations == null ? "" : recommendations;

        return new FeedbackEntry(date, safeAnalysis, safeCorrelationJson, safeRecommendations);
    }
}
