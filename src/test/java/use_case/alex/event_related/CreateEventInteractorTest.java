package use_case.alex.event_related;

import entity.info.InfoFactory;
import entity.info.InfoInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.alex.event_related.create_event.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class CreateEventInteractorTest {

    private CreateEventDataAccessInterface mockDAO;
    private CreateEventOutputBoundary mockPresenter;
    private InfoFactory mockFactory;
    private InfoInterf mockInfo;
    private CreateEventInteractor interactor;

    @BeforeEach
    void setup() {
        mockDAO = mock(CreateEventDataAccessInterface.class);
        mockPresenter = mock(CreateEventOutputBoundary.class);
        mockFactory = mock(InfoFactory.class);
        mockInfo = mock(InfoInterf.class);
        interactor = new CreateEventInteractor(mockDAO, mockPresenter, mockFactory);
    }

    @Test
    void testSuccess() {
        CreateEventInputData input = new CreateEventInputData(
                "id123",
                "Event Name",
                "Description",
                "Study", // category
                LocalDate.now() // createdDate
        );
        
        when(mockDAO.getAllEvents()).thenReturn(Collections.emptyList());
        when(mockFactory.create("Event Name", "Description", "Study")).thenReturn(mockInfo);
        when(mockInfo.getName()).thenReturn("Event Name");
        when(mockInfo.getCategory()).thenReturn("Study");
        when(mockInfo.getDescription()).thenReturn("Description");

        interactor.execute(input);

        verify(mockDAO).save(mockInfo);
        verify(mockPresenter).prepareSuccessView(argThat(output ->
                output.getName().equals("Event Name") &&
                        output.getCategory().equals("Study") &&
                        output.getDescription().equals("Description") &&
                        !output.isFailed()));
    }

    @Test
    void testEmptyName() {
        CreateEventInputData input = new CreateEventInputData("id123", " ",
                "desc", "Study", LocalDate.now());
        when(mockDAO.getAllEvents()).thenReturn(Collections.emptyList());
        interactor.execute(input);
        verify(mockPresenter).prepareFailView("Event name cannot be empty.");
    }

    @Test
    void testNameTooLong() {
        String longName = "x".repeat(21);
        CreateEventInputData input = new CreateEventInputData("id123", longName,
                "desc", "Study", LocalDate.now());
        when(mockDAO.getAllEvents()).thenReturn(Collections.emptyList());
        interactor.execute(input);
        verify(mockPresenter).prepareFailView("Event name cannot exceed 20 characters.");
    }

    @Test
    void testDuplicateName() {
        CreateEventInputData input = new CreateEventInputData("id123", "Sleep",
                "desc", "Study", LocalDate.now());
        InfoInterf existing = mock(InfoInterf.class);
        when(existing.getName()).thenReturn("Sleep");
        when(mockDAO.getAllEvents()).thenReturn(List.of(existing));
        interactor.execute(input);
        verify(mockPresenter).prepareFailView("No duplicate allowed for event name.");
    }

    @Test
    void testCategoryTooLong() {
        String longCategory = "x".repeat(21);
        CreateEventInputData input = new CreateEventInputData("id123", "Sleep",
                "desc", longCategory, LocalDate.now());
        when(mockDAO.getAllEvents()).thenReturn(Collections.emptyList());
        interactor.execute(input);
        verify(mockPresenter).prepareFailView("Category cannot exceed 20 characters.");
    }

    @Test
    void testDescriptionTooLong() {
        String longDescription = "x".repeat(51);
        CreateEventInputData input = new CreateEventInputData("id123", "Sleep",
                longDescription, "Study", LocalDate.now());
        when(mockDAO.getAllEvents()).thenReturn(Collections.emptyList());
        interactor.execute(input);
        verify(mockPresenter).prepareFailView("Description cannot exceed 50 characters.");
    }
}

