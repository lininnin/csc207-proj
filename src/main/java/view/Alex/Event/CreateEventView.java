package view.Alex.Event;

import entity.info.InfoInterf;
import interface_adapter.alex.event_related.add_event.AddedEventState;
import interface_adapter.alex.event_related.add_event.AddedEventViewModel;
import interface_adapter.alex.event_related.create_event.CreateEventController;
import interface_adapter.alex.event_related.create_event.CreatedEventViewModel;
import interface_adapter.alex.event_related.create_event.CreatedEventState;
import interface_adapter.alex.event_related.create_event.CreateEventViewModelUpdateListener;
import view.LabelComponentPanel;
import view.FontUtil;
import entity.Category;
import use_case.Angela.category.CategoryGateway;

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

import entity.info.Info;
import data_access.EventAvailableDataAccessObject;

public class CreateEventView extends JPanel implements PropertyChangeListener, CreateEventViewModelUpdateListener {

    private final String viewName = "New Available Event";

    private final CreatedEventViewModel createdEventViewModel;
    private final JTextField nameInputField = new JTextField(8);
    private final JComboBox<CategoryItem> categoryComboBox = new JComboBox<>();
    private final JTextArea descriptionInputArea = new JTextArea(2, 8);
    private final JCheckBox oneTimeCheckbox = new JCheckBox();
    private final JButton create;
    private JButton manageCategoriesButton;
    private CreateEventController createEventController;

    // 可选：注入以支持 AddEventViewModel 同步
    private AddedEventViewModel addedEventViewModel;
    private final EventAvailableDataAccessObject availableDAO;
    private final CategoryGateway categoryGateway;

    public CreateEventView(CreatedEventViewModel createdEventViewModel,
                           AddedEventViewModel addedEventViewModel,
                           EventAvailableDataAccessObject availableDAO,
                           CategoryGateway categoryGateway) {
        this.createdEventViewModel = createdEventViewModel;
        this.addedEventViewModel = addedEventViewModel;
        this.availableDAO = availableDAO;
        this.categoryGateway = categoryGateway;

        createdEventViewModel.addPropertyChangeListener(this);
        createdEventViewModel.addListener(this);

        this.setMaximumSize(new Dimension(150, 70));
        this.setPreferredSize(new Dimension(150, 70));

        final JLabel title = new JLabel(CreatedEventViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        nameInputField.setMaximumSize(new Dimension(100, 25));
        nameInputField.setFont(FontUtil.getStandardFont());

        // Setup category dropdown with manage button
        categoryComboBox.setPreferredSize(new Dimension(80, 25));
        categoryComboBox.setFont(FontUtil.getStandardFont());
        categoryComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof CategoryItem) {
                    setText(((CategoryItem) value).toString());
                }
                setFont(FontUtil.getStandardFont());
                if (!isSelected) {
                    setForeground(Color.BLACK);
                    setBackground(Color.WHITE);
                }
                return this;
            }
        });

        manageCategoriesButton = new JButton("Manage");
        manageCategoriesButton.setFont(FontUtil.getStandardFont());
        manageCategoriesButton.setPreferredSize(new Dimension(60, 25));
        manageCategoriesButton.addActionListener(e -> openCategoryManagement());

        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        categoryPanel.add(categoryComboBox);
        categoryPanel.add(manageCategoriesButton);

        descriptionInputArea.setLineWrap(true);
        descriptionInputArea.setWrapStyleWord(true);
        descriptionInputArea.setFont(FontUtil.getStandardFont());

        LabelComponentPanel nameInfo = new LabelComponentPanel(
                new JLabel(CreatedEventViewModel.NAME_LABEL), nameInputField);
