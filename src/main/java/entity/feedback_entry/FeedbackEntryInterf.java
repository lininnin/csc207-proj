package entity.feedback_entry;

import java.time.LocalDate;

public interface FeedbackEntryInterf {

    /**
     * Get the date that this feedback entry is generated.
     * @return a local date that this feedback is generated
     */
    LocalDate getDate();

    /**
     * Get the paragraph of wellness analysis generated from GPT.
     * @return a String of wellness and productivity analysis
     */
    String getAiAnalysis();

    /**
     * Get the result of bayes correlation analysis by GPT.
     * @return a string of correlation data between completion rate and wellness factors
     */
    String getCorrelationData();

    /**
     * Get a string literal of recommendation for wellness improvement by GPT.
     * @return a string of recommendations for improving wellness and productivity
     */
    String getRecommendations();
}
