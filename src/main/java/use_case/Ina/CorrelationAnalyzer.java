package use_case.Ina;

import entity.Angela.DailyLog;

import java.util.List;

public interface CorrelationAnalyzer {
    RegressionResult analyze(List<DailyLog> logs);
}
