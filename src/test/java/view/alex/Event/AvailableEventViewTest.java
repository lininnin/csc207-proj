package view.alex.Event;

import entity.info.InfoFactory;
import entity.info.InfoFactoryInterf;
import entity.info.InfoInterf;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventState;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventViewModel;
import interface_adapter.alex.event_related.available_event_module.delete_event.DeletedEventState;
import interface_adapter.alex.event_related.available_event_module.delete_event.DeletedEventViewModel;
import interface_adapter.alex.event_related.create_event.CreatedEventState;
import interface_adapter.alex.event_related.create_event.CreatedEventViewModel;
import interface_adapter.alex.event_related.available_event_module.edit_event.EditedEventState;
import interface_adapter.alex.event_related.available_event_module.edit_event.EditedEventViewModel;
import interface_adapter.alex.event_related.available_event_module.edit_event.EditEventController;
import interface_adapter.alex.event_related.available_event_module.delete_event.DeleteEventController;
import use_case.alex.event_related.avaliable_events_module.delete_event.DeleteEventInputBoundary;
import use_case.alex.event_related.avaliable_events_module.delete_event.DeleteEventInputData;
import use_case.alex.event_related.avaliable_events_module.edit_event.EditEventInputBoundary;
import use_case.alex.event_related.avaliable_events_module.edit_event.EditEventInputData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AvailableEventViewTest {

    private AvailableEventView view;
    private AvailableEventViewModel availableEventVM;
    private CreatedEventViewModel createdEventVM;
    private DeletedEventViewModel deletedEventVM;
    private EditedEventViewModel editedEventVM;

    private InfoFactoryInterf infoFactory;

    @BeforeEach
    public void setUp() {
        availableEventVM = new AvailableEventViewModel();
        deletedEventVM = new DeletedEventViewModel();
        createdEventVM = new CreatedEventViewModel();
        editedEventVM = new EditedEventViewModel();

        infoFactory = new InfoFactory();

        DeleteEventController deleteController = new DeleteEventController(new DeleteEventInputBoundary() {
            @Override
            public void execute(DeleteEventInputData inputData) {
                DeletedEventState state = new DeletedEventState();
                state.setDeletedEventName("Mock Event");
                state.setDeletedSuccessfully(true);
                deletedEventVM.setState(state);
                deletedEventVM.firePropertyChanged();
            }
        });

        EditEventController editController = new EditEventController(new EditEventInputBoundary() {
            @Override
            public void execute(EditEventInputData inputData) {
                EditedEventState state = new EditedEventState();
                state.setName(inputData.getName());
                editedEventVM.setState(state);
                editedEventVM.firePropertyChanged();
            }
        });

        view = new AvailableEventView(
                availableEventVM,
                deleteController,
                deletedEventVM,
                createdEventVM,
                editController,
                editedEventVM
        );
    }

    @Test
    public void testEmptyEventListMessage() {
        SwingUtilities.invokeLater(() -> {
            boolean found = findComponentWithText(view, "No available events.");
            assertTrue(found);
        });
    }

    @Test
    public void testFullLifecycle() {
        InfoInterf info = infoFactory.create("Test Event", "desc", null);
        List<InfoInterf> list = new ArrayList<>();
        list.add(info);

        // 设置事件列表状态并刷新
        AvailableEventState state = new AvailableEventState();
        state.setAvailableEvents(list);
        availableEventVM.setState(state);
        availableEventVM.firePropertyChanged();

        view.forceRefresh();

        // 创建反馈
        CreatedEventState created = new CreatedEventState();
        created.setName("Created Event");
        createdEventVM.setState(created);
        createdEventVM.firePropertyChanged();

        // 删除失败
        DeletedEventState delFail = new DeletedEventState();
        delFail.setDeleteError("Something went wrong");
        deletedEventVM.setState(delFail);
        deletedEventVM.firePropertyChanged();

        // 删除成功
        DeletedEventState delSuccess = new DeletedEventState();
        delSuccess.setDeletedEventName("Test Event");
        delSuccess.setDeletedSuccessfully(true);
        deletedEventVM.setState(delSuccess);
        deletedEventVM.firePropertyChanged();

        // 编辑失败
        EditedEventState editFail = new EditedEventState();
        editFail.setEditError("Invalid input");
        editedEventVM.setState(editFail);
        editedEventVM.firePropertyChanged();

        // 编辑成功
        EditedEventState editSuccess = new EditedEventState();
        editSuccess.setName("Updated Name");
        editedEventVM.setState(editSuccess);
        editedEventVM.firePropertyChanged();

        view.forceRefresh();
    }

    private boolean findComponentWithText(JComponent parent, String text) {
        for (Component comp : parent.getComponents()) {
            if (comp instanceof JLabel && text.equals(((JLabel) comp).getText())) {
                return true;
            } else if (comp instanceof JComponent) {
                if (findComponentWithText((JComponent) comp, text)) return true;
            }
        }
        return false;
    }
}
