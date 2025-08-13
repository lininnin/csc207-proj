package use_case.generate_feedback;

import java.io.IOException;

public interface GptService {
    /**
     * Calls GPT to perform general weekly productivity and wellness analysis.
     * @param prompt the input prompt containing user data and analysis instructions
     * @return the GPT-generated Json string containing analysis
     * @throws IOException if there is issue connecting to GptService
     */
    String callGeneralAnalysis(String prompt) throws IOException;

    /**
     * Calls GPT to compute correlation between wellness variables and completion rate using bayesian inference.
     * @param prompt the input that contains user data and correlation instruction
     * @return GPT generated Json string containing correlation results
     * @throws IOException if there is issue connecting to GptService
     */
    String callCorrelationBayes(String prompt) throws IOException;

    /**
     * Calls GPT to generated recommendations based on provided analysis data from general analysis.
     * @param prompt the input containing analysis results and recommendation specifications
     * @return GPT generated json string containing recommendations
     * @throws IOException if there is issue connecting to GptService
     */
    String callRecommendation(String prompt) throws IOException;
}
