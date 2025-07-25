package use_case.Alex.Event_related.avaliable_events_module.edit_event;

public class EditEventOutputData {
    private final String id;
    private final String name;
    private final String category;
    private final String description;
    private final boolean useCaseFailed;

    public EditEventOutputData(String id, String name, String category, String description, boolean useCaseFailed) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.useCaseFailed = useCaseFailed;
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
}
