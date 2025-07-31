package view.Angela.Category;

import entity.Category;
import interface_adapter.Angela.category.*;
import interface_adapter.Angela.category.create.CreateCategoryController;
import interface_adapter.Angela.category.delete.DeleteCategoryController;
import interface_adapter.Angela.category.edit.EditCategoryController;
import use_case.Angela.category.CategoryGateway;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Popup dialog for managing categories.
 * Follows the design spec with create, edit, and delete functionality.
 */
public class CategoryManagementDialog extends JDialog implements PropertyChangeListener {
    private final CategoryGateway categoryGateway;
    private final CategoryManagementViewModel viewModel;

    // Controllers
    private CreateCategoryController createController;
    private DeleteCategoryController deleteController;
    private EditCategoryController editController;

    // UI Components
    private JTextField nameField;
    private JButton createButton;
    private JButton saveButton;
    private JButton cancelButton;
    private DefaultTableModel tableModel;
    private JTable categoryTable;
    private JLabel messageLabel;

    // State
    private String editingCategoryId = null;

    // Listener for category changes
    private CategoryChangeListener categoryChangeListener;

    public CategoryManagementDialog(JFrame parent, CategoryGateway categoryGateway,
                                    CategoryManagementViewModel viewModel) {
        super(parent, "Category Management", true);
        this.categoryGateway = categoryGateway;
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        initializeUI();
        loadCategories();

        setSize(500, 400);
        setLocationRelativeTo(parent);
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Top panel - Create new category
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("New Category"));

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField(15);
        inputPanel.add(nameField);

        createButton = new JButton("Create");
        createButton.addActionListener(e -> createCategory());
        inputPanel.add(createButton);

        topPanel.add(inputPanel, BorderLayout.CENTER);

        // Message label
        messageLabel = new JLabel(" ");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(messageLabel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Center - Category table
        setupTable();
        JScrollPane scrollPane = new JScrollPane(categoryTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Existing Categories"));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom - Close button
        JPanel bottomPanel = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupTable() {
        String[] columnNames = {"Name", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; // Only actions column is editable
            }
        };

        categoryTable = new JTable(tableModel);
        categoryTable.setRowHeight(30);
        categoryTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        categoryTable.getColumnModel().getColumn(1).setCellRenderer(new ButtonRenderer());
        categoryTable.getColumnModel().getColumn(1).setCellEditor(new ButtonEditor());
    }

    private void createCategory() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showMessage("Please enter a category name", true);
            return;
        }

        if (createController != null) {
            createController.execute(name);
        }
    }

    private void loadCategories() {
        tableModel.setRowCount(0);
        for (Category category : categoryGateway.getAllCategories()) {
            tableModel.addRow(new Object[]{category.getName(), category});
        }
    }

    public void setControllers(CreateCategoryController createController,
                               DeleteCategoryController deleteController,
                               EditCategoryController editController) {
        this.createController = createController;
        this.deleteController = deleteController;
        this.editController = editController;
    }

    public void setCategoryChangeListener(CategoryChangeListener listener) {
        this.categoryChangeListener = listener;
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setForeground(isError ? Color.RED : Color.GREEN);

        // Clear message after 3 seconds
        Timer timer = new Timer(3000, e -> messageLabel.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            CategoryManagementState state = (CategoryManagementState) evt.getNewValue();

            if (state.getError() != null) {
                showMessage(state.getError(), true);
            } else if (state.getMessage() != null) {
                showMessage(state.getMessage(), false);
                nameField.setText("");
                loadCategories();

                // Notify listeners of category changes
                if (categoryChangeListener != null) {
                    categoryChangeListener.onCategoryChanged();
                }
            }
        }
    }

    // Button renderer
    private class ButtonRenderer extends JPanel implements TableCellRenderer {
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            removeAll();

            JButton editBtn = new JButton("Edit");
            JButton deleteBtn = new JButton("Delete");

            editBtn.setMargin(new Insets(2, 5, 2, 5));
            deleteBtn.setMargin(new Insets(2, 5, 2, 5));

            add(editBtn);
            add(deleteBtn);

            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

            return this;
        }
    }

    // Button editor
    private class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private Category currentCategory;

        public ButtonEditor() {
            super(new JCheckBox());
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            panel.removeAll();
            currentCategory = (Category) value;

            JButton editBtn = new JButton("Edit");
            JButton deleteBtn = new JButton("Delete");

            editBtn.setMargin(new Insets(2, 5, 2, 5));
            deleteBtn.setMargin(new Insets(2, 5, 2, 5));

            editBtn.addActionListener(e -> {
                editCategory(currentCategory);
                fireEditingStopped();
            });

            deleteBtn.addActionListener(e -> {
                deleteCategory(currentCategory);
                fireEditingStopped();
            });

            panel.add(editBtn);
            panel.add(deleteBtn);

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return currentCategory;
        }

        private void editCategory(Category category) {
            String newName = JOptionPane.showInputDialog(
                    CategoryManagementDialog.this,
                    "Enter new name for category:",
                    category.getName()
            );

            if (newName != null && editController != null) {
                editController.execute(category.getId(), newName);
            }
        }

        private void deleteCategory(Category category) {
            int result = JOptionPane.showConfirmDialog(
                    CategoryManagementDialog.this,
                    "Delete category '" + category.getName() + "'?\n" +
                            "All tasks with this category will have empty category.",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION && deleteController != null) {
                deleteController.execute(category.getId());
            }
        }
    }

    // Interface for listening to category changes
    public interface CategoryChangeListener {
        void onCategoryChanged();
    }
}