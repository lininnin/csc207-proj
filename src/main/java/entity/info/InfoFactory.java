package entity.info;

public class InfoFactory implements InfoFactoryInterf {

    /**
     * Creates an Info instance using builder pattern.
     *
     * @param name Name of the info (required)
     * @param description Optional description
     * @param category Optional category
     * @return InfoInterf object
     */
    @Override
    public InfoInterf create(String name, String description, String category) {
        Info.Builder builder = new Info.Builder(name);
        if (description != null) {
            builder.description(description);
        }
        if (category != null) {
            builder.category(category);
        }
        return builder.build();
    }
}

