package view.Task;

import entity.Angela.CategoryManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Dialog for selecting, creating, editing, and deleting categories.
 * Implements the category popup as shown in the GUI mockup.
 */
public class CategorySelectionDialog extends JDialog {
    private final CategoryManager categoryManager;
    private String selectedCategory;
    private final JList<String> categoryList;
    private final DefaultListModel<String> listModel;
    private final JTextField categoryField;

    /**
     * Creates a new category selection dialog.
     *
     * @param parent The parent frame
     * @param currentCategory The currently selected category (can be null)
     * @param categoryField The text field to update with selected category
     */
    public CategorySelectionDialog(Frame parent, String currentCategory, JTextField categoryField) {
        super(parent, "Select Category", true);
        this.categoryManager = CategoryManager.getInstance();
        this.selectedCategory = currentCategory;
        this.categoryField = categoryField;
        this.listModel = new DefaultListModel<>();
        this.categoryList = new JList<>(listModel);

        initializeUI();
        loadCategories();

        // Select current category if exists
        if (currentCategory != null && categoryManager.hasCategory(currentCategory)) {
            categoryList.setSelectedValue(currentCategory, true);
        }
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(300, 400);
        setLocationRelativeTo(getParent());

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel("Category:");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Category list
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryList.setFont(new Font("Arial", Font.PLAIN, 12));
        categoryList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    selectCategory();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(categoryList);
        scrollPane.setPreferredSize(new Dimension(250, 200));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Add category change listener
        categoryManager.addCategoryChangeListener(new CategoryManager.CategoryChangeListener() {
            @Override
            public void onCategoryAdded(String category) {
                loadCategories();
            }

            @Override
            public void onCategoryRemoved(String category) {
                loadCategories();
            }

            @Override
            public void onCategoriesLoaded(java.util.Set<String> categories) {
                loadCategories();
            }
        });
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

        // Create buttons matching GUI mockup
        JButton newButton = new JButton("new");
        JButton editButton = new JButton("edit");
        JButton deleteButton = new JButton("delete");
        JButton selectButton = new JButton("select");

        // Style buttons
        Dimension buttonSize = new Dimension(80, 30);
        for (JButton button : new JButton[]{newButton, editButton, deleteButton, selectButton}) {
            button.setPreferredSize(buttonSize);
            button.setFont(new Font("Arial", Font.PLAIN, 12));
        }

        // Button actions
        newButton.addActionListener(e -> createNewCategory());
        editButton.addActionListener(e -> editCategory());
        deleteButton.addActionListener(e -> deleteCategory());
        selectButton.addActionListener(e -> selectCategory());

        // Add buttons to panel
        panel.add(newButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(selectButton);

        return panel;
    }

    private void loadCategories() {
        listModel.clear();
        List<String> categories = categoryManager.getCategoriesList();
        for (String category : categories) {
            listModel.addElement(category);
        }
    }

    private void createNewCategory() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel("Category name:"), BorderLayout.WEST);
        JTextField nameField = new JTextField(20);
        panel.add(nameField, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Create New Category",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String newCategory = nameField.getText().trim();
            if (!newCategory.isEmpty()) {
                if (categoryManager.addCategory(newCategory)) {
                    categoryList.setSelectedValue(newCategory, true);
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Category already exists",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }

    private void editCategory() {
        String selected = categoryList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a category to edit",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel("New name:"), BorderLayout.WEST);
        JTextField nameField = new JTextField(selected, 20);
        panel.add(nameField, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit Category",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            if (!newName.isEmpty() && !newName.equals(selected)) {
                if (categoryManager.editCategory(selected, newName)) {
                    categoryList.setSelectedValue(newName, true);
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Category already exists or edit failed",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }

    private void deleteCategory() {
        String selected = categoryList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a category to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Show warning as per design
        int result = JOptionPane.showConfirmDialog(
                this,
                "This action is not undoable. If a category label is deleted,\n" +
                        "then the tasks/events/goals with this category will have\n" +
                        "their category being empty. Continue?",
                "Delete Category",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            categoryManager.removeCategory(selected);
            if (selected.equals(selectedCategory)) {
                selectedCategory = null;
                categoryField.setText("");
            }
        }
    }

    private void selectCategory() {
        String selected = categoryList.getSelectedValue();
        if (selected != null) {
            selectedCategory = selected;
            categoryField.setText(selected);
            dispose();
        }
    }

    /**
     * Shows the dialog and returns the selected category.
     *
     * @return The selected category, or null if cancelled
     */
    public String showDialog() {
        setVisible(true);
        return selectedCategory;
    }
}