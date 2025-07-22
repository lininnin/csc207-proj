package use_case;

import entity.DailyLog;

public interface CorrelationAnalyzer {
    String computeStressTaskCorrelation(DailyLog log);
}
