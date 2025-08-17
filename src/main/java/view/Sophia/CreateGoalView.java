package view.Sophia;

import entity.Angela.Task.Task;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;
import interface_adapter.sophia.create_goal.CreatedGoalViewModel;
import interface_adapter.sophia.create_goal.CreateGoalController;
import entity.Sophia.Goal;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

public class CreateGoalView extends JPanel implements ActionListener, PropertyChangeListener {
    public final String viewName = "create goal";
    private final CreatedGoalViewModel createdGoalViewModel;
    private final CreateGoalController createGoalController;

    // Form fields
    private final JTextField goalNameField = new JTextField(15);
    private final JTextField goalDescriptionField = new JTextField(15);
    private final JTextField targetAmountField = new JTextField(15);
    private final JTextField currentAmountField = new JTextField(15);
    private final JTextField startDateField = new JTextField(15);
    private final JTextField endDateField = new JTextField(15);
    private final JComboBox<Goal.TimePeriod> timePeriodCombo = new JComboBox<>(Goal.TimePeriod.values());
    private final JSpinner frequencySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
    private final JButton createButton;

    private final JComboBox<Task> taskComboBox;


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

        JLabel title = new JLabel("Create New Goal");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form panel with updated fields
        JPanel fields = new JPanel(new GridLayout(8, 2, 5, 5));
        fields.add(new JLabel("Goal Name:"));
        fields.add(goalNameField);
        fields.add(new JLabel("Description:"));
        fields.add(goalDescriptionField);
        fields.add(new JLabel("Target Amount:"));
        fields.add(targetAmountField);
        fields.add(new JLabel("Current Amount:"));
        fields.add(currentAmountField);
        fields.add(new JLabel("Start Date (YYYY-MM-DD):"));
        fields.add(startDateField);
        fields.add(new JLabel("End Date (YYYY-MM-DD):"));
        fields.add(endDateField);
        fields.add(new JLabel("Time Period:"));
        fields.add(timePeriodCombo);
        fields.add(new JLabel("Frequency:"));
        fields.add(frequencySpinner);
        fields.add(new JLabel("Target Task:"));
        fields.add(taskComboBox);


        // Button panel
        JPanel buttons = new JPanel();
        createButton = new JButton("Create Goal");
        createButton.addActionListener(this);
        buttons.add(createButton);

        // Main layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(Box.createVerticalStrut(10));
        this.add(fields);
        this.add(Box.createVerticalStrut(10));
        this.add(buttons);

        // Styling
        fields.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Set default current amount to 0
        currentAmountField.setText("0.0");
    }

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
                showErrorDialog("Please enter valid numbers for amounts");
            } catch (Exception e) {
                showErrorDialog("Error creating goal: " + e.getMessage());
            }
        }
    }

    private void clearFormFields() {
        goalNameField.setText("");
        goalDescriptionField.setText("");
        targetAmountField.setText("");
        currentAmountField.setText("0.0");
        startDateField.setText("");
        endDateField.setText("");
        frequencySpinner.setValue(1);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

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