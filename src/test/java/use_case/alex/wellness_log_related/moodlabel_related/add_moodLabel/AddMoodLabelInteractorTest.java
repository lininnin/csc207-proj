package use_case.alex.wellness_log_related.moodlabel_related.add_moodLabel;

import entity.alex.MoodLabel.MoodLabelFactoryInterf;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelViewModel;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.add_moodLabel.AddMoodLabelState;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.add_moodLabel.AddMoodLabelViewModel;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.add_moodLabel.AddMoodLabelPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import data_access.MoodAvailableDataAccessObject;
import entity.alex.AvalibleMoodLabel.AvaliableMoodLabelFactory;
import entity.alex.MoodLabel.MoodLabelFactory;
import entity.alex.MoodLabel.Type;

public class AddMoodLabelInteractorTest {

    private AddMoodLabelInteractor interactor;
    private AddMoodLabelViewModel addViewModel;
    private AvailableMoodLabelViewModel availableViewModel;
    private MoodAvailableDataAccessObject realDAO;

    @BeforeEach
    public void setUp() {
        // ✅ 使用真实 factory 和 DAO
        AvaliableMoodLabelFactory availableFactory = new AvaliableMoodLabelFactory();
        MoodLabelFactoryInterf moodFactory = new MoodLabelFactory();
        realDAO = new MoodAvailableDataAccessObject(availableFactory, moodFactory);

        // ✅ 使用真实 ViewModel 和 Presenter
        addViewModel = new AddMoodLabelViewModel();
        availableViewModel = new AvailableMoodLabelViewModel();
        AddMoodLabelPresenter presenter = new AddMoodLabelPresenter(addViewModel, availableViewModel, realDAO);

        interactor = new AddMoodLabelInteractor(realDAO, presenter, moodFactory);
    }

    @Test
    public void testAddPositiveMoodLabelSuccess() {
        AddMoodLabelInputData inputData = new AddMoodLabelInputData("Grateful", Type.Positive.toString());
        interactor.execute(inputData);

        assertEquals("Grateful", addViewModel.getState().getMoodName());
        assertNull(addViewModel.getState().getErrorMessage());
        assertEquals("Mood label \"Grateful\" added successfully.", addViewModel.getState().getSuccessMessage());
        assertTrue(
                availableViewModel.getState().getPositiveLabels()
                        .stream()
                        .anyMatch(entry -> entry.getName().equals("Grateful"))
        );
    }

    @Test
    public void testAddDuplicateMoodLabelFails() {
        // Add one first
        interactor.execute(new AddMoodLabelInputData("Calm", Type.Positive.toString()));
        // Add again
        interactor.execute(new AddMoodLabelInputData("Calm", Type.Positive.toString()));

        AddMoodLabelState state = addViewModel.getState();
        assertEquals("Mood label already exists.", state.getErrorMessage());
        assertNull(state.getSuccessMessage());
        assertTrue(state.getMoodName().isEmpty()); // ✅ 如果你还想测试 name 是空

        assertEquals("Mood label already exists.", addViewModel.getState().getErrorMessage());
        assertNull(addViewModel.getState().getSuccessMessage());
    }
}

