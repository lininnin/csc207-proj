package use_case.generate_feedback;

import java.io.IOException;

public interface GPTService {
    String callGeneralAnalysis(String prompt) throws IOException;
    String callCorrelationBayes(String prompt) throws IOException;
    String callRecommendation(String prompt) throws IOException;
}
