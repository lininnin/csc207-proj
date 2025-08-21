package use_case.generate_feedback;

import java.util.List;

import entity.Angela.DailyLog;

/**
 * Port that defines how the Generate Feedback use case can request
 * GPT-ready prompts to be constructed from domain data.
 */
public interface GptPromptPort {

    /**
     * Builds a prompt for general weekly analysis.
     * @param logs the past week's logs
     * @return a formatted prompt string suitable for sending to GPT
     */
    String buildAnalysis(List<DailyLog> logs);

    /**
     * Builds a prompt for Bayesian-style correlation analysis.
     * @param logs  the past week's logs
     * @return a formatted prompt string suitable for sending to GPT
     */
    String buildCorrelation(List<DailyLog> logs);

    /**
     * Builds a prompt for generating recommendations.
     * @param analysisJson the JSON returned from GPT's
     *                     analysis phase, never {@code null}
     * @return a formatted prompt string suitable for sending to GPT
     */
    String buildRecommendation(String analysisJson);
}
