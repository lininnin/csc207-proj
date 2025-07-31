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

/**
 * View for creating a new task.
 * Follows the same pattern as other views in the project.
 */
public class CreateTaskView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "create task";

    private final CreateTaskViewModel createTaskViewModel;
    private final CategoryGateway categoryGateway;

    // UI Components
    private final JTextField taskNameField = new JTextField(20);
    private final JTextArea descriptionArea = new JTextArea(3, 20);
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
    }

    private void initializeUI() {
        // Main form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Task Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Task Name:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(taskNameField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setPreferredSize(new Dimension(300, 60));
        formPanel.add(scrollPane, gbc);

        // Category row with manage button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Category:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel categoryPanel = new JPanel(new BorderLayout());
        categoryPanel.add(categoryComboBox, BorderLayout.CENTER);

        manageCategoriesButton = new JButton("Manage");
        manageCategoriesButton.addActionListener(e -> openCategoryManagement());
        categoryPanel.add(manageCategoriesButton, BorderLayout.EAST);

        formPanel.add(categoryPanel, gbc);

        // One-Time Task
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(oneTimeCheckBox, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(createButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Error label
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        errorLabel.setForeground(Color.RED);
        formPanel.add(errorLabel, gbc);

        // Add listeners
        createButton.addActionListener(this);
        clearButton.addActionListener(e -> clearForm());

        // Add to main panel
        add(formPanel, BorderLayout.CENTER);
    }

    private void openCategoryManagement() {
        // This will be wired up in TaskPageBuilder
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
        if (evt.getSource() == createButton && createTaskController != null) {
            CategoryItem selectedCategory = (CategoryItem) categoryComboBox.getSelectedItem();
            String categoryId = selectedCategory != null ? selectedCategory.getId() : "";

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
        CreateTaskState state = (CreateTaskState) evt.getNewValue();

        if (state.getError() != null) {
            errorLabel.setText(state.getError());
            errorLabel.setForeground(Color.RED);
        } else if (state.getSuccessMessage() != null) {
            errorLabel.setText(state.getSuccessMessage());
            errorLabel.setForeground(new Color(0, 128, 0));
            clearForm();
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