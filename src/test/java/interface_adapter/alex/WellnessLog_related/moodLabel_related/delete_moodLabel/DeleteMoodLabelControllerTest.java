package interface_adapter.alex.WellnessLog_related.moodLabel_related.delete_moodLabel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import use_case.alex.wellness_log_related.moodlabel_related.delete_moodLabel.DeleteMoodLabelInputBoundary;
import use_case.alex.wellness_log_related.moodlabel_related.delete_moodLabel.DeleteMoodLabelInputData;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for DeleteMoodLabelController.
 * Tests controller responsibilities in Clean Architecture.
 */
class DeleteMoodLabelControllerTest {

    private DeleteMoodLabelController controller;
    private DeleteMoodLabelInputBoundary mockInteractor;

    @BeforeEach
    void setUp() {
        mockInteractor = mock(DeleteMoodLabelInputBoundary.class);
        controller = new DeleteMoodLabelController(mockInteractor);
    }

    @Test
    @DisplayName("Should create input data and call interactor with valid mood label name")
    void testDeleteWithValidMoodLabelName() {
        // Given
        String moodLabelName = "Happy";

        // When
        controller.delete(moodLabelName);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                moodLabelName.equals(inputData.getMoodLabelName())
        ));
    }

    @Test
    @DisplayName("Should throw exception for null mood label name")
    void testDeleteWithNullMoodLabelName() {
        // Given
        String moodLabelName = null;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            controller.delete(moodLabelName);
        });
    }

    @Test
    @DisplayName("Should throw exception for empty mood label name")
    void testDeleteWithEmptyMoodLabelName() {
        // Given
        String moodLabelName = "";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            controller.delete(moodLabelName);
        });
    }

    @Test
    @DisplayName("Should throw exception for whitespace-only mood label name")
    void testDeleteWithWhitespaceOnlyMoodLabelName() {
        // Given
        String moodLabelName = "   ";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            controller.delete(moodLabelName);
        });
    }

    @Test
    @DisplayName("Should trim whitespace from valid mood label name")
    void testDeleteWithLeadingTrailingWhitespace() {
        // Given
        String moodLabelNameWithSpaces = "  Happy  ";
        String expectedTrimmedName = "Happy";

        // When
        controller.delete(moodLabelNameWithSpaces);

        // Then - Should trim the spaces and pass the trimmed name
        verify(mockInteractor).execute(argThat(inputData ->
                expectedTrimmedName.equals(inputData.getMoodLabelName())
        ));
    }

    @Test
    @DisplayName("Should handle mood label names with special characters")
    void testDeleteWithSpecialCharacterMoodLabelName() {
        // Given
        String moodLabelName = "Very Happy! 😊";

        // When
        controller.delete(moodLabelName);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                moodLabelName.equals(inputData.getMoodLabelName())
        ));
    }

    @Test
    @DisplayName("Should handle long mood label names")
    void testDeleteWithLongMoodLabelName() {
        // Given
        String moodLabelName = "This is a very long mood label name that exceeds normal expectations";

        // When
        controller.delete(moodLabelName);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                moodLabelName.equals(inputData.getMoodLabelName())
        ));
    }

    @Test
    @DisplayName("Should handle case-sensitive mood label names")
    void testDeleteWithCaseSensitiveMoodLabelNames() {
        // Given
        String lowerCase = "happy";
        String upperCase = "HAPPY";
        String mixedCase = "Happy";

        // When
        controller.delete(lowerCase);
        controller.delete(upperCase);
        controller.delete(mixedCase);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                lowerCase.equals(inputData.getMoodLabelName())
        ));
        verify(mockInteractor).execute(argThat(inputData ->
                upperCase.equals(inputData.getMoodLabelName())
        ));
        verify(mockInteractor).execute(argThat(inputData ->
                mixedCase.equals(inputData.getMoodLabelName())
        ));
    }

    @Test
    @DisplayName("Should create new input data object for each call")
    void testCreatesNewInputDataForEachCall() {
        // When - Make multiple calls
        controller.delete("Happy");
        controller.delete("Sad");

        // Then - Verify interactor called twice with different data
        verify(mockInteractor, times(2)).execute(any(DeleteMoodLabelInputData.class));
    }

    @Test
    @DisplayName("Should pass through mood label name unchanged")
    void testParameterPassthrough() {
        // Given
        String specificMoodLabelName = "Excited";

        // When
        controller.delete(specificMoodLabelName);

        // Then - Verify exact parameter matching
        verify(mockInteractor).execute(argThat(inputData ->
                specificMoodLabelName.equals(inputData.getMoodLabelName())
        ));
    }

    @Test
    @DisplayName("Should handle mood label names with numbers")
    void testDeleteWithNumericMoodLabelName() {
        // Given
        String moodLabelName = "Level5Happy";

        // When
        controller.delete(moodLabelName);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                moodLabelName.equals(inputData.getMoodLabelName())
        ));
    }

    @Test
    @DisplayName("Should handle mood label names with unicode characters")
    void testDeleteWithUnicodeMoodLabelName() {
        // Given
        String moodLabelName = "快乐"; // Chinese characters for "happy"

        // When
        controller.delete(moodLabelName);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                moodLabelName.equals(inputData.getMoodLabelName())
        ));
    }
}