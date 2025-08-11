package view.Alex.Event;

import entity.Alex.Event.EventInterf;
import interface_adapter.alex.event_related.add_event.AddEventController;
import interface_adapter.alex.event_related.add_event.AddedEventState;
import interface_adapter.alex.event_related.add_event.AddedEventViewModel;
import interface_adapter.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventController;
import interface_adapter.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventState;
import interface_adapter.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventViewModel;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsState;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;
import interface_adapter.alex.event_related.todays_events_module.delete_todays_event.DeleteTodaysEventController;
import view.DueDatePickerPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * View that displays today's events in a table format with edit/delete buttons.
 * Now uses EventInterf instead of the concrete Event class.
 */
public class TodaysEventsView extends JPanel {

    private final TodaysEventsViewModel todaysEventsViewModel;
    private final AddEventController addEventController;
    private final AddedEventViewModel addedEventViewModel;
    private final DeleteTodaysEventController deleteTodaysEventController;
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

        JPanel listContainer = new JPanel(new BorderLayout());

        JPanel header = new JPanel(new GridLayout(1, 5));
        header.add(new JLabel("Name", SwingConstants.CENTER));
        header.add(new JLabel("Category", SwingConstants.CENTER));
        header.add(new JLabel("Due Date", SwingConstants.CENTER));
        header.add(new JLabel("Edit", SwingConstants.CENTER));
        header.add(new JLabel("Delete", SwingConstants.CENTER));
        listContainer.add(header, BorderLayout.NORTH);

        todaysEventsListPanel.setLayout(new BoxLayout(todaysEventsListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(todaysEventsListPanel);
        listContainer.add(scrollPane, BorderLayout.CENTER);

        add(listContainer, BorderLayout.CENTER);

        todaysEventsViewModel.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName())) {
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
                JOptionPane.showMessageDialog(this, "Edit failed: " + state.getEditError(),
                        "Edit Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        refreshEventList(todaysEventsViewModel.getState());
    }

    private void refreshEventList(TodaysEventsState state) {
        todaysEventsListPanel.removeAll();

        List<EventInterf> events = state.getTodaysEvents();

        if (events.isEmpty()) {
            JLabel emptyLabel = new JLabel("No available events.", SwingConstants.CENTER);
            emptyLabel.setPreferredSize(new Dimension(150, 250));
            todaysEventsListPanel.add(emptyLabel);
        }

        for (EventInterf event : events) {
            JPanel row = new JPanel(new GridLayout(1, 5));
            row.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            row.add(new JLabel(event.getInfo().getName(), SwingConstants.CENTER));
            row.add(new JLabel(event.getInfo().getCategory(), SwingConstants.CENTER));
            row.add(new JLabel(String.valueOf(event.getBeginAndDueDates().getDueDate()), SwingConstants.CENTER));

            JButton editButton = new JButton("edit");
            editButton.addActionListener(e -> {
                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                        "Edit Due Date", true);
                dialog.setLayout(new BorderLayout());

                DueDatePickerPanel datePicker = new DueDatePickerPanel();
                dialog.add(datePicker, BorderLayout.CENTER);

                JButton confirmButton = new JButton("Confirm");
                confirmButton.addActionListener(confirmEvent -> {
                    LocalDate newDate = datePicker.getSelectedDate();
                    if (newDate != null) {
                        editTodaysEventController.execute(event.getInfo().getId(), newDate.toString());
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Please select a due date.",
                                "Input Error", JOptionPane.ERROR_MESSAGE);
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

            JButton deleteButton = new JButton("delete");
            deleteButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete \"" + event.getInfo().getName() + "\"?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteTodaysEventController.delete(event.getInfo().getId());
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

