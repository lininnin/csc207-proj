package use_case.Ina;

public interface GPTService {
    String callGeneralAnalysis(String prompt);
    String callCorrelationBayes(String prompt);
    String callRecommendation(String prompt);
}
