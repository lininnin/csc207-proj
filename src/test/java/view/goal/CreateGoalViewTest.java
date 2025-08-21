package view.goal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.contains;

import entity.Angela.Task.Task;
import entity.Sophia.Goal;
import view.Sophia.CreateGoalView;
import interface_adapter.sophia.create_goal.CreateGoalController;
import interface_adapter.sophia.create_goal.CreatedGoalViewModel;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class CreateGoalViewTest {

    @Mock
    private CreatedGoalViewModel createdGoalViewModel;

    @Mock
    private CreateGoalController createGoalController;

    // ---- Utilities to run on EDT and to walk component tree ----
    private static void onEDT(Runnable r) {
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                r.run();
            } else {
                SwingUtilities.invokeAndWait(r);
            }
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Component> T find(Component root, Class<T> type) {
        if (type.isInstance(root)) return (T) root;
        if (root instanceof Container c) {
            for (Component child : c.getComponents()) {
                T got = find(child, type);
                if (got != null) return got;
            }
        }
        return null;
    }

    private static JButton findButtonByText(Component root, String text) {
        JButton btn = find(root, JButton.class);
        if (btn != null && text.equals(btn.getText())) return btn;
        if (root instanceof Container c) {
            for (Component child : c.getComponents()) {
                JButton got = findButtonByText(child, text);
                if (got != null) return got;
            }
        }
        return null;
    }

    @BeforeAll
    static void headless() {
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    void constructor_addsPropertyChangeListener() {
        onEDT(() -> {
            new CreateGoalView(createdGoalViewModel, createGoalController);
            // Verify the view registered itself
            verify(createdGoalViewModel, atLeastOnce()).addPropertyChangeListener(any());
        });
    }

    @Test
    void successfulCreate_callsController_and_clearsForm() {
        onEDT(() -> {
            // Arrange
            CreateGoalView view = new CreateGoalView(createdGoalViewModel, createGoalController);

            // Locate fields/components
            JTextField nameField        = getNthTextField(view, 0);
            JTextField descField        = getNthTextField(view, 1);
            JTextField startDateField   = getNthTextField(view, 2);
            JTextField endDateField     = getNthTextField(view, 3);
            @SuppressWarnings("unchecked")
            JComboBox<Goal.TimePeriod> periodCombo = find(view, JComboBox.class);
            JSpinner freqSpinner       = find(view, JSpinner.class);
            @SuppressWarnings("unchecked")
            JComboBox<Task> taskCombo  = (JComboBox<Task>) getSecondComboInTree(view);

            assertNotNull(nameField);
            assertNotNull(descField);
            assertNotNull(startDateField);
            assertNotNull(endDateField);
            assertNotNull(periodCombo);
            assertNotNull(freqSpinner);
            assertNotNull(taskCombo);

            // Fill inputs (valid)
            nameField.setText("Read");
            descField.setText("Read algorithms");
            startDateField.setText("2025-08-20");
            endDateField.setText("2025-08-30");

            // Select first enum / set frequency
            if (periodCombo.getItemCount() > 0) {
                periodCombo.setSelectedIndex(0);
            }
            freqSpinner.setValue(3);

            // Select the default task provided by the view
            if (taskCombo.getItemCount() > 0) {
                taskCombo.setSelectedIndex(0);
            }

            JButton createBtn = findButtonByText(view, "Create Goal");
            assertNotNull(createBtn);

            // Act
            createBtn.doClick(); // fires ActionEvent

            // Assert controller call
            ArgumentCaptor<String> goalNameCap = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<String> descCap = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<LocalDate> startCap = ArgumentCaptor.forClass(LocalDate.class);
            ArgumentCaptor<LocalDate> endCap = ArgumentCaptor.forClass(LocalDate.class);
            ArgumentCaptor<Goal.TimePeriod> periodCap = ArgumentCaptor.forClass(Goal.TimePeriod.class);
            ArgumentCaptor<Integer> freqCap = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<Task> taskCap = ArgumentCaptor.forClass(Task.class);

            verify(createGoalController, times(1)).execute(
                    goalNameCap.capture(),
                    descCap.capture(),
                    startCap.capture(),
                    endCap.capture(),
                    periodCap.capture(),
                    freqCap.capture(),
                    taskCap.capture()
            );

            assertEquals("Read", goalNameCap.getValue());
            assertEquals("Read algorithms", descCap.getValue());
            assertEquals(LocalDate.parse("2025-08-20"), startCap.getValue());
            assertEquals(LocalDate.parse("2025-08-30"), endCap.getValue());
            assertNotNull(periodCap.getValue());
            assertEquals(3, freqCap.getValue());
            assertNotNull(taskCap.getValue());

            // Fields cleared
            assertEquals("", nameField.getText());
            assertEquals("", descField.getText());
            assertEquals("", startDateField.getText());
            assertEquals("", endDateField.getText());
            assertEquals(1, freqSpinner.getValue()); // reset to FREQUENCY_DEFAULT
        });
    }

    @Test
    void invalidNumbers_showErrorDialog() {
        onEDT(() -> {
            try (MockedStatic<JOptionPane> jp = Mockito.mockStatic(JOptionPane.class)) {
                CreateGoalView view = new CreateGoalView(createdGoalViewModel, createGoalController);

                JTextField startDate     = getNthTextField(view, 2);
                JTextField endDate       = getNthTextField(view, 3);

                // Minimal valid fields + intentionally bad date format
                getNthTextField(view, 0).setText("X");
                getNthTextField(view, 1).setText("Y");
                startDate.setText("bad-date");      // will cause DateTimeParseException
                endDate.setText("2025-08-30");

                JButton createBtn = findButtonByText(view, "Create Goal");
                assertNotNull(createBtn);

                createBtn.doClick();

                // Expect an error dialog with the specific message
                jp.verify(() -> JOptionPane.showMessageDialog(
                        any(),
                        contains("Text 'bad-date' could not be parsed"),
                        eq("Error"),
                        eq(JOptionPane.ERROR_MESSAGE)
                ), times(1));

                // Controller should NOT be called
                verifyNoInteractions(createGoalController);
            }
        });
    }

    @Test
    void propertyChange_onState_showsMessageDialog() {
        onEDT(() -> {
            try (MockedStatic<JOptionPane> jp = Mockito.mockStatic(JOptionPane.class)) {
                CreateGoalView view = new CreateGoalView(createdGoalViewModel, createGoalController);

                // Mock state object with message
                var state = mock(interface_adapter.sophia.create_goal.CreatedGoalState.class, withSettings().lenient());
                when(state.getMessage()).thenReturn("Created!");

                when(createdGoalViewModel.getState()).thenReturn(state);

                // Fire property change with name "state"
                view.propertyChange(new java.beans.PropertyChangeEvent(this, "state", null, null));

                jp.verify(() -> JOptionPane.showMessageDialog(
                        any(Component.class),
                        eq("Created!")
                ), times(1));
            }
        });
    }

    // ---- helpers to find specific fields in the order they were added ----

    /** The view adds four JTextFields in order:
     * 0: goalName, 1: description, 2: startDate, 3: endDate */
    private static JTextField getNthTextField(Container root, int n) {
        java.util.List<JTextField> list = new java.util.ArrayList<>();
        collectTextFields(root, list);
        if (n < 0 || n >= list.size())
            throw new AssertionError("Requested text field index " + n + " but found only " + list.size());
        return list.get(n);
    }

    private static void collectTextFields(Component c, java.util.List<JTextField> out) {
        if (c instanceof JTextField tf) out.add(tf);
        if (c instanceof Container ct) {
            for (Component child : ct.getComponents()) {
                collectTextFields(child, out);
            }
        }
    }

    /** The view has two JComboBoxes: [Goal.TimePeriod], then [Task]. */
    private static Component getSecondComboInTree(Container root) {
        java.util.List<JComboBox<?>> list = new java.util.ArrayList<>();
        collectCombos(root, list);
        if (list.size() < 2) throw new AssertionError("Expected at least 2 JComboBox components, found " + list.size());
        return list.get(1);
    }

    private static void collectCombos(Component c, java.util.List<JComboBox<?>> out) {
        if (c instanceof JComboBox<?> cb) out.add(cb);
        if (c instanceof Container ct) {
            for (Component child : ct.getComponents()) {
                collectCombos(child, out);
            }
        }
    }

    // Sanity check: clicking the button only triggers when source matches the same instance.
    @Test
    void clickingForeignButton_doesNothing() {
        onEDT(() -> {
            CreateGoalView view = new CreateGoalView(createdGoalViewModel, createGoalController);

            // Fire actionPerformed with a different JButton instance as source
            JButton foreign = new JButton("Create Goal");
            view.actionPerformed(new ActionEvent(foreign, ActionEvent.ACTION_PERFORMED, "cmd"));

            verifyNoInteractions(createGoalController);
        });
    }
}
