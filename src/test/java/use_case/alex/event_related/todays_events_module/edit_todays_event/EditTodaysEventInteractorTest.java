package use_case.alex.event_related.todays_events_module.edit_todays_event;

import entity.BeginAndDueDates.BeginAndDueDatesInterf;
import entity.Alex.Event.EventInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class EditTodaysEventInteractorTest {

    private EditTodaysEventDataAccessInterf mockDataAccess;
    private EditTodaysEventOutputBoundary mockPresenter;
    private EditTodaysEventInteractor interactor;

    @BeforeEach
    void setUp() {
        mockDataAccess = mock(EditTodaysEventDataAccessInterf.class);
        mockPresenter = mock(EditTodaysEventOutputBoundary.class);
        interactor = new EditTodaysEventInteractor(mockDataAccess, mockPresenter);
    }

    @Test
    void testInvalidDateFormat_shouldFail() {
        EditTodaysEventInputData input = new EditTodaysEventInputData("e123", "invalid-date");

        interactor.execute(input);

        verify(mockPresenter).prepareFailView(
                argThat(out -> out != null &&
                        "e123".equals(out.getId()) &&
                        out.isUseCaseFailed())
        );

        verifyNoInteractions(mockDataAccess);
    }

    @Test
    void testEventNotFound_shouldFail() {
        String eventId = "missing";
        String dateStr = "2025-08-20";
        EditTodaysEventInputData input = new EditTodaysEventInputData(eventId, dateStr);

        when(mockDataAccess.getEventById(eventId)).thenReturn(null);

        interactor.execute(input);

        verify(mockPresenter).prepareFailView(
                argThat(out -> out != null &&
                        eventId.equals(out.getId()) &&
                        out.isUseCaseFailed())
        );
    }

    @Test
    void testUpdateFails_shouldFail() {
        String eventId = "e123";
        String dateStr = "2025-08-20";

        EditTodaysEventInputData input = new EditTodaysEventInputData(eventId, dateStr);

        BeginAndDueDatesInterf mockDates = mock(BeginAndDueDatesInterf.class);
        EventInterf mockEvent = mock(EventInterf.class);
        when(mockEvent.getBeginAndDueDates()).thenReturn(mockDates);

        when(mockDataAccess.getEventById(eventId)).thenReturn(mockEvent);
        when(mockDataAccess.update(mockEvent)).thenReturn(false);

        interactor.execute(input);

        verify(mockDates).setDueDate(LocalDate.parse(dateStr));
        verify(mockPresenter).prepareFailView(
                argThat(out -> out != null &&
                        eventId.equals(out.getId()) &&
                        out.isUseCaseFailed())
        );
    }

    @Test
    void testValidEdit_shouldSucceed() {
        String eventId = "e123";
        String dateStr = "2025-08-20";

        EditTodaysEventInputData input = new EditTodaysEventInputData(eventId, dateStr);

        BeginAndDueDatesInterf mockDates = mock(BeginAndDueDatesInterf.class);
        EventInterf mockEvent = mock(EventInterf.class);
        when(mockEvent.getBeginAndDueDates()).thenReturn(mockDates);

        when(mockDataAccess.getEventById(eventId)).thenReturn(mockEvent);
        when(mockDataAccess.update(mockEvent)).thenReturn(true);

        interactor.execute(input);

        verify(mockDates).setDueDate(LocalDate.parse(dateStr));
        verify(mockPresenter).prepareSuccessView(
                argThat(out -> out != null &&
                        eventId.equals(out.getId()) &&
                        dateStr.equals(out.getDueDate()) &&
                        !out.isUseCaseFailed())
        );
    }
}
