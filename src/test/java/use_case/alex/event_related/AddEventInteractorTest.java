package use_case.alex.event_related;

import entity.Alex.Event.Event;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.alex.event_related.add_event.*;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AddEventInteractorTest {

    private AddEventDataAccessInterf mockTodaysEventDAO;
    private ReadAvailableEventDataAccessInterf mockAvailableEventDAO;
    private AddEventOutputBoundary mockPresenter;
    private AddEventInteractor interactor;

    @BeforeEach
    void setUp() {
        mockTodaysEventDAO = mock(AddEventDataAccessInterf.class);
        mockAvailableEventDAO = mock(ReadAvailableEventDataAccessInterf.class);
        mockPresenter = mock(AddEventOutputBoundary.class);
        interactor = new AddEventInteractor(mockTodaysEventDAO, mockAvailableEventDAO, mockPresenter);
    }

    @Test
    void testExecute_eventNotFoundInAvailable_shouldFail() {
        AddEventInputData inputData = new AddEventInputData("NonExistentEvent", LocalDate.of(2025, 8, 10));
        when(mockAvailableEventDAO.findInfoByName("NonExistentEvent")).thenReturn(null);

        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView("No available event with name: NonExistentEvent");
        verifyNoInteractions(mockTodaysEventDAO);
    }

    @Test
    void testExecute_eventAlreadyExistsToday_shouldFail() {
        Info mockInfo = new Info.Builder("Test Event")
                .description("Some description")
                .category("Work")
                .build();

        AddEventInputData inputData = new AddEventInputData("Test Event", LocalDate.of(2025, 8, 10));

        when(mockAvailableEventDAO.findInfoByName("Test Event")).thenReturn(mockInfo);
        when(mockTodaysEventDAO.contains(any(Event.class))).thenReturn(true);

        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView("Event already added today.");
        verify(mockTodaysEventDAO, never()).save(any(Event.class));
    }

    @Test
    void testExecute_validEvent_shouldSucceed() {
        Info mockInfo = new Info.Builder("Test Event")
                .description("A test event")
                .category("Personal")
                .build();

        AddEventInputData inputData = new AddEventInputData("Test Event", LocalDate.of(2025, 8, 10));

        when(mockAvailableEventDAO.findInfoByName("Test Event")).thenReturn(mockInfo);
        when(mockTodaysEventDAO.contains(any(Event.class))).thenReturn(false);

        interactor.execute(inputData);

        verify(mockTodaysEventDAO).save(any(Event.class));
        verify(mockPresenter).prepareSuccessView(
                argThat(output -> output.getName().equals("Test Event")
                        && output.getDueDate().equals(LocalDate.of(2025, 8, 10))
                        && output.isSuccess()));
    }
}

