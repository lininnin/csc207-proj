package interface_adapter.alex.WellnessLog_related.moodLabel_related.edit_moodLabel;

public class EditMoodLabelState {

    private String labelId = "";
    private String name = "";
    private String nameError;

    private String type = ""; // e.g., "Positive" or "Negative"
    private String typeError;

    private String editError; // ✅ 编辑失败的整体提示信息

    // ----------- Getters -----------

    public String getLabelId() {
        return labelId;
    }

    public String getName() {
        return name;
    }

    public String getNameError() {
        return nameError;
    }

    public String getType() {
        return type;
    }

    public String getTypeError() {
        return typeError;
    }

    public String getEditError() {
        return editError;
    }

    // ----------- Setters -----------

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameError(String nameError) {
        this.nameError = nameError;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTypeError(String typeError) {
        this.typeError = typeError;
    }

    public void setEditError(String editError) {
        this.editError = editError;
    }
}

