package use_case.alex.wellness_log_related.todays_wellness_log_module.edit_wellnesslog;

import entity.alex.MoodLabel.MoodLabelFactoryInterf;
import entity.alex.MoodLabel.MoodLabelInterf;
import entity.alex.MoodLabel.Type;
import entity.alex.WellnessLogEntry.WellnessLogEntryFactoryInterf;
import entity.alex.WellnessLogEntry.WellnessLogEntryInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.alex.wellness_log_related.todays_wellness_log_module.edit_wellnesslog.*;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class EditWellnessLogInteractorTest {

    private EditWellnessLogDataAccessInterf mockDAO;
    private EditWellnessLogOutputBoundary mockPresenter;
    private WellnessLogEntryFactoryInterf mockEntryFactory;
    private MoodLabelFactoryInterf moodLabelFactory;
    private EditWellnessLogInteractor interactor;

    @BeforeEach
    public void setUp() {
        mockDAO = mock(EditWellnessLogDataAccessInterf.class);
        mockPresenter = mock(EditWellnessLogOutputBoundary.class);
        mockEntryFactory = mock(WellnessLogEntryFactoryInterf.class);
        moodLabelFactory = mock(MoodLabelFactoryInterf.class);
        interactor = new EditWellnessLogInteractor(mockDAO, mockPresenter, mockEntryFactory,moodLabelFactory);
    }

    /**
     * 分支1: existing == null
     */
    @Test
    public void testExecute_logNotFound_shouldCallFailView() {
        String logId = "nonexistent";
        EditWellnessLogInputData inputData = new EditWellnessLogInputData(
                logId, 1, 2, 3, "Happy", Type.Positive, "Feeling good"
        );

        when(mockDAO.getById(logId)).thenReturn(null);

        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView("Log not found for ID: " + logId);
    }

    /**
     * 分支2: update == true
     */
    @Test
    public void testExecute_successfulUpdate_shouldCallSuccessView() {
        String logId = "log123";
        EditWellnessLogInputData inputData = new EditWellnessLogInputData(
                logId, 1, 2, 3, "Happy", Type.Positive, "Feeling good"
        );

        WellnessLogEntryInterf existing = mock(WellnessLogEntryInterf.class);
        when(existing.getId()).thenReturn(logId);
        when(existing.getTime()).thenReturn(LocalDateTime.now());

        when(mockDAO.getById(logId)).thenReturn(existing);

        MoodLabelInterf mockMood = mock(MoodLabelInterf.class);
        when(moodLabelFactory.create("Happy", Type.Positive)).thenReturn(mockMood);

        when(mockDAO.update(any())).thenReturn(true);

        interactor.execute(inputData);

        verify(mockPresenter).prepareSuccessView(argThat(data ->
                data.getLogId().equals(logId) && data.isSuccessMessageVisible()));
    }

    /**
     * 分支3: update == false
     */
    @Test
    public void testExecute_updateFails_shouldCallFailView() {
        String logId = "log321";
        EditWellnessLogInputData inputData = new EditWellnessLogInputData(
                logId, 2, 2, 2, "Sad", Type.Negative, "No energy"
        );

        WellnessLogEntryInterf existing = mock(WellnessLogEntryInterf.class);
        when(existing.getId()).thenReturn(logId);
        when(existing.getTime()).thenReturn(LocalDateTime.now());

        when(mockDAO.getById(logId)).thenReturn(existing);

        MoodLabelInterf mockMood = mock(MoodLabelInterf.class);
        when(moodLabelFactory.create(any(), any())).thenReturn(mockMood);

        when(mockDAO.update(any())).thenReturn(false);

        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView("Failed to update the wellness log.");
    }

    /**
     * 分支4: moodLabelFactory 抛异常
     */
    @Test
    public void testExecute_moodFactoryThrowsException_shouldCallFailView() {
        String logId = "log999";
        EditWellnessLogInputData inputData = new EditWellnessLogInputData(
                logId, 2, 2, 2, "Angry", Type.Negative, "Annoyed"
        );

        WellnessLogEntryInterf existing = mock(WellnessLogEntryInterf.class);
        when(existing.getId()).thenReturn(logId);
        when(existing.getTime()).thenReturn(LocalDateTime.now());

        when(mockDAO.getById(logId)).thenReturn(existing);
        when(moodLabelFactory.create(any(), any())).thenThrow(new RuntimeException("Test Exception"));

        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView(argThat(msg -> msg.contains("Error while editing log: Test Exception")));
    }
}
