package use_case;

import entity.DailyLog;

public class CorrelationAnalyzerUseCase implements CorrelationAnalyzer
{

    /**
     * @param log
     * @return a value that quantifies the correlation between stress and tasks
     */
    public String computeStressTaskCorrelation(DailyLog log) {
        return "0.5";
    }
}
