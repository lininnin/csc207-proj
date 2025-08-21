package view.Sophia;

import entity.Angela.Task.Task;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Sophia.Goal;
import entity.info.Info;
import interface_adapter.sophia.create_goal.CreateGoalController;
import interface_adapter.sophia.create_goal.CreatedGoalViewModel;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

/**
 * A Swing view for creating new goals. This view allows the user to
 * input various details for a new goal, such as name, description,
 * target amounts, and dates, and then submit the information to the
 * controller to create a new goal entity.
 * It implements ActionListener to handle button clicks and PropertyChangeListener
 * to respond to changes in the associated view model.
 */
public class CreateGoalView extends JPanel implements ActionListener, PropertyChangeListener {

    // Constants for magic numbers, grouped by data type
    private static final int TEXT_FIELD_COLUMNS = 15;
    private static final int GRID_ROWS = 8;
    private static final int GRID_COLS = 2;
    private static final int GRID_GAP = 5;
    private static final int VERTICAL_STRUT_SIZE = 10;
    private static final int FORM_PADDING = 10;
    private static final int FRAME_PADDING = 20;
    private static final int FREQUENCY_DEFAULT = 1;
    private static final int FREQUENCY_MIN = 1;
    private static final int FREQUENCY_MAX = 100;

    private static final double DEFAULT_CURRENT_AMOUNT = 0.0;

    private static final String DEFAULT_CURRENT_AMOUNT_TEXT = "0.0";
    private static final String TITLE_TEXT = "Create New Goal";
    private static final String GOAL_NAME_LABEL = "Goal Name:";
    private static final String DESCRIPTION_LABEL = "Description:";
    private static final String TARGET_AMOUNT_LABEL = "Target Amount:";
    private static final String CURRENT_AMOUNT_LABEL = "Current Amount:";
    private static final String START_DATE_LABEL = "Start Date (YYYY-MM-DD):";
    private static final String END_DATE_LABEL = "End Date (YYYY-MM-DD):";
    private static final String TIME_PERIOD_LABEL = "Time Period:";
    private static final String FREQUENCY_LABEL = "Frequency:";
    private static final String TARGET_TASK_LABEL = "Target Task:";
    private static final String CREATE_BUTTON_TEXT = "Create Goal";
    private static final String ERROR_DIALOG_TITLE = "Error";
    private static final String NUMBER_FORMAT_ERROR_MESSAGE = "Please enter valid numbers for amounts";

    // Form fields
    private final JTextField goalNameField = new JTextField(TEXT_FIELD_COLUMNS);
    private final JTextField goalDescriptionField = new JTextField(TEXT_FIELD_COLUMNS);
    private final JTextField targetAmountField = new JTextField(TEXT_FIELD_COLUMNS);
    private final JTextField currentAmountField = new JTextField(TEXT_FIELD_COLUMNS);
    private final JTextField startDateField = new JTextField(TEXT_FIELD_COLUMNS);
    private final JTextField endDateField = new JTextField(TEXT_FIELD_COLUMNS);
    private final JComboBox<Goal.TimePeriod> timePeriodCombo = new JComboBox<>(Goal.TimePeriod.values());
    private final JSpinner frequencySpinner = new JSpinner(new SpinnerNumberModel(FREQUENCY_DEFAULT, FREQUENCY_MIN, FREQUENCY_MAX, FREQUENCY_MIN));
    private final JButton createButton;
    private final JComboBox<Task> taskComboBox;

    public final String viewName = "create goal";
    private final CreatedGoalViewModel createdGoalViewModel;
    private final CreateGoalController createGoalController;


