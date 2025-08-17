package use_case.alex.wellness_log_related.moodlabel_related;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.alex.wellness_log_related.moodlabel_related.delete_moodLabel.*;

import static org.mockito.Mockito.*;

public class DeleteMoodLabelInteractorTest {

    private DeleteMoodLabelDataAccessInterf mockDAO;
    private DeleteMoodLabelOutputBoundary mockPresenter;
    private DeleteMoodLabelInteractor interactor;

    @BeforeEach
    void setUp() {
        mockDAO = mock(DeleteMoodLabelDataAccessInterf.class);
        mockPresenter = mock(DeleteMoodLabelOutputBoundary.class);
        interactor = new DeleteMoodLabelInteractor(mockDAO, mockPresenter);
    }

    @Test
    void testExecute_existingMood_shouldDeleteAndReturnSuccess() {
        // Arrange
        String labelName = "Anxious";
        MoodLabel target = new MoodLabel.Builder(labelName).type(Type.Negative).build();
        when(mockDAO.getByName(labelName)).thenReturn(target);
        when(mockDAO.remove(target)).thenReturn(true);

        DeleteMoodLabelInputData input = new DeleteMoodLabelInputData(labelName);

        // Act
        interactor.execute(input);

        // Assert
        verify(mockDAO).remove(target);
        verify(mockPresenter).prepareSuccessView(
                argThat(output -> output.isDeletionSuccess()
                        && output.getMoodLabelName().equals(labelName)
                        && output.getErrorMessage() == null)
        );
    }

    @Test
    void testExecute_nonexistentMood_shouldReturnFailure() {
        String labelName = "UnknownLabel";
        when(mockDAO.getByName(labelName)).thenReturn(null);

        DeleteMoodLabelInputData input = new DeleteMoodLabelInputData(labelName);
        interactor.execute(input);

        verify(mockPresenter).prepareFailView(
                argThat(output -> !output.isDeletionSuccess()
                        && output.getMoodLabelName().equals(labelName)
                        && "Mood label not found.".equals(output.getErrorMessage()))
        );
        verify(mockDAO, never()).remove(any());
    }

    @Test
    void testExecute_removalFails_shouldReturnFailure() {
        String labelName = "Calm";
        MoodLabel existing = new MoodLabel.Builder(labelName).type(Type.Positive).build();

        when(mockDAO.getByName(labelName)).thenReturn(existing);
        when(mockDAO.remove(existing)).thenReturn(false);

        DeleteMoodLabelInputData input = new DeleteMoodLabelInputData(labelName);
        interactor.execute(input);

        verify(mockPresenter).prepareFailView(
                argThat(output -> !output.isDeletionSuccess()
                        && output.getMoodLabelName().equals(labelName)
                        && "Failed to delete mood label.".equals(output.getErrorMessage()))
        );
    }
}

