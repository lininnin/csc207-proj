package view.Alex;

import entity.Alex.Event.Event;
import interface_adapter.Alex.add_event.AddEventController;
import interface_adapter.Alex.add_event.AddedEventState;
import interface_adapter.Alex.add_event.AddedEventViewModel;
import interface_adapter.Alex.edit_todays_event.EditTodaysEventController;
import interface_adapter.Alex.edit_todays_event.EditTodaysEventState;
import interface_adapter.Alex.edit_todays_event.EditTodaysEventViewModel;
import interface_adapter.Alex.todays_events.TodaysEventsState;
import interface_adapter.Alex.todays_events.TodaysEventsViewModel;
import interface_adapter.Alex.delete_todays_event.DeleteTodaysEventController;
import view.DueDatePickerPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * View that displays today's events in a table format with edit/delete buttons.
 */
public class TodaysEventsView extends JPanel {

    private final TodaysEventsViewModel todaysEventsViewModel;
    private final AddEventController addEventController;
    private final AddedEventViewModel addedEventViewModel;
    private final DeleteTodaysEventController deleteTodaysEventController; // ✅ 添加删除 controller
    private final EditTodaysEventController editTodaysEventController;
    private final EditTodaysEventViewModel editTodaysEventViewModel;


    private final JPanel todaysEventsListPanel = new JPanel();

    public TodaysEventsView(TodaysEventsViewModel todaysEventsViewModel,
                            AddEventController addEventController,
                            AddedEventViewModel addedEventViewModel,
                            DeleteTodaysEventController deleteTodaysEventController,
                            EditTodaysEventController editTodaysEventController,
                            EditTodaysEventViewModel editTodaysEventViewModel) {
        this.todaysEventsViewModel = todaysEventsViewModel;
        this.addEventController = addEventController;
        this.addedEventViewModel = addedEventViewModel;
        this.deleteTodaysEventController = deleteTodaysEventController;
        this.editTodaysEventController = editTodaysEventController;
        this.editTodaysEventViewModel = editTodaysEventViewModel;

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Today's Events", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // 表头
        JPanel header = new JPanel(new GridLayout(1, 5));
        header.add(new JLabel("Name", SwingConstants.CENTER));
        header.add(new JLabel("Category", SwingConstants.CENTER));
        header.add(new JLabel("Due Date", SwingConstants.CENTER));
        header.add(new JLabel("Edit", SwingConstants.CENTER));
        header.add(new JLabel("Delete", SwingConstants.CENTER));
        add(header, BorderLayout.CENTER);

        // 列表区域
        todaysEventsListPanel.setLayout(new BoxLayout(todaysEventsListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(todaysEventsListPanel);
        add(scrollPane, BorderLayout.CENTER);

        // ViewModel监听器
        todaysEventsViewModel.addPropertyChangeListener(evt -> {
            if (TodaysEventsViewModel.TODAYS_EVENTS_PROPERTY.equals(evt.getPropertyName())) {
                refreshEventList((TodaysEventsState) evt.getNewValue());
            }
        });

        addedEventViewModel.addPropertyChangeListener(evt -> {
            if ("addedEvent".equals(evt.getPropertyName())) {
                AddedEventState state = addedEventViewModel.getState();
                if (state.getSelectedName() != null && !state.getSelectedName().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Event added: " + state.getSelectedName());
                }
            }
        });

        editTodaysEventViewModel.addPropertyChangeListener(evt -> {
            EditTodaysEventState state = editTodaysEventViewModel.getState();
            if (state.getEditError() != null) {
                JOptionPane.showMessageDialog(this, "Edit failed: " + state.getEditError(), "Edit Error", JOptionPane.ERROR_MESSAGE);
            } else if (state.getDueDate() != null) {
                JOptionPane.showMessageDialog(this, "Due date updated to " + state.getDueDate(), "Edit Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });


        // 初始加载
        refreshEventList(todaysEventsViewModel.getState());
    }

    private void refreshEventList(TodaysEventsState state) {
        todaysEventsListPanel.removeAll();

        List<Event> events = state.getTodaysEvents();

        if (events.isEmpty()) {
            JLabel emptyLabel = new JLabel("No available events.", SwingConstants.CENTER);
            emptyLabel.setPreferredSize(new Dimension(150, 250));
            todaysEventsListPanel.add(emptyLabel);
        }

        for (Event event : events) {
            JPanel row = new JPanel(new GridLayout(1, 5));
            row.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            row.add(new JLabel(event.getInfo().getName(), SwingConstants.CENTER));
            row.add(new JLabel(event.getInfo().getCategory(), SwingConstants.CENTER));
            row.add(new JLabel(String.valueOf(event.getBeginAndDueDates().getDueDate()), SwingConstants.CENTER));

            // Edit 按钮（暂留空逻辑）
            JButton editButton = new JButton("edit");
            editButton.addActionListener(e -> {
                // 创建弹窗
                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Due Date", true);
                dialog.setLayout(new BorderLayout());

                // 新建日期选择组件
                DueDatePickerPanel datePicker = new DueDatePickerPanel();
                dialog.add(datePicker, BorderLayout.CENTER);

                // 确认按钮
                JButton confirmButton = new JButton("Confirm");
                confirmButton.addActionListener(confirmEvent -> {
                    LocalDate newDate = datePicker.getSelectedDate();
                    if (newDate != null) {
                        editTodaysEventController.execute(event.getInfo().getId(), newDate.toString());
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Please select a due date.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                JPanel buttonPanel = new JPanel();
                buttonPanel.add(confirmButton);
                dialog.add(buttonPanel, BorderLayout.SOUTH);

                dialog.pack();
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
            });
            row.add(editButton);

            // ✅ Delete 按钮：调用 deleteTodaysEventController
            JButton deleteButton = new JButton("delete");
            deleteButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete \"" + event.getInfo().getName() + "\"?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteTodaysEventController.delete(event.getInfo().getId()); // ✅ 删除指令发出
                }
            });
            row.add(deleteButton);

            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            row.setPreferredSize(new Dimension(100, 40));
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            row.setBackground(new Color(245, 245, 245));

            todaysEventsListPanel.add(row);
        }

        todaysEventsListPanel.revalidate();
        todaysEventsListPanel.repaint();
    }
}


