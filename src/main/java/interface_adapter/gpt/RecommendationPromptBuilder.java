package interface_adapter.gpt;

import org.json.JSONObject;

/**
 * Builds a prompt for GPT to generate 3–5 actionable recommendations
 * using the JSON output from GeneralAnalysisPromptBuilder.
 */
public final class RecommendationPromptBuilder {

    private RecommendationPromptBuilder() {

    }

    /**
     * Build a prompt that returns recommendations based on the user's weekly productivity analysis.
     * @param analysisJson JSON string returned by GeneralAnalysisPromptBuilder call
     * @return prompt string for GPT
     */
    public static String buildPrompt(String analysisJson) {
        final JSONObject obj = new JSONObject(analysisJson);
        final String analysisTxt = obj.optString("analysis", "(no analysis)");
        final String dataGapsTxt = obj.optString("extra_notes", "None");

        return """
You are a productivity & wellness coach.  Provide concise, concrete advice only.

Below is this week's analysis%s
Write 3–5 imperative sentences of recommendations for next week.
Return in form of numerical list.

ANALYSIS:
%s

OUTPUT:
Plain text only – 3 to 5 sentences.
""".formatted(
                dataGapsTxt.isBlank() ? "" : " (and a note about data gaps)",
                analysisTxt.trim());
    }
}
