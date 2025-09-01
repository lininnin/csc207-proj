package view.alex.Event;

import interface_adapter.alex.event_related.add_event.AddedEventViewModel;
import interface_adapter.alex.event_related.add_event.AddedEventState;
import interface_adapter.alex.event_related.add_event.AddEventController;
import use_case.alex.event_related.add_event.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.DueDatePickerPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AddEventViewTest {

    private AddEventView view;
    private AddedEventViewModel viewModel;
    private TestAddEventInteractor interactor;

    @BeforeEach
    public void setUp() {
        viewModel = new AddedEventViewModel();

        // 手动构造 AddEventOutputBoundary 接口的匿名实现
        AddEventOutputBoundary outputBoundary = new AddEventOutputBoundary() {
            @Override
            public void prepareSuccessView(AddEventOutputData outputData) {
                AddedEventState state = viewModel.getState();
                state.setSuccessMessage("Successfully added: " + outputData.getName());
                state.setErrorMessage(null);
                viewModel.setState(state);
                viewModel.firePropertyChanged();
            }

            @Override
            public void prepareFailView(String errorMessage) {
                AddedEventState state = viewModel.getState();
                state.setErrorMessage(errorMessage);
                state.setSuccessMessage(null);
                viewModel.setState(state);
                viewModel.firePropertyChanged();
            }
        };

        interactor = new TestAddEventInteractor(outputBoundary);
        AddEventController controller = new AddEventController(interactor);
        view = new AddEventView(viewModel, controller);

        viewModel.getState().setAvailableNames(List.of("Task A", "Task B"));
        view.forceRefresh();
    }

    @Test
    public void testAddEventSuccess() {
        JComboBox<String> comboBox = findComboBox();
        comboBox.setSelectedItem("Task A");

        DueDatePickerPanel datePicker = findDatePicker();
        datePicker.setSelectedDate(LocalDate.of(2025, 8, 31));

        JButton addButton = findAddButton();
        addButton.doClick();

        // 验证 interactor 接收了正确的数据
        assertEquals(1, interactor.recordedInputs.size());
        AddEventInputData data = interactor.recordedInputs.get(0);
        assertEquals("Task A", data.getSelectedName());
        assertEquals(LocalDate.of(2025, 8, 31), data.getDueDate());

        // 验证成功消息
        JLabel messageLabel = findMessageLabel();
        assertTrue(messageLabel.getText().contains("Successfully added: Task A"));
        assertEquals(new Color(0, 128, 0), messageLabel.getForeground());
    }

    @Test
    public void testAddEventFail() {
        // Clear the comboBox to ensure no selection
        JComboBox<String> comboBox = findComboBox();
        comboBox.removeAllItems(); // Remove all items so getSelectedItem() returns null

        JButton addButton = findAddButton();
        addButton.doClick();

        JLabel messageLabel = findMessageLabel();
        assertEquals("Event name is required", messageLabel.getText());
        assertEquals(Color.RED, messageLabel.getForeground());
    }

    // === Helper Methods ===

    private JComboBox<String> findComboBox() {
        for (Component c : view.getComponents()) {
            if (c instanceof JPanel panel) {
                for (Component inner : panel.getComponents()) {
                    if (inner instanceof JComboBox<?> cb) return (JComboBox<String>) cb;
                }
            }
        }
        throw new RuntimeException("ComboBox not found");
    }

    private JButton findAddButton() {
        for (Component c : view.getComponents()) {
            if (c instanceof JPanel panel) {
                for (Component inner : panel.getComponents()) {
                    if (inner instanceof JButton btn) return btn;
                }
            }
        }
        throw new RuntimeException("Add button not found");
    }

    private DueDatePickerPanel findDatePicker() {
        for (Component c : view.getComponents()) {
            if (c instanceof DueDatePickerPanel dp) return dp;
        }
        throw new RuntimeException("Date picker not found");
    }

    private JLabel findMessageLabel() {
        for (Component c : view.getComponents()) {
            if (c instanceof JLabel label && 
                !label.getText().contains("Name") && 
                !label.getText().contains("Add Today's Event")) {
                return label;
            }
        }
        throw new RuntimeException("Message label not found");
    }

    // === Fake Interactor ===

    private static class TestAddEventInteractor implements AddEventInputBoundary {

        private final AddEventOutputBoundary presenter;
        public final List<AddEventInputData> recordedInputs = new ArrayList<>();

        public TestAddEventInteractor(AddEventOutputBoundary presenter) {
            this.presenter = presenter;
        }

        @Override
        public void execute(AddEventInputData inputData) {
            recordedInputs.add(inputData);
            if (inputData.getSelectedName() == null || inputData.getSelectedName().isBlank()) {
                presenter.prepareFailView("Event name is required");
            } else {
                AddEventOutputData out = new AddEventOutputData(
                        inputData.getSelectedName(),
                        inputData.getDueDate(),
                        true
                );
                presenter.prepareSuccessView(out);
            }
        }
    }
}
