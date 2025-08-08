package use_case.alex.WellnessLog_related.Moodlabel_related.edit_moodLabel;

public class EditMoodLabelInputData {

    private final String originalName; // 用于识别原始标签（等价于ID）
    private final String newName;
    private final String newType; // "Positive" 或 "Negative"

    public EditMoodLabelInputData(String originalName, String newName, String newType) {
        this.originalName = originalName;
        this.newName = newName;
        this.newType = newType;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getNewName() {
        return newName;
    }

    public String getNewType() {
        return newType;
    }
}

