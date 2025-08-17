package use_case.alex.event_related.available_events_module.edit_event;

public class EditEventInputData {
    private final String id;
    private final String name;
    private final String category;
    private final String description;

    public EditEventInputData(String id, String name, String category, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
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
}

