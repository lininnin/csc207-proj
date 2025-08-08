package use_case.Alex.Event_related.avaliable_events_module.edit_event;

public class EditEventOutputData {
    private final String id;
    private final String name;
    private final String category;
    private final String description;
    private final boolean useCaseFailed;
    private final String errorMessage;  // ✅ 新增字段

    // ✅ 新构造函数，包含错误信息
    public EditEventOutputData(String id, String name, String category, String description, String errorMessage, boolean useCaseFailed) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.errorMessage = errorMessage;
        this.useCaseFailed = useCaseFailed;
    }

    // ✅ 若不需要 errorMessage，也提供原来的构造函数，默认 errorMessage 为 null
    public EditEventOutputData(String id, String name, String category, String description, boolean useCaseFailed) {
        this(id, name, category, description, null, useCaseFailed);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }

    // ✅ getter for errorMessage
    public String getErrorMessage() {
        return errorMessage;
    }
}
