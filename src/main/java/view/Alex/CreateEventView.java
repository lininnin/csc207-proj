package view.Alex;

import interface_adapter.Alex.add_event.AddedEventState;
import interface_adapter.Alex.add_event.AddedEventViewModel;
import interface_adapter.Alex.create_event.CreateEventController;
import interface_adapter.Alex.create_event.CreatedEventViewModel;
import interface_adapter.Alex.create_event.CreatedEventState;
import interface_adapter.Alex.create_event.CreateEventViewModelUpdateListener;
import view.LabelComponentPanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

import entity.Info.Info;
import data_access.EventAvailableDataAccessObject;

public class CreateEventView extends JPanel implements PropertyChangeListener, CreateEventViewModelUpdateListener {

    private final String viewName = "New Available Event";

    private final CreatedEventViewModel createdEventViewModel;
    private final JTextField nameInputField = new JTextField(8);
    private final JTextField categoryInputField = new JTextField(8);
    private final JTextArea descriptionInputArea = new JTextArea(2, 8);
    private final JCheckBox oneTimeCheckbox = new JCheckBox();
    private final JButton create;
    private CreateEventController createEventController;

    // 可选：注入以支持 AddEventViewModel 同步
    private AddedEventViewModel addedEventViewModel;
    private final EventAvailableDataAccessObject availableDAO;

    public CreateEventView(CreatedEventViewModel createdEventViewModel,
                           AddedEventViewModel addedEventViewModel,
                           EventAvailableDataAccessObject availableDAO) {
        this.createdEventViewModel = createdEventViewModel;
        this.addedEventViewModel = addedEventViewModel;
        this.availableDAO = availableDAO;

        createdEventViewModel.addPropertyChangeListener(this);
        createdEventViewModel.addListener(this);

        this.setMaximumSize(new Dimension(150, 70));
        this.setPreferredSize(new Dimension(150, 70));

        final JLabel title = new JLabel(CreatedEventViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        nameInputField.setMaximumSize(new Dimension(100, 25));
        categoryInputField.setMaximumSize(new Dimension(100, 25));
        descriptionInputArea.setLineWrap(true);
        descriptionInputArea.setWrapStyleWord(true);

        LabelComponentPanel nameInfo = new LabelComponentPanel(
                new JLabel(CreatedEventViewModel.NAME_LABEL), nameInputField);
        LabelComponentPanel oneTimeInfo = new LabelComponentPanel(
                new JLabel(CreatedEventViewModel.ONE_TIME_LABEL), oneTimeCheckbox);
        LabelComponentPanel categoryInfo = new LabelComponentPanel(
                new JLabel(CreatedEventViewModel.CATEGORY_LABEL), categoryInputField);
        LabelComponentPanel descriptionInfo = new LabelComponentPanel(
                new JLabel(CreatedEventViewModel.DESCRIPTION_LABEL),
                new JScrollPane(descriptionInputArea));

        JPanel row1 = new JPanel(new GridLayout(1, 2, 5, 1));
        row1.add(nameInfo);
        row1.add(oneTimeInfo);

        JPanel row2 = new JPanel(new GridLayout(1, 2, 5, 1));
        row2.add(categoryInfo);
        row2.add(descriptionInfo);

        JPanel buttons = new JPanel();
        create = new JButton(CreatedEventViewModel.CREATE_EVENT_INFO_LABEL);
        create.setPreferredSize(new Dimension(120, 25));
        buttons.add(create);

        create.addActionListener(evt -> {
            createEventController.execute(
                    UUID.randomUUID().toString(),
                    nameInputField.getText(),
                    categoryInputField.getText(),
                    descriptionInputArea.getText(),
                    LocalDate.now()
            );
        });

        addFieldListeners();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 1, 5));
        this.add(title);
        this.add(row1);
        this.add(Box.createVerticalStrut(1));
        this.add(row2);
        this.add(Box.createVerticalStrut(1));
        this.add(buttons);
    }

    private void addFieldListeners() {
        nameInputField.getDocument().addDocumentListener(new DocumentAdapter() {
            public void update(DocumentEvent e) {
                CreatedEventState state = createdEventViewModel.getState();
                state.setName(nameInputField.getText());
                createdEventViewModel.setState(state);
            }
        });

        categoryInputField.getDocument().addDocumentListener(new DocumentAdapter() {
            public void update(DocumentEvent e) {
                CreatedEventState state = createdEventViewModel.getState();
                state.setCategory(categoryInputField.getText());
                createdEventViewModel.setState(state);
            }
        });

        descriptionInputArea.getDocument().addDocumentListener(new DocumentAdapter() {
            public void update(DocumentEvent e) {
                CreatedEventState state = createdEventViewModel.getState();
                state.setDescription(descriptionInputArea.getText());
                createdEventViewModel.setState(state);
            }
        });

        oneTimeCheckbox.addItemListener(e -> {
            CreatedEventState state = createdEventViewModel.getState();
            state.setOneTime(oneTimeCheckbox.isSelected());
            createdEventViewModel.setState(state);
        });
    }

    @Override
    public void onViewModelUpdated() {
        CreatedEventState state = createdEventViewModel.getState();
        if (!nameInputField.getText().equals(state.getName())) {
            nameInputField.setText(state.getName());
        }
        if (!categoryInputField.getText().equals(state.getCategory())) {
            categoryInputField.setText(state.getCategory());
        }
        if (!descriptionInputArea.getText().equals(state.getDescription())) {
            descriptionInputArea.setText(state.getDescription());
        }
        if (oneTimeCheckbox.isSelected() != state.isOneTime()) {
            oneTimeCheckbox.setSelected(state.isOneTime());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        CreatedEventState state = (CreatedEventState) evt.getNewValue();
        String errorMsg = state.getNameError();
        if (errorMsg != null && !errorMsg.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, errorMsg);
        }

        // ✅ 同步 AddEventViewModel 可用的事件名称
        List<String> availableNames = availableDAO.getAllEvents().stream()
                .map(Info::getName)
                .collect(Collectors.toList());
        AddedEventState addState = addedEventViewModel.getState();
        addState.setAvailableNames(availableNames);
        addedEventViewModel.setState(addState);
    }

    public String getViewName() {
        return viewName;
    }

    public void setCreateEventController(CreateEventController controller) {
        this.createEventController = controller;
    }

    private abstract static class DocumentAdapter implements DocumentListener {
        public abstract void update(DocumentEvent e);
        public void insertUpdate(DocumentEvent e) { update(e); }
        public void removeUpdate(DocumentEvent e) { update(e); }
        public void changedUpdate(DocumentEvent e) { update(e); }
    }
}
