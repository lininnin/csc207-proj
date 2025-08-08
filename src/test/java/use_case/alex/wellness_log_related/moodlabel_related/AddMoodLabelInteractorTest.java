package use_case.alex.wellness_log_related.moodlabel_related;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelFactoryInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.alex.wellness_log_related.moodlabel_related.add_moodLabel.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class AddMoodLabelInteractorTest {

    private AddMoodLabelDataAccessInterf mockDAO;
    private AddMoodLabelOutputBoundary mockPresenter;
    private MoodLabelFactoryInterf mockFactory;
    private AddMoodLabelInteractor interactor;

    @BeforeEach
    void setUp() {
        mockDAO = mock(AddMoodLabelDataAccessInterf.class);
        mockPresenter = mock(AddMoodLabelOutputBoundary.class);
        mockFactory = mock(MoodLabelFactoryInterf.class);
        interactor = new AddMoodLabelInteractor(mockDAO, mockPresenter, mockFactory);
    }

    @Test
    void testExecute_successfulAddition_shouldCallSaveAndPresenter() {
        // Arrange
        String name = "Joyful";
        String type = "Positive";
        AddMoodLabelInputData input = new AddMoodLabelInputData(name, type);
        MoodLabel newLabel = new MoodLabel.Builder(name).type(MoodLabel.Type.Positive).build();

        when(mockDAO.getAllLabels()).thenReturn(new ArrayList<>());
        when(mockFactory.create(name, MoodLabel.Type.Positive)).thenReturn(newLabel);

        // Act
        interactor.execute(input);

        // Assert
        verify(mockDAO).save(newLabel);
        verify(mockPresenter).prepareSuccessView(
                argThat(output -> output.isSuccess()
                        && output.getMoodName().equals(name)
                        && output.getMoodType().equals(type))
        );
    }

    @Test
    void testExecute_emptyName_shouldCallFailView() {
        AddMoodLabelInputData input = new AddMoodLabelInputData("  ", "Positive");

        interactor.execute(input);

        verify(mockPresenter).prepareFailView("Mood label name cannot be empty.");
        verify(mockDAO, never()).save(any());
    }

    @Test
    void testExecute_invalidType_shouldCallFailView() {
        AddMoodLabelInputData input = new AddMoodLabelInputData("Calm", "Neutral");

        interactor.execute(input);

        verify(mockPresenter).prepareFailView("Mood label type must be 'Positive' or 'Negative'.");
        verify(mockDAO, never()).save(any());
    }

    @Test
    void testExecute_duplicateName_shouldCallFailView() {
        String name = "Sad";
        MoodLabel existing = new MoodLabel.Builder(name).type(MoodLabel.Type.Negative).build();

        when(mockDAO.getAllLabels()).thenReturn(List.of(existing));

        AddMoodLabelInputData input = new AddMoodLabelInputData(name, "Negative");

        interactor.execute(input);

        verify(mockPresenter).prepareFailView("Mood label already exists.");
        verify(mockDAO, never()).save(any());
    }
}

