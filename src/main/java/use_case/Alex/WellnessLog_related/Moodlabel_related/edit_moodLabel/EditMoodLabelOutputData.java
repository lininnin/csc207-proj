package use_case.Alex.WellnessLog_related.Moodlabel_related.edit_moodLabel;

public class EditMoodLabelOutputData {

    private final String name;
    private final String type; // "Positive" 或 "Negative"
    private final boolean useCaseFailed;

    public EditMoodLabelOutputData(String name, String type, boolean useCaseFailed) {
        this.name = name;
        this.type = type;
        this.useCaseFailed = useCaseFailed;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}