    /**
     * Constructs a {@code CreateGoalView}.
     *
     * @param createdGoalViewModel The view model that holds the state and data related to the creation of a goal.
     * @param createGoalController The controller that handles the logic for creating and saving a new goal.
     */
    public CreateGoalView(CreatedGoalViewModel createdGoalViewModel,
                          CreateGoalController createGoalController) {

        Info studyInfo = new Info.Builder("Study")
                .description("Study algorithms")
                .build();
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), null); // or appropriate dates
        Task studyTask = new Task("templateId1", studyInfo, dates, false);

        Task[] tasks = new Task[] { studyTask };
        taskComboBox = new JComboBox<>(tasks);


        this.createdGoalViewModel = createdGoalViewModel;
        this.createGoalController = createGoalController;
        createdGoalViewModel.addPropertyChangeListener(this);

        JLabel title = new JLabel(TITLE_TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form panel with updated fields
        JPanel fields = new JPanel(new GridLayout(GRID_ROWS, GRID_COLS, GRID_GAP, GRID_GAP));
        fields.add(new JLabel(GOAL_NAME_LABEL));
        fields.add(goalNameField);
        fields.add(new JLabel(DESCRIPTION_LABEL));
        fields.add(goalDescriptionField);
        fields.add(new JLabel(TARGET_AMOUNT_LABEL));
        fields.add(targetAmountField);
        fields.add(new JLabel(CURRENT_AMOUNT_LABEL));
        fields.add(currentAmountField);
        fields.add(new JLabel(START_DATE_LABEL));
        fields.add(startDateField);
        fields.add(new JLabel(END_DATE_LABEL));
        fields.add(endDateField);
        fields.add(new JLabel(TIME_PERIOD_LABEL));
        fields.add(timePeriodCombo);
        fields.add(new JLabel(FREQUENCY_LABEL));
        fields.add(frequencySpinner);
        fields.add(new JLabel(TARGET_TASK_LABEL));
        fields.add(taskComboBox);


        // Button panel
        JPanel buttons = new JPanel();
        createButton = new JButton(CREATE_BUTTON_TEXT);
        createButton.addActionListener(this);
        buttons.add(createButton);

        // Main layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(Box.createVerticalStrut(VERTICAL_STRUT_SIZE));
        this.add(fields);
        this.add(Box.createVerticalStrut(VERTICAL_STRUT_SIZE));
        this.add(buttons);

        // Styling
        fields.setBorder(BorderFactory.createEmptyBorder(FORM_PADDING, FORM_PADDING, FORM_PADDING, FORM_PADDING));
        this.setBorder(BorderFactory.createEmptyBorder(FRAME_PADDING, FRAME_PADDING, FRAME_PADDING, FRAME_PADDING));

        // Set default current amount to 0
        currentAmountField.setText(DEFAULT_CURRENT_AMOUNT_TEXT);
    }

    /**
     * Handles action events from the view, specifically from the create button.
     * It attempts to parse user input, calls the controller to create a new goal,
     * and handles any exceptions that may occur during the process.
     *
     * @param evt The action event that triggered this method call.
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == createButton) {
            try {
                String goalName = goalNameField.getText();
                String goalDescription = goalDescriptionField.getText();
                double targetAmount = Double.parseDouble(targetAmountField.getText());
                double currentAmount = Double.parseDouble(currentAmountField.getText());
                LocalDate startDate = LocalDate.parse(startDateField.getText());
                LocalDate endDate = LocalDate.parse(endDateField.getText());
                Goal.TimePeriod timePeriod = (Goal.TimePeriod) timePeriodCombo.getSelectedItem();
                int frequency = (int) frequencySpinner.getValue();
                Task selectedTask = (Task) taskComboBox.getSelectedItem();


                createGoalController.execute(
                        goalName,
                        goalDescription,
                        targetAmount,
                        currentAmount,
                        startDate,
                        endDate,
                        timePeriod,
                        frequency,
                        selectedTask

                );

                // Clear form after successful submission
                clearFormFields();

            } catch (NumberFormatException e) {
                showErrorDialog(NUMBER_FORMAT_ERROR_MESSAGE);
            } catch (Exception e) {
                showErrorDialog("Error creating goal: " + e.getMessage());
            }
        }
    }

    /**
     * Clears all input fields in the goal creation form.
     */
    private void clearFormFields() {
        goalNameField.setText("");
        goalDescriptionField.setText("");
        targetAmountField.setText("");
        currentAmountField.setText(DEFAULT_CURRENT_AMOUNT_TEXT);
        startDateField.setText("");
        endDateField.setText("");
        frequencySpinner.setValue(FREQUENCY_DEFAULT);
    }

    /**
     * Displays a modal error dialog to the user.
     *
     * @param message The error message to be displayed.
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                ERROR_DIALOG_TITLE,
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Responds to property changes from the view model.
     * When the "state" property changes, it checks for a message to display
     * to the user, typically confirming a successful goal creation.
     *
     * @param evt The property change event.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            String message = createdGoalViewModel.getState().getMessage();
            if (message != null && !message.isEmpty()) {
                JOptionPane.showMessageDialog(this, message);
            }
        }
    }
}
