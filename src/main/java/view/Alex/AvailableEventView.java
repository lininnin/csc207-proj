package view.Alex;

import interface_adapter.Alex.delete_event.DeletedEventState;
import interface_adapter.Alex.delete_event.DeletedEventViewModel;
import interface_adapter.Alex.delete_event.DeleteEventController;
import interface_adapter.Alex.available_event.AvailableEventState;
import interface_adapter.Alex.available_event.AvailableEventViewModel;
import interface_adapter.Alex.create_event.CreatedEventViewModel;
import interface_adapter.Alex.create_event.CreatedEventState;

import entity.Info.Info;

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

    private final JPanel eventListPanel = new JPanel();

    public AvailableEventView(AvailableEventViewModel availableEventViewModel,
                              DeleteEventController deleteEventController,
                              DeletedEventViewModel deletedEventViewModel,
                              CreatedEventViewModel createdEventViewModel) {

        this.availableEventViewModel = availableEventViewModel;
        this.deleteEventController = deleteEventController;
        this.deletedEventViewModel = deletedEventViewModel;
        this.createdEventViewModel = createdEventViewModel;

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Available Event", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // 表头
        JPanel header = new JPanel(new GridLayout(1, 5));
        header.add(new JLabel("Name", SwingConstants.CENTER));
        header.add(new JLabel("Category", SwingConstants.CENTER));
        header.add(new JLabel("Description", SwingConstants.CENTER));
        header.add(new JLabel("Edit", SwingConstants.CENTER));
        header.add(new JLabel("Delete", SwingConstants.CENTER));
        add(header, BorderLayout.CENTER);

        // 列表区域
        eventListPanel.setLayout(new BoxLayout(eventListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(eventListPanel);
        add(scrollPane, BorderLayout.SOUTH);

        // ✅ 修复后的监听 ViewModel 更新 —— 监听 "state"
        availableEventViewModel.addPropertyChangeListener(evt -> {
            refreshEventList(availableEventViewModel.getState());
        });

        // ✅ 创建成功弹窗提示
        createdEventViewModel.addPropertyChangeListener(evt -> {
            if ("createdEvent".equals(evt.getPropertyName())) {
                CreatedEventState state = createdEventViewModel.getState();
                if (state.getName() != null && !state.getName().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Event created: " + state.getName());
                }
            }
        });

        // 删除操作结果监听
        deletedEventViewModel.addPropertyChangeListener(evt -> {
            DeletedEventState state = deletedEventViewModel.getState();
            if (state.isDeletedSuccessfully()) {
                JOptionPane.showMessageDialog(this, "Deleted event: " + state.getDeletedEventName());
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed: " + state.getDeleteError());
            }
        });

        // 初始加载
        refreshEventList(availableEventViewModel.getState());
    }

    private void refreshEventList(AvailableEventState state) {
        eventListPanel.removeAll();
        List<Info> events = state.getAvailableEvents();

        for (Info event : events) {
            JPanel row = new JPanel(new GridLayout(1, 5));
            row.add(new JLabel(event.getName(), SwingConstants.CENTER));
            row.add(new JLabel(event.getCategory(), SwingConstants.CENTER));
            row.add(new JLabel(event.getDescription(), SwingConstants.CENTER));

            JButton editButton = new JButton("edit");
            editButton.addActionListener(e -> {
                System.out.println("Edit " + event.getName());
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

            eventListPanel.add(row);
        }

        eventListPanel.revalidate();
        eventListPanel.repaint();
    }
}




