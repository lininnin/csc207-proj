package interface_adapter.alex.WellnessLog_related.new_wellness_log;

import interface_adapter.ViewModel;

import java.util.Arrays;
import java.util.List;

public class AddWellnessLogViewModel extends ViewModel<AddWellnessLogState> {

    public static final String WELLNESS_LOG_PROPERTY = "addWellnessLogState";

    /**
     * UI label texts used in the form.
     */
    private final List<String> fieldLabels = Arrays.asList(
            "Mood label",
            "Stress",
            "Energy",
            "Fatigue",
            "User Note"
    );

    /**
     * The text shown on the submit button.
     */
    private final String submitButtonLabel = "Add as current wellness log";

    public AddWellnessLogViewModel() {
        super("AddWellnessLogView");
        this.setState(new AddWellnessLogState());
    }

    public List<String> getFieldLabels() {
        return fieldLabels;
    }

    public String getSubmitButtonLabel() {
        return submitButtonLabel;
    }
}


