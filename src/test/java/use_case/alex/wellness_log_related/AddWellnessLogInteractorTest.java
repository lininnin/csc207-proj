package use_case.alex.wellness_log_related;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.WellnessLogEntry.Levels;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import entity.Alex.WellnessLogEntry.WellnessLogEntryFactoryInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.alex.wellness_log_related.add_wellnessLog.*;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class AddWellnessLogInteractorTest {

    private AddWellnessLogDataAccessInterf mockDAO;
    private WellnessLogEntryFactoryInterf mockFactory;
    private AddWellnessLogOutputBoundary mockPresenter;
    private AddWellnessLogInteractor interactor;

    @BeforeEach
    void setUp() {
        mockDAO = mock(AddWellnessLogDataAccessInterf.class);
        mockFactory = mock(WellnessLogEntryFactoryInterf.class);
        mockPresenter = mock(AddWellnessLogOutputBoundary.class);
        interactor = new AddWellnessLogInteractor(mockDAO, mockFactory, mockPresenter);
    }

    @Test
    void testExecute_successfulCreation_shouldCallSaveAndPresenter() {
        // Arrange
        LocalDateTime time = LocalDateTime.of(2025, 8, 8, 12, 0);
        Levels stress = Levels.FIVE;
        Levels energy = Levels.SIX;
        Levels fatigue = Levels.THREE;
        MoodLabel mood = new MoodLabel.Builder("Happy")
                .type(MoodLabel.Type.Positive)
                .build();
        String note = "Feeling great!";

        AddWellnessLogInputData input = new AddWellnessLogInputData(
                time, stress, energy, fatigue, mood, note
        );

        // ✅ 使用链式 builder 构造实体（修正点）
        WellnessLogEntry expectedEntry = new WellnessLogEntry.Builder()
                .time(time)
                .stressLevel(stress)
                .energyLevel(energy)
                .fatigueLevel(fatigue)
                .moodLabel(mood)
                .userNote(note)
                .build();

        when(mockFactory.create(time, stress, energy, fatigue, mood, note))
                .thenReturn(expectedEntry);

        // Act
        interactor.execute(input);

        // Assert
        verify(mockDAO).save(expectedEntry);
        verify(mockPresenter).prepareSuccessView(
                argThat(output -> output.isSuccess()
                        && output.getSavedEntry().equals(expectedEntry)
                        && output.getMessage().contains("successfully"))
        );
    }

    @Test
    void testExecute_factoryThrowsException_shouldCallFailView() {
        // Arrange
        LocalDateTime time = LocalDateTime.now();
        Levels stress = Levels.NINE;
        Levels energy = Levels.ONE;
        Levels fatigue = Levels.SEVEN;
        MoodLabel mood = new MoodLabel.Builder("Anxious")
                .type(MoodLabel.Type.Negative)
                .build();
        String note = "Burned out.";

        AddWellnessLogInputData input = new AddWellnessLogInputData(
                time, stress, energy, fatigue, mood, note
        );

        when(mockFactory.create(any(), any(), any(), any(), any(), anyString()))
                .thenThrow(new RuntimeException("Factory error"));

        // Act
        interactor.execute(input);

        // Assert
        verify(mockPresenter).prepareFailView(
                argThat(msg -> msg.contains("Failed to add log"))
        );
        verify(mockDAO, never()).save(any());
    }
}

