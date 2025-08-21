package use_case.alex.event_related.todays_events_module.edit_todays_event;

import entity.BeginAndDueDates.BeginAndDueDatesInterf;
import entity.alex.Event.EventInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class EditTodaysEventInteractorTest {

    private EditTodaysEventDataAccessInterf mockDAO;
    private EditTodaysEventOutputBoundary mockPresenter;
    private EditTodaysEventInteractor interactor;

    @BeforeEach
    void setUp() {
        mockDAO = mock(EditTodaysEventDataAccessInterf.class);
        mockPresenter = mock(EditTodaysEventOutputBoundary.class);
        interactor = new EditTodaysEventInteractor(mockDAO, mockPresenter);
    }

    @Test
    void testSuccessfulUpdate() {
        String id = "event123";
        String newDueDate = "2025-08-30";

        EventInterf mockEvent = mock(EventInterf.class);
        BeginAndDueDatesInterf mockDates = mock(BeginAndDueDatesInterf.class);
        when(mockEvent.getBeginAndDueDates()).thenReturn(mockDates);
        when(mockDAO.getEventById(id)).thenReturn(mockEvent);
        when(mockDAO.update(mockEvent)).thenReturn(true);

        EditTodaysEventInputData input = new EditTodaysEventInputData(id, newDueDate);
        interactor.execute(input);

        verify(mockDates).setDueDate(LocalDate.parse(newDueDate));
        verify(mockPresenter).prepareSuccessView(
                argThat(out -> out.getId().equals(id)
                        && out.getDueDate().equals(newDueDate)
                        && !out.isUseCaseFailed()));
    }

    @Test
    void testFailOnInvalidDate() {
        String id = "event123";
        String invalidDate = "2025-13-99";  // 非法格式

        EditTodaysEventInputData input = new EditTodaysEventInputData(id, invalidDate);
        interactor.execute(input);

        verify(mockPresenter).prepareFailView(
                argThat(out -> out.getId().equals(id)
                        && out.getDueDate().equals("")
                        && out.isUseCaseFailed()));
    }

    @Test
    void testFailWhenEventNotFound() {
        String id = "event123";
        String newDate = "2025-08-30";

        when(mockDAO.getEventById(id)).thenReturn(null);
        EditTodaysEventInputData input = new EditTodaysEventInputData(id, newDate);
        interactor.execute(input);

        verify(mockPresenter).prepareFailView(
                argThat(out -> out.getId().equals(id)
                        && out.getDueDate().equals(newDate)
                        && out.isUseCaseFailed()));
    }

    @Test
    void testFailWhenUpdateFailsInDAO() {
        String id = "event123";
        String newDate = "2025-08-30";

        EventInterf mockEvent = mock(EventInterf.class);
        BeginAndDueDatesInterf mockDates = mock(BeginAndDueDatesInterf.class);
        when(mockEvent.getBeginAndDueDates()).thenReturn(mockDates);
        when(mockDAO.getEventById(id)).thenReturn(mockEvent);
        when(mockDAO.update(mockEvent)).thenReturn(false); // 模拟更新失败

        EditTodaysEventInputData input = new EditTodaysEventInputData(id, newDate);
        interactor.execute(input);

        verify(mockPresenter).prepareFailView(
                argThat(out -> out.getId().equals(id)
                        && out.getDueDate().equals(newDate)
                        && out.isUseCaseFailed()));
    }
}

