package use_case.alex.wellness_log_related.todays_wellness_log_related;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelFactoryInterf;
import entity.Alex.WellnessLogEntry.Levels;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import entity.Alex.WellnessLogEntry.WellnessLogEntryFactoryInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import use_case.alex.wellness_log_related.todays_wellness_log_module.edit_wellnesslog.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class EditWellnessLogInteractorTest {

    private EditWellnessLogDataAccessInterf mockDAO;
    private EditWellnessLogOutputBoundary mockPresenter;
    private EditWellnessLogInteractor interactor;

    @BeforeEach
    public void setUp() {
        mockDAO = mock(EditWellnessLogDataAccessInterf.class);
        mockPresenter = mock(EditWellnessLogOutputBoundary.class);

        interactor = new EditWellnessLogInteractor(mockDAO, mockPresenter);
    }

    @Test
    public void testExecute_logNotFound_shouldCallFailView() {
        // Arrange
        String logId = "log404";
        EditWellnessLogInputData inputData = new EditWellnessLogInputData(
                logId, 2, 2, 2, "Stressed", MoodLabel.Type.Negative, "Too much work"
        );

        when(mockDAO.getById(logId)).thenReturn(null);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(mockDAO, never()).update(any());
        verify(mockPresenter).prepareFailView("Wellness log not found for ID: " + logId);
    }
}
