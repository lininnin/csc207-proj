package interface_adapter.alex.WellnessLog_related.moodLabel_related.add_moodLabel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import use_case.alex.wellness_log_related.moodlabel_related.add_moodLabel.AddMoodLabelInputBoundary;
import use_case.alex.wellness_log_related.moodlabel_related.add_moodLabel.AddMoodLabelInputData;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for AddMoodLabelController.
 * Tests controller responsibilities in Clean Architecture.
 */
class AddMoodLabelControllerTest {

    private AddMoodLabelController controller;
    private AddMoodLabelInputBoundary mockInteractor;

    @BeforeEach
    void setUp() {
        mockInteractor = mock(AddMoodLabelInputBoundary.class);
        controller = new AddMoodLabelController(mockInteractor);
    }

    @Test
    @DisplayName("Should create input data and call interactor with valid mood label parameters")
    void testAddMoodLabelWithValidParameters() {
        // Given
        String name = "Happy";
        String type = "Positive";

        // When
        controller.addMoodLabel(name, type);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                name.equals(inputData.getMoodName()) && type.equals(inputData.getMoodType())
        ));
    }

    @Test
    @DisplayName("Should handle positive mood label type")
    void testAddPositiveMoodLabel() {
        // Given
        String name = "Excited";
        String type = "Positive";

        // When
        controller.addMoodLabel(name, type);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                name.equals(inputData.getMoodName()) && type.equals(inputData.getMoodType())
        ));
    }

    @Test
    @DisplayName("Should handle negative mood label type")
    void testAddNegativeMoodLabel() {
        // Given
        String name = "Sad";
        String type = "Negative";

        // When
        controller.addMoodLabel(name, type);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                name.equals(inputData.getMoodName()) && type.equals(inputData.getMoodType())
        ));
    }

    @Test
    @DisplayName("Should pass through name and type unchanged")
    void testParameterPassthrough() {
        // Given
        String name = "Anxious";
        String type = "Negative";

        // When
        controller.addMoodLabel(name, type);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                name.equals(inputData.getMoodName()) && type.equals(inputData.getMoodType())
        ));
    }

    @Test
    @DisplayName("Should create new input data object for each call")
    void testCreatesNewInputDataForEachCall() {
        // When - Make multiple calls
        controller.addMoodLabel("Happy", "Positive");
        controller.addMoodLabel("Sad", "Negative");

        // Then - Verify interactor called twice with different data
        verify(mockInteractor, times(2)).execute(any(AddMoodLabelInputData.class));
    }

    @Test
    @DisplayName("Should handle mood label names with special characters")
    void testAddMoodLabelWithSpecialCharacters() {
        // Given
        String name = "Very Happy! 😊";
        String type = "Positive";

        // When
        controller.addMoodLabel(name, type);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                name.equals(inputData.getMoodName()) && type.equals(inputData.getMoodType())
        ));
    }

    @Test
    @DisplayName("Should handle empty string parameters")
    void testAddMoodLabelWithEmptyStrings() {
        // Given
        String name = "";
        String type = "";

        // When
        controller.addMoodLabel(name, type);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                name.equals(inputData.getMoodName()) && type.equals(inputData.getMoodType())
        ));
    }

    @Test
    @DisplayName("Should handle null parameters")
    void testAddMoodLabelWithNullParameters() {
        // Given
        String name = null;
        String type = null;

        // When
        controller.addMoodLabel(name, type);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getMoodName() == name && inputData.getMoodType() == type
        ));
    }
}