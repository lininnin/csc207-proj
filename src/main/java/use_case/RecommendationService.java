package use_case;

import entity.DailyLog;

import java.util.List;

public interface RecommendationService {

    public String generateRecommendation(List<DailyLog> log);
}
