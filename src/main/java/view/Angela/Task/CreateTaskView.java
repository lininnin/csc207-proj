package view.Angela.Task;

import interface_adapter.Angela.task.create.CreateTaskController;
import interface_adapter.Angela.task.create.CreateTaskState;
import interface_adapter.Angela.task.create.CreateTaskViewModel;
import use_case.Angela.category.CategoryGateway;
import entity.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import view.FontUtil;

/**
 * View for creating a new task.
 * Follows the same pattern as other views in the project.
 */
public class CreateTaskView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "create task";

    private final CreateTaskViewModel createTaskViewModel;
    private final CategoryGateway categoryGateway;

    // UI Components
    private final JTextField taskNameField = new JTextField();
    private final JTextArea descriptionArea = new JTextArea();
    private final JComboBox<CategoryItem> categoryComboBox = new JComboBox<>();
    private final JCheckBox oneTimeCheckBox = new JCheckBox("One-Time Task");
    private final JButton createButton = new JButton("Create Task");
    private final JButton clearButton = new JButton("Clear");
    private final JLabel errorLabel = new JLabel(" ");
    private JButton manageCategoriesButton;

    private CreateTaskController createTaskController;

    public CreateTaskView(CreateTaskViewModel createTaskViewModel, CategoryGateway categoryGateway) {
        this.createTaskViewModel = createTaskViewModel;
        this.categoryGateway = categoryGateway;
        this.createTaskViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Create New Task"));

        initializeUI();
        loadCategories();
        
        // Fix font for buttons to prevent missing W, E, L, F
        createButton.setFont(FontUtil.getStandardFont());
        clearButton.setFont(FontUtil.getStandardFont());
        oneTimeCheckBox.setFont(FontUtil.getStandardFont());
    }

    private void initializeUI() {
        // Main form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Task Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Task Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        taskNameField.setPreferredSize(new Dimension(250, 25));
        taskNameField.setFont(FontUtil.getStandardFont()); // Fix font for input fields
        formPanel.add(taskNameField, gbc);

        // Category with manage button
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPanel categoryPanel = new JPanel(new BorderLayout(5, 0));
        categoryComboBox.setPreferredSize(new Dimension(200, 25)); // Increased width
        categoryComboBox.setFont(FontUtil.getStandardFont()); // Fix font for selected value display
        // Fix rendering issue - ensure ALL items display with black text
        categoryComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof CategoryItem) {
                    String text = ((CategoryItem) value).toString();
                    setText(text);
                }
                
                setFont(FontUtil.getStandardFont());
                // CRITICAL: Force black text for ALL items to fix white text bug
                if (!isSelected) {
                    setForeground(Color.BLACK);
                    setBackground(Color.WHITE);
                }
                return this;
            }
        });
        categoryPanel.add(categoryComboBox, BorderLayout.CENTER);
        manageCategoriesButton = new JButton("Manage");
        manageCategoriesButton.setFont(FontUtil.getStandardFont()); // Fix font
        manageCategoriesButton.addActionListener(e -> openCategoryManagement());
        categoryPanel.add(manageCategoriesButton, BorderLayout.EAST);
        formPanel.add(categoryPanel, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        descriptionArea.setRows(3);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(FontUtil.getStandardFont()); // Fix font for text area
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setPreferredSize(new Dimension(250, 60));
        formPanel.add(scrollPane, gbc);

        // One-time checkbox
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        formPanel.add(oneTimeCheckBox, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(createButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        // Error label
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        errorLabel.setForeground(Color.RED);
        formPanel.add(errorLabel, gbc);

        // Add listeners
        createButton.addActionListener(this);
        clearButton.addActionListener(e -> clearForm());

        // Add to main panel
        add(formPanel, BorderLayout.CENTER);
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

    public void setCreateTaskController(CreateTaskController controller) {
        this.createTaskController = controller;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        System.out.println("DEBUG: Create button clicked");
        if (evt.getSource() == createButton && createTaskController != null) {
            CategoryItem selectedCategory = (CategoryItem) categoryComboBox.getSelectedItem();
            String categoryId = selectedCategory != null ? selectedCategory.getId() : "";

            System.out.println("DEBUG: Executing create task with name: " + taskNameField.getText());
            createTaskController.execute(
                    taskNameField.getText(),
                    descriptionArea.getText(),
                    categoryId,
                    oneTimeCheckBox.isSelected()
            );
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("DEBUG: CreateTaskView received property change: " + evt.getPropertyName());
        if (CreateTaskViewModel.CREATE_TASK_STATE_PROPERTY.equals(evt.getPropertyName())) {
            CreateTaskState state = (CreateTaskState) evt.getNewValue();

            if (state.getError() != null) {
                System.out.println("DEBUG: Showing error: " + state.getError());
                errorLabel.setText(state.getError());
                errorLabel.setForeground(Color.RED);
            } else if (state.getSuccessMessage() != null) {
                System.out.println("DEBUG: Showing success: " + state.getSuccessMessage());
                errorLabel.setText(state.getSuccessMessage());
                errorLabel.setForeground(new Color(0, 128, 0));
                clearForm();
            }
        }
    }

    private void clearForm() {
        taskNameField.setText("");
        descriptionArea.setText("");
        categoryComboBox.setSelectedIndex(0);
        oneTimeCheckBox.setSelected(false);
        errorLabel.setText(" ");
    }

    // Helper class for ComboBox items
    private static class CategoryItem {
        private final String id;
        private final String name;

        public CategoryItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() { return id; }

        @Override
        public String toString() { return name; }
    }

    public String getViewName() { return viewName; }
}