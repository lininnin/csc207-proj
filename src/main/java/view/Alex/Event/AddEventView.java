package view.Alex.Event;

import interface_adapter.alex.event_related.add_event.AddEventController;
import interface_adapter.alex.event_related.add_event.AddedEventViewModel;
import interface_adapter.alex.event_related.add_event.AddedEventState;
import view.DueDatePickerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.List;

public class AddEventView extends JPanel implements ActionListener, PropertyChangeListener {

    private final AddedEventViewModel viewModel;
    private final AddEventController controller;

    private final JLabel titleLabel = new JLabel(AddedEventViewModel.TITLE_LABEL);
    private final JComboBox<String> nameComboBox = new JComboBox<>();
    private final DueDatePickerPanel dueDatePicker = new DueDatePickerPanel();  // ✅ 日历组件
    private final JButton addButton = new JButton(AddedEventViewModel.ADD_BUTTON_LABEL);
    private final JLabel messageLabel = new JLabel();

    public AddEventView(AddedEventViewModel viewModel, AddEventController controller) {
        this.viewModel = viewModel;
        this.controller = controller;
        this.viewModel.addPropertyChangeListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        this.setMaximumSize(new Dimension(150, 70));
        this.setPreferredSize(new Dimension(150, 70));

        // ---------- 标题 ----------
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        this.add(titleLabel);

        // ---------- 内容 ----------
        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameRow.add(new JLabel(AddedEventViewModel.NAME_LABEL));
        nameRow.add(nameComboBox);

        JPanel buttonRow = new JPanel();
        buttonRow.add(addButton);

        this.add(nameRow);
        this.add(dueDatePicker); // ✅ 替代原 dateRow
        this.add(buttonRow);
        this.add(messageLabel);

        addButton.addActionListener(this);
        refreshComboBox();
    }

    private void refreshComboBox() {
        nameComboBox.removeAllItems();
        List<String> names = viewModel.getState().getAvailableNames();
        if (names != null) {
            for (String name : names) {
                nameComboBox.addItem(name);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedName = (String) nameComboBox.getSelectedItem();
        LocalDate dueDate = dueDatePicker.getSelectedDate(); // ✅ 获取日历时间

        controller.execute(selectedName, dueDate);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        AddedEventState state = viewModel.getState();

        if (state.getErrorMessage() != null) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(state.getErrorMessage());
        } else if (state.getSuccessMessage() != null) {
            messageLabel.setForeground(new Color(0, 128, 0));
            messageLabel.setText(state.getSuccessMessage());
            dueDatePicker.clear(); // ✅ 清除日期
        }

        refreshComboBox();
    }

    public String getViewName() {
        return viewModel.getViewName();
    }
}
