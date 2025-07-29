package use_case.Ina;

import use_case.repository.DailyLogRepository;

public class AnalyzeWellnessVSCompletionUseCase {
    private final DailyLogRepository dailyLogRepository;
    private final CorrelationAnalyzer analyzer;
    private final RegressionResultRepository resultRepository; // nullable


    public AnalyzeWellnessVSCompletionUseCase(DailyLogRepository dailyLogRepository,
                                              CorrelationAnalyzer analyzer,
                                              RegressionResultRepository resultRepository) {
        this.dailyLogRepository = dailyLogRepository;
        this.analyzer = analyzer;
        this.resultRepository = resultRepository;
    }
}
