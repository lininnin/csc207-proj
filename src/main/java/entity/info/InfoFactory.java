package entity.info;

/**
 * Factory class for creating Info instances using Builder pattern.
 * Matches the existing InfoFactory implementation.
 */
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
        // Handle null or empty name - Info requires non-empty name
        String effectiveName = (name == null || name.trim().isEmpty()) ? "Untitled" : name.trim();
        
        Info.Builder builder = new Info.Builder(effectiveName);
        
        // Add description if provided
        if (description != null) {
            builder.description(description);
        }
        
        // Add category if provided
        if (category != null) {
            builder.category(category);
        }
        
        return builder.build();
    }
}