//        LabelComponentPanel oneTimeInfo = new LabelComponentPanel(
//                new JLabel(CreatedEventViewModel.ONE_TIME_LABEL), oneTimeCheckbox);
        LabelComponentPanel categoryInfo = new LabelComponentPanel(
                new JLabel(CreatedEventViewModel.CATEGORY_LABEL), categoryPanel);
        LabelComponentPanel descriptionInfo = new LabelComponentPanel(
                new JLabel(CreatedEventViewModel.DESCRIPTION_LABEL),
                new JScrollPane(descriptionInputArea));

        JPanel row1 = new JPanel(new GridLayout(1, 2, 5, 1));
        row1.add(nameInfo);
        //row1.add(oneTimeInfo);

        JPanel row2 = new JPanel(new GridLayout(1, 2, 5, 1));
        row2.add(categoryInfo);
        row2.add(descriptionInfo);

        JPanel buttons = new JPanel();
        create = new JButton(CreatedEventViewModel.CREATE_EVENT_INFO_LABEL);
        create.setPreferredSize(new Dimension(120, 25));
        buttons.add(create);

        create.addActionListener(evt -> {
            CategoryItem selectedCategory = (CategoryItem) categoryComboBox.getSelectedItem();
            String categoryId = selectedCategory != null ? selectedCategory.getId() : "";

            createEventController.execute(
                    UUID.randomUUID().toString(),
                    nameInputField.getText(),
                    descriptionInputArea.getText(),
                    categoryId,
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

        // Load categories on initialization
        loadCategories();
    }

    private void openCategoryManagement() {
        firePropertyChange("openCategoryManagement", null, null);
    }

    public void refreshCategories() {
        loadCategories();
    }

    private void loadCategories() {
        categoryComboBox.removeAllItems();
        categoryComboBox.addItem(new CategoryItem("", "-- No Category --"));

        for (Category category : categoryGateway.getAllCategories()) {
            categoryComboBox.addItem(new CategoryItem(category.getId(), category.getName()));
        }
    }

    private void addFieldListeners() {
        nameInputField.getDocument().addDocumentListener(new DocumentAdapter() {
            public void update(DocumentEvent e) {
                CreatedEventState state = createdEventViewModel.getState();
                state.setName(nameInputField.getText());
                createdEventViewModel.setState(state);
            }
        });

        categoryComboBox.addActionListener(e -> {
            CategoryItem selectedItem = (CategoryItem) categoryComboBox.getSelectedItem();
            if (selectedItem != null) {
                CreatedEventState state = createdEventViewModel.getState();
                state.setCategory(selectedItem.getId());
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

//        oneTimeCheckbox.addItemListener(e -> {
//            CreatedEventState state = createdEventViewModel.getState();
//            state.setOneTime(oneTimeCheckbox.isSelected());
//            createdEventViewModel.setState(state);
//        });
    }

    @Override
    public void onViewModelUpdated() {
        CreatedEventState state = createdEventViewModel.getState();
        if (!nameInputField.getText().equals(state.getName())) {
            nameInputField.setText(state.getName());
        }
        // Update category selection if needed
        String categoryId = state.getCategory();
        if (categoryId != null) {
            for (int i = 0; i < categoryComboBox.getItemCount(); i++) {
                CategoryItem item = categoryComboBox.getItemAt(i);
                if (item.getId().equals(categoryId)) {
                    categoryComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
        if (!descriptionInputArea.getText().equals(state.getDescription())) {
            descriptionInputArea.setText(state.getDescription());
        }
//        if (oneTimeCheckbox.isSelected() != state.isOneTime()) {
//            oneTimeCheckbox.setSelected(state.isOneTime());
//        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!CreatedEventViewModel.CREATED_EVENT_STATE_PROPERTY.equals(evt.getPropertyName())) return;

        CreatedEventState state = (CreatedEventState) evt.getNewValue();

        // 错误弹窗
        String errorMsg = state.getNameError();
        if (errorMsg != null && !errorMsg.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, errorMsg);
        }

        // ✅ 更新 AddEventViewModel 中的名称（同步更新）
        List<String> availableNames = availableDAO.getAllEvents().stream()
                .map(InfoInterf::getName)
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

    // Inner class to hold category information
    private static class CategoryItem {
        private final String id;
        private final String name;

        public CategoryItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}