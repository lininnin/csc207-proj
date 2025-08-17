package use_case.alex.event_related.create_event;

import entity.info.InfoFactory;
import entity.info.InfoFactoryInterf;
import entity.info.InfoInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Unit test for CreateEventInteractor using Mockito.
 * Covers edge cases: name length, duplication, category/description limits.
 */
public class CreateEventInteractorTest {

    private CreateEventDataAccessInterface mockDAO;
    private CreateEventOutputBoundary mockPresenter;
    private InfoFactoryInterf factory;
    private CreateEventInteractor interactor;

    @BeforeEach
    public void setUp() {
        mockDAO = mock(CreateEventDataAccessInterface.class);
        mockPresenter = mock(CreateEventOutputBoundary.class);
        factory = new InfoFactory();
        interactor = new CreateEventInteractor(mockDAO, mockPresenter, factory);
    }

    @Test
    public void testNameTooLong_shouldTriggerErrorMessage() {
        when(mockDAO.getAllEvents()).thenReturn(Collections.emptyList());

        CreateEventInputData input = new CreateEventInputData(
                "id1", "ThisNameIsMoreThanTwentyChars", "desc", "cat", LocalDate.now());

        interactor.execute(input);

        verify(mockPresenter).prepareFailView("Event name cannot exceed 20 characters.");
        verifyNoInteractions(mockDAO); // save shouldn't happen
    }

    @Test
    public void testNameEmpty_shouldTriggerErrorMessage() {
        when(mockDAO.getAllEvents()).thenReturn(Collections.emptyList());

        CreateEventInputData input = new CreateEventInputData(
                "id2", "   ", "desc", "cat", LocalDate.now());

        interactor.execute(input);

        verify(mockPresenter).prepareFailView("Event name cannot be empty.");
        verifyNoInteractions(mockDAO);
    }

    @Test
    public void testNameDuplicate_shouldTriggerErrorMessage() {
        InfoInterf existing = factory.create("Focus", "desc", "cat");
        when(mockDAO.getAllEvents()).thenReturn(List.of(existing));

        CreateEventInputData input = new CreateEventInputData(
                "id3", "Focus", "newdesc", "newcat", LocalDate.now());

        interactor.execute(input);

        verify(mockPresenter).prepareFailView("No duplicate allowed for event name.");
        verify(mockDAO, never()).save(any());
    }

    @Test
    public void testCategoryTooLong_shouldTriggerErrorMessage() {
        when(mockDAO.getAllEvents()).thenReturn(Collections.emptyList());

        String longCategory = "AReallyReallyLongCategoryName";
        CreateEventInputData input = new CreateEventInputData(
                "id4", "Event", "desc", longCategory, LocalDate.now());

        interactor.execute(input);

        verify(mockPresenter).prepareFailView("Category cannot exceed 20 characters.");
        verify(mockDAO).getAllEvents(); // ⚠️ 这一步一定会被执行
        verify(mockDAO, never()).save(any());
    }


    @Test
    public void testDescriptionTooLong_shouldTriggerErrorMessage() {
        when(mockDAO.getAllEvents()).thenReturn(Collections.emptyList());

        String longDesc = "This is a really long description that definitely goes over fifty characters in total.";
        CreateEventInputData input = new CreateEventInputData(
                "id5", "Event", longDesc, "cat", LocalDate.now());

        interactor.execute(input);

        verify(mockPresenter).prepareFailView("Description cannot exceed 50 characters.");
        verify(mockDAO).getAllEvents(); // ⚠️ 这一步一定会被执行
        verify(mockDAO, never()).save(any());
    }

    @Test
    public void testValidInput_shouldSucceed() {
        when(mockDAO.getAllEvents()).thenReturn(Collections.emptyList());

        CreateEventInputData input = new CreateEventInputData(
                "id6", "Study", "Short desc", "Health", LocalDate.now());

        interactor.execute(input);

        verify(mockPresenter).prepareSuccessView(any(CreateEventOutputData.class));
        verify(mockDAO).save(any(InfoInterf.class));
    }
}

