package view.Alex.Event;

import interface_adapter.Alex.Event_related.available_event_module.delete_event.DeletedEventState;
import interface_adapter.Alex.Event_related.available_event_module.delete_event.DeletedEventViewModel;
import interface_adapter.Alex.Event_related.available_event_module.delete_event.DeleteEventController;
import interface_adapter.Alex.Event_related.available_event_module.available_event.AvailableEventState;
import interface_adapter.Alex.Event_related.available_event_module.available_event.AvailableEventViewModel;
import interface_adapter.Alex.Event_related.create_event.CreatedEventViewModel;
import interface_adapter.Alex.Event_related.create_event.CreatedEventState;
import interface_adapter.Alex.Event_related.available_event_module.edit_event.EditEventController;
import interface_adapter.Alex.Event_related.available_event_module.edit_event.EditedEventViewModel;
import interface_adapter.Alex.Event_related.available_event_module.edit_event.EditedEventState;

import entity.info.Info;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * View that displays all available events in a table format with edit/delete buttons.
 */
public class AvailableEventView extends JPanel {

    private final AvailableEventViewModel availableEventViewModel;
    private final DeleteEventController deleteEventController;
    private final DeletedEventViewModel deletedEventViewModel;
    private final CreatedEventViewModel createdEventViewModel;
    private final EditEventController editEventController;
    private final EditedEventViewModel editedEventViewModel;

    private final JPanel eventListPanel = new JPanel();

    public AvailableEventView(AvailableEventViewModel availableEventViewModel,
                              DeleteEventController deleteEventController,
                              DeletedEventViewModel deletedEventViewModel,
                              CreatedEventViewModel createdEventViewModel,
                              EditEventController editEventController,
                              EditedEventViewModel editedEventViewModel) {

        this.availableEventViewModel = availableEventViewModel;
        this.deleteEventController = deleteEventController;
        this.deletedEventViewModel = deletedEventViewModel;
        this.createdEventViewModel = createdEventViewModel;
        this.editEventController = editEventController;
        this.editedEventViewModel = editedEventViewModel;

        setLayout(new BorderLayout());

        // --- 顶部标题 ---
        JLabel title = new JLabel("Available Event", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));

        // --- 表头 ---
        JPanel header = new JPanel(new GridLayout(1, 5));
        header.add(new JLabel("Name", SwingConstants.CENTER));
        header.add(new JLabel("Description", SwingConstants.CENTER));
        header.add(new JLabel("Category", SwingConstants.CENTER));
        header.add(new JLabel("Edit", SwingConstants.CENTER));
        header.add(new JLabel("Delete", SwingConstants.CENTER));

        // ✅ 合并标题和表头
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(title);
        topPanel.add(header);
        add(topPanel, BorderLayout.PAGE_START);

        // --- 列表区域 ---
        eventListPanel.setLayout(new BoxLayout(eventListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(eventListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);  // 滚动区域占据剩余空间

        // === 注册监听 ===

        availableEventViewModel.addPropertyChangeListener(evt -> {
            System.out.println("AvailableEventView received property change: " + evt.getPropertyName());
            if (AvailableEventViewModel.AVAILABLE_EVENTS_PROPERTY.equals(evt.getPropertyName())) {
                refreshEventList((AvailableEventState) evt.getNewValue());
            }
        });

        createdEventViewModel.addPropertyChangeListener(evt -> {
            if ("createdEvent".equals(evt.getPropertyName())) {
                CreatedEventState state = createdEventViewModel.getState();
                if (state.getName() != null && !state.getName().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Event created: " + state.getName());
                }
            }
        });

        deletedEventViewModel.addPropertyChangeListener(evt -> {
            DeletedEventState state = deletedEventViewModel.getState();
            if (state.isDeletedSuccessfully()) {
                JOptionPane.showMessageDialog(this, "Deleted event: " + state.getDeletedEventName());
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed: " + state.getDeleteError());
            }
        });

        editedEventViewModel.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName())) {
                EditedEventState state = editedEventViewModel.getState();
                if (state.getEditError() != null && !state.getEditError().isEmpty()) {
                    JOptionPane.showMessageDialog(this, state.getEditError(), "Edit Failed", JOptionPane.ERROR_MESSAGE);
                    editedEventViewModel.clearError();
                } else if (state.getName() != null && !state.getName().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Event edited: " + state.getName());
                }
            }
        });

        // === 初始刷新 ===
        refreshEventList(availableEventViewModel.getState());
    }

    private void refreshEventList(AvailableEventState state) {
        eventListPanel.removeAll();

        List<Info> events = state.getAvailableEvents();
        System.out.println("Refreshing event list, count: " + events.size());

        if (events.isEmpty()) {
            JLabel emptyLabel = new JLabel("No available events.", SwingConstants.CENTER);
            emptyLabel.setPreferredSize(new Dimension(600, 250));
            eventListPanel.add(emptyLabel);
        }

        for (Info event : events) {
            JPanel row = new JPanel(new GridLayout(1, 5));
            row.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            row.add(new JLabel(event.getName(), SwingConstants.CENTER));
            row.add(new JLabel(event.getDescription(), SwingConstants.CENTER));
            row.add(new JLabel(event.getCategory(), SwingConstants.CENTER));

            JButton editButton = new JButton("edit");
            editButton.addActionListener(e -> {
                JTextField nameField = new JTextField(event.getName());
                JTextField categoryField = new JTextField(event.getCategory());
                JTextArea descriptionArea = new JTextArea(event.getDescription());
                JScrollPane descScroll = new JScrollPane(descriptionArea);
                descScroll.setPreferredSize(new Dimension(250, 80));

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Name:"));
                panel.add(nameField);
                panel.add(new JLabel("Description:"));
                panel.add(descScroll);
                panel.add(new JLabel("Category:"));
                panel.add(categoryField);

                int result = JOptionPane.showConfirmDialog(
                        this, panel, "Edit Event: " + event.getName(),
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
                );

                if (result == JOptionPane.OK_OPTION) {
                    editEventController.execute(
                            event.getId(),
                            nameField.getText(),
                            categoryField.getText(),
                            descriptionArea.getText()
                    );
                }
            });
            row.add(editButton);

            JButton deleteButton = new JButton("delete");
            deleteButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete \"" + event.getName() + "\"?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteEventController.delete(event.getId());
                }
            });
            row.add(deleteButton);

            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            row.setPreferredSize(new Dimension(600, 40));
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            row.setBackground(new Color(245, 245, 245));

            eventListPanel.add(row);
        }

        eventListPanel.revalidate();
        eventListPanel.repaint();
    }
}

