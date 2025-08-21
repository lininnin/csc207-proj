package use_case.alex.event_related.create_event;

import entity.info.InfoFactory;
import entity.info.InfoInterf;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventViewModel;
import interface_adapter.alex.event_related.create_event.CreatedEventViewModel;
import interface_adapter.alex.event_related.create_event.CreateEventPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateEventInteractorTest {

    private CreateEventDataAccessInterface mockDAO;
    private CreatedEventViewModel createdEventViewModel;
    private AvailableEventViewModel availableEventViewModel;
    private CreateEventInteractor interactor;

    @BeforeEach
    void setUp() {
        mockDAO = mock(CreateEventDataAccessInterface.class);

        createdEventViewModel = new CreatedEventViewModel();
        availableEventViewModel = new AvailableEventViewModel();

        CreateEventPresenter presenter =
                new CreateEventPresenter(createdEventViewModel, availableEventViewModel, mockDAO);

        InfoFactory realFactory = new InfoFactory(); // ✅ 使用真实工厂
        interactor = new CreateEventInteractor(mockDAO, presenter, realFactory);
    }

    @Test
    void testSuccessfulCreationClearsState() {
        CreateEventInputData input = new CreateEventInputData(
                "id-1", "Study", "Finish math", "School", LocalDate.now());

        when(mockDAO.getAllEvents()).thenReturn(Collections.emptyList());

        interactor.execute(input);

        // ✅ 成功创建后 ViewModel 字段应被清空
        assertEquals("", createdEventViewModel.getState().getName());
        assertEquals("", createdEventViewModel.getState().getCategory());
        assertEquals("", createdEventViewModel.getState().getDescription());

        // ✅ DAO 的 save 方法应该被调用
        verify(mockDAO).save(any(InfoInterf.class));
    }

    @Test
    void testFailOnEmptyName() {
        CreateEventInputData input = new CreateEventInputData(
                "id-2", "   ", "desc", "cat", LocalDate.now());
        when(mockDAO.getAllEvents()).thenReturn(Collections.emptyList());

        interactor.execute(input);

        assertEquals("Event name cannot be empty.", createdEventViewModel.getState().getNameError());
    }

    @Test
    void testFailOnDuplicateName() {
        InfoInterf existing = mock(InfoInterf.class);
        when(existing.getName()).thenReturn("Study");
        when(mockDAO.getAllEvents()).thenReturn(Collections.singletonList(existing));

        CreateEventInputData input = new CreateEventInputData(
                "id-3", "Study", "desc", "cat", LocalDate.now());

        interactor.execute(input);

        assertEquals("No duplicate allowed for event name.", createdEventViewModel.getState().getNameError());
    }

    @Test
    void testFailOnLongName() {
        String longName = "a".repeat(21);
        CreateEventInputData input = new CreateEventInputData(
                "id-4", longName, "desc", "cat", LocalDate.now());

        when(mockDAO.getAllEvents()).thenReturn(Collections.emptyList());
        interactor.execute(input);

        assertEquals("Event name cannot exceed 20 characters.", createdEventViewModel.getState().getNameError());
    }

    @Test
    void testFailOnLongCategory() {
        String longCat = "c".repeat(21);
        CreateEventInputData input = new CreateEventInputData(
                "id-5", "Test", "desc", longCat, LocalDate.now());

        when(mockDAO.getAllEvents()).thenReturn(Collections.emptyList());
        interactor.execute(input);

        assertEquals("Category cannot exceed 20 characters.", createdEventViewModel.getState().getNameError());
    }

    @Test
    void testFailOnLongDescription() {
        String longDesc = "d".repeat(51);
        CreateEventInputData input = new CreateEventInputData(
                "id-6", "Test", longDesc, "cat", LocalDate.now());

        when(mockDAO.getAllEvents()).thenReturn(Collections.emptyList());
        interactor.execute(input);

        assertEquals("Description cannot exceed 50 characters.", createdEventViewModel.getState().getNameError());
    }
}
