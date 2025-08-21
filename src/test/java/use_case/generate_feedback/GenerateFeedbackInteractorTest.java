package use_case.generate_feedback;

import entity.Angela.DailyLog;
import entity.feedback_entry.FeedbackEntryInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import use_case.repository.DailyLogRepository;
import use_case.repository.FeedbackRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateFeedbackInteractorTest {

    @Mock DailyLogRepository dailyRepo;
    @Mock FeedbackRepository feedbackRepo;
    @Mock GptService gpt;
    @Mock GenerateFeedbackOutputBoundary output;

    @Captor ArgumentCaptor<LocalDate> dateCaptor;
    @Captor ArgumentCaptor<LocalDate> fromCaptor;
    @Captor ArgumentCaptor<LocalDate> toCaptor;
    @Captor ArgumentCaptor<GenerateFeedbackOutputData> outCaptor;

    GenerateFeedbackInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new GenerateFeedbackInteractor(dailyRepo, feedbackRepo, gpt, output);
    }

    @Test
    void execute_createsEntry_whenNotCached() throws IOException {
        // Arrange
        // 1) Not cached
        when(feedbackRepo.loadByDate(any(LocalDate.class))).thenReturn(null);

        // 2) Last week's logs
        when(dailyRepo.loadBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of());

        // 3) GPT calls
        String analysisJson = """
          {"analysis":"Weekly overview","extra_notes":"Hydrate more"}
        """;
        when(gpt.callGeneralAnalysis(anyString())).thenReturn(analysisJson);
        when(gpt.callCorrelationBayes(anyString())).thenReturn("{\"correlations\":[]}");
        when(gpt.callRecommendation(anyString())).thenReturn("Walk 20 minutes daily.");

        // Act
        interactor.execute();

        // Assert
        // A) We computed monday and looked for a cached entry
        verify(feedbackRepo).loadByDate(dateCaptor.capture());
        LocalDate mondayUsed = dateCaptor.getValue();
        assertEquals(java.time.DayOfWeek.MONDAY, mondayUsed.getDayOfWeek());

        // B) We loaded last week's range relative to that monday (Mon..Sun)
        verify(dailyRepo).loadBetween(fromCaptor.capture(), toCaptor.capture());
        assertEquals(mondayUsed.minusWeeks(1), fromCaptor.getValue());
        assertEquals(mondayUsed.minusDays(1), toCaptor.getValue());

        // C) We saved a newly created entry
        ArgumentCaptor<FeedbackEntryInterf> savedEntry = ArgumentCaptor.forClass(FeedbackEntryInterf.class);
        verify(feedbackRepo).save(savedEntry.capture());
        FeedbackEntryInterf entry = savedEntry.getValue();
        assertNotNull(entry);
        assertEquals(mondayUsed, entry.getDate());

        // Combined analysis should include extra notes appended
        String analysisCombined = entry.getAiAnalysis();
        assertTrue(analysisCombined.contains("Weekly overview"));
        assertTrue(analysisCombined.contains("Notes: Hydrate more"));

        // Correlation JSON and recommendation carried through
        assertEquals("{\"correlations\":[]}", entry.getCorrelationData());
        assertEquals("Walk 20 minutes daily.", entry.getRecommendations());

        // D) Presenter receives the same entry
        verify(output).present(outCaptor.capture());
        GenerateFeedbackOutputData presented = outCaptor.getValue();
        assertSame(entry, presented.getFeedbackEntry());

        // E) GPT was called 3 times with some prompts
        verify(gpt, times(1)).callGeneralAnalysis(anyString());
        verify(gpt, times(1)).callCorrelationBayes(anyString());
        verify(gpt, times(1)).callRecommendation(anyString());
    }

    @Test
    void execute_usesCachedEntry_whenPresent() {
        // Arrange
        FeedbackEntryInterf cached = mock(FeedbackEntryInterf.class);
        when(feedbackRepo.loadByDate(any(LocalDate.class))).thenReturn(cached);

        // Act
        interactor.execute();

        // Assert: short-circuits
        verify(output).present(outCaptor.capture());
        assertSame(cached, outCaptor.getValue().getFeedbackEntry());

        verifyNoInteractions(dailyRepo);
        try {
            verify(gpt, never()).callGeneralAnalysis(anyString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            verify(gpt, never()).callCorrelationBayes(anyString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            verify(gpt, never()).callRecommendation(anyString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        verify(feedbackRepo, never()).save(any());
    }
}
