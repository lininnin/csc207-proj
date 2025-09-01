package interface_adapter.alex.WellnessLog_related.new_wellness_log;

import entity.alex.MoodLabel.MoodLabelInterf;
import entity.alex.WellnessLogEntry.Levels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import use_case.alex.wellness_log_related.add_wellnessLog.AddWellnessLogInputBoundary;
import use_case.alex.wellness_log_related.add_wellnessLog.AddWellnessLogInputData;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for AddWellnessLogController.
 * Tests controller responsibilities in Clean Architecture.
 */
class AddWellnessLogControllerTest {

    private AddWellnessLogController controller;
    private AddWellnessLogInputBoundary mockInteractor;
    private MoodLabelInterf mockMoodLabel;

    @BeforeEach
    void setUp() {
        mockInteractor = mock(AddWellnessLogInputBoundary.class);
        mockMoodLabel = mock(MoodLabelInterf.class);
        controller = new AddWellnessLogController(mockInteractor);
    }

    @Test
    @DisplayName("Should create input data and call interactor with valid parameters")
    void testExecuteWithValidParameters() {
        // Given
        LocalDateTime time = LocalDateTime.of(2024, 1, 15, 10, 30);
        Levels stressLevel = Levels.FIVE;
        Levels energyLevel = Levels.EIGHT;
        Levels fatigueLevel = Levels.TWO;
        String userNote = "Feeling good today";

        // When
        controller.execute(time, stressLevel, energyLevel, fatigueLevel, mockMoodLabel, userNote);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTime().equals(time) &&
                inputData.getStressLevel() == stressLevel &&
                inputData.getEnergyLevel() == energyLevel &&
                inputData.getFatigueLevel() == fatigueLevel &&
                inputData.getMoodLabel() == mockMoodLabel &&
                inputData.getUserNote().equals(userNote)
        ));
    }

    @Test
    @DisplayName("Should handle null user note")
    void testExecuteWithNullUserNote() {
        // Given
        LocalDateTime time = LocalDateTime.now();
        Levels stressLevel = Levels.TWO;
        Levels energyLevel = Levels.FIVE;
        Levels fatigueLevel = Levels.EIGHT;

        // When
        controller.execute(time, stressLevel, energyLevel, fatigueLevel, mockMoodLabel, null);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getUserNote() == null
        ));
    }

    @Test
    @DisplayName("Should handle empty user note")
    void testExecuteWithEmptyUserNote() {
        // Given
        LocalDateTime time = LocalDateTime.now();
        Levels stressLevel = Levels.NINE;
        Levels energyLevel = Levels.THREE;
        Levels fatigueLevel = Levels.SIX;
        String emptyNote = "";

        // When
        controller.execute(time, stressLevel, energyLevel, fatigueLevel, mockMoodLabel, emptyNote);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getUserNote().equals(emptyNote)
        ));
    }

    @Test
    @DisplayName("Should handle all level combinations")
    void testExecuteWithAllLevelCombinations() {
        LocalDateTime time = LocalDateTime.now();

        // Test all level combinations
        for (Levels stress : Levels.values()) {
            for (Levels energy : Levels.values()) {
                for (Levels fatigue : Levels.values()) {
                    controller.execute(time, stress, energy, fatigue, mockMoodLabel, "Test note");
                }
            }
        }

        // Verify interactor was called for each combination (10^3 = 1000 times)
        verify(mockInteractor, times(1000)).execute(any(AddWellnessLogInputData.class));
    }

    @Test
    @DisplayName("Should pass through all parameters unchanged")
    void testParameterPassthrough() {
        // Given
        LocalDateTime specificTime = LocalDateTime.of(2024, 12, 25, 14, 45, 30);
        Levels specificStress = Levels.TEN;
        Levels specificEnergy = Levels.ONE;
        Levels specificFatigue = Levels.SEVEN;
        String specificNote = "Christmas wellness check";

        // When
        controller.execute(specificTime, specificStress, specificEnergy, specificFatigue, mockMoodLabel, specificNote);

        // Then - Verify exact parameter matching
        verify(mockInteractor).execute(argThat(inputData -> {
            return inputData.getTime().equals(specificTime) &&
                   inputData.getStressLevel() == specificStress &&
                   inputData.getEnergyLevel() == specificEnergy &&
                   inputData.getFatigueLevel() == specificFatigue &&
                   inputData.getMoodLabel() == mockMoodLabel &&
                   inputData.getUserNote().equals(specificNote);
        }));
    }

    @Test
    @DisplayName("Should create new input data object for each call")
    void testCreatesNewInputDataForEachCall() {
        // Given
        LocalDateTime time = LocalDateTime.now();
        Levels stress = Levels.FIVE;
        Levels energy = Levels.FIVE;
        Levels fatigue = Levels.FIVE;

        // When - Make multiple calls
        controller.execute(time, stress, energy, fatigue, mockMoodLabel, "Note 1");
        controller.execute(time, stress, energy, fatigue, mockMoodLabel, "Note 2");

        // Then - Verify interactor called twice
        verify(mockInteractor, times(2)).execute(any(AddWellnessLogInputData.class));
    }

    @Test
    @DisplayName("Should handle null mood label")
    void testExecuteWithNullMoodLabel() {
        // Given
        LocalDateTime time = LocalDateTime.now();
        Levels stress = Levels.TWO;
        Levels energy = Levels.NINE;
        Levels fatigue = Levels.ONE;

        // When
        controller.execute(time, stress, energy, fatigue, null, "Test note");

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getMoodLabel() == null
        ));
    }

    @Test
    @DisplayName("Should handle past, present and future timestamps")
    void testExecuteWithDifferentTimeStamps() {
        LocalDateTime past = LocalDateTime.now().minusDays(1);
        LocalDateTime present = LocalDateTime.now();
        LocalDateTime future = LocalDateTime.now().plusDays(1);

        // When
        controller.execute(past, Levels.TWO, Levels.TWO, Levels.TWO, mockMoodLabel, "Past");
        controller.execute(present, Levels.FIVE, Levels.FIVE, Levels.FIVE, mockMoodLabel, "Present");
        controller.execute(future, Levels.NINE, Levels.NINE, Levels.NINE, mockMoodLabel, "Future");

        // Then
        verify(mockInteractor, times(3)).execute(any(AddWellnessLogInputData.class));
        verify(mockInteractor).execute(argThat(data -> data.getTime().equals(past)));
        verify(mockInteractor).execute(argThat(data -> data.getTime().equals(present)));
        verify(mockInteractor).execute(argThat(data -> data.getTime().equals(future)));
    }
}