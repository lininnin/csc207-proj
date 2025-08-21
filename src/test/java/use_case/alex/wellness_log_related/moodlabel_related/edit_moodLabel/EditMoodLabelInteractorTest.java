package use_case.alex.wellness_log_related.moodlabel_related.edit_moodLabel;

import entity.alex.MoodLabel.MoodLabel;
import entity.alex.MoodLabel.MoodLabelFactory;
import entity.alex.MoodLabel.MoodLabelFactoryInterf;
import entity.alex.MoodLabel.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class EditMoodLabelInteractorTest {

    private EditMoodLabelDataAccessInterface mockDAO;
    private EditMoodLabelOutputBoundary mockPresenter;
    private EditMoodLabelInteractor interactor;
    private MoodLabelFactoryInterf Factory;

    @BeforeEach
    void setUp() {
        mockDAO = mock(EditMoodLabelDataAccessInterface.class);
        mockPresenter = mock(EditMoodLabelOutputBoundary.class);
        Factory = new MoodLabelFactory();

        interactor = new EditMoodLabelInteractor(mockDAO, mockPresenter, Factory);
    }

    @Test
    void testExecute_successfulEdit_shouldCallUpdateAndSuccessView() {
        String original = "Happy";
        String newName = "Joyful";
        String newType = "Positive";

        MoodLabel originalLabel = new MoodLabel.Builder(original).type(Type.Positive).build();
        when(mockDAO.getByName(original)).thenReturn(originalLabel);
        when(mockDAO.update(any())).thenReturn(true);

        EditMoodLabelInputData input = new EditMoodLabelInputData(original, newName, newType);
        interactor.execute(input);

        verify(mockDAO).update(argThat(label -> label.getName().equals(newName) && label.getType() == Type.Positive));
        verify(mockPresenter).prepareSuccessView(argThat(output ->
                output.getName().equals(newName)
                        && output.getType().equals(newType)
                        && !output.isUseCaseFailed()));
    }

    @Test
    void testExecute_invalidName_shouldFail() {
        EditMoodLabelInputData input = new EditMoodLabelInputData("Sad", "", "Negative");

        interactor.execute(input);

        verify(mockPresenter).prepareFailView(argThat(output ->
                output.isUseCaseFailed()
                        && output.getName().equals("")
                        && output.getType().equals("Negative")));
        verify(mockDAO, never()).update(any());
    }

    @Test
    void testExecute_invalidType_shouldFail() {
        EditMoodLabelInputData input = new EditMoodLabelInputData("Sad", "Blue", "Angry");

        interactor.execute(input);

        verify(mockPresenter).prepareFailView(argThat(output ->
                output.isUseCaseFailed()
                        && output.getName().equals("Blue")
                        && output.getType().equals("Angry")));
        verify(mockDAO, never()).update(any());
    }

    @Test
    void testExecute_originalNotFound_shouldFail() {
        when(mockDAO.getByName("Lost")).thenReturn(null);

        EditMoodLabelInputData input = new EditMoodLabelInputData("Lost", "Calm", "Positive");
        interactor.execute(input);

        verify(mockPresenter).prepareFailView(argThat(output ->
                output.isUseCaseFailed()
                        && output.getName().equals("Calm")
                        && output.getType().equals("Positive")));
        verify(mockDAO, never()).update(any());
    }

    @Test
    void testExecute_updateFails_shouldFail() {
        String original = "Tired";
        String newName = "Exhausted";
        String newType = "Negative";

        MoodLabel originalLabel = new MoodLabel.Builder(original).type(Type.Negative).build();
        when(mockDAO.getByName(original)).thenReturn(originalLabel);
        when(mockDAO.update(any())).thenReturn(false);

        EditMoodLabelInputData input = new EditMoodLabelInputData(original, newName, newType);
        interactor.execute(input);

        verify(mockPresenter).prepareFailView(argThat(output ->
                output.isUseCaseFailed()
                        && output.getName().equals(newName)
                        && output.getType().equals(newType)));
    }
}


