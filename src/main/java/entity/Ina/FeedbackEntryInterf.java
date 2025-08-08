package entity.Ina;

import java.time.LocalDate;

public interface FeedbackEntryInterf {
    LocalDate getDate();
    String getAiAnalysis();
    String getCorrelationData();
    String getRecommendations();
}
