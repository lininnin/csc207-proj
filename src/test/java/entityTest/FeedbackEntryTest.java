package entityTest;

import entity.feedback_entry.FeedbackEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackEntryTest {

    @Test
    void testConstructorAndGetters() {
        LocalDate date = LocalDate.of(2025, 7, 28);
        String aiAnalysis = "Analysis text";
        String correlationData = "Correlation info";
        String recommendations = "Do this, do that";

        FeedbackEntry entry = new FeedbackEntry(date, aiAnalysis, correlationData, recommendations);

        assertEquals(date, entry.getDate());
        assertEquals(aiAnalysis, entry.getAiAnalysis());
        assertEquals(correlationData, entry.getCorrelationData());
        assertEquals(recommendations, entry.getRecommendations());
    }

    @Test
    void testToStringIncludesAllFields() {
        LocalDate date = LocalDate.of(2025, 7, 28);
        String aiAnalysis = "AI summary";
        String correlationData = "Some correlation";
        String recommendations = "Take breaks";

        FeedbackEntry entry = new FeedbackEntry(date, aiAnalysis, correlationData, recommendations);
        String result = entry.toString();

        assertTrue(result.contains(date.toString()));
        assertTrue(result.contains(aiAnalysis));
        assertTrue(result.contains(correlationData));
        assertTrue(result.contains(recommendations));
    }
}
