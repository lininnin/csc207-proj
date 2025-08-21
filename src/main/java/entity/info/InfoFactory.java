package entity.info;

/**
 * Factory class for creating Info instances using Builder pattern.
 * Provides methods to create both mutable and immutable Info entities.
 * Uses interface types for dependency inversion compliance.
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
    
    /**
     * Creates an immutable Info instance.
     *
     * @param name Name of the info (required)
     * @param description Optional description
     * @param category Optional category
     * @return ImmutableInfo object
     */
    public ImmutableInfo createImmutable(String name, String description, String category) {
        // Handle null or empty name - Info requires non-empty name
        String effectiveName = (name == null || name.trim().isEmpty()) ? "Untitled" : name.trim();
        
        // First create a regular Info, then convert to immutable
        Info mutableInfo = new Info.Builder(effectiveName)
                .description(description)
                .category(category)
                .build();
                
        return new ImmutableInfo(mutableInfo);
    }
    
    /**
     * Creates an immutable Info instance from existing Info.
     *
     * @param info The Info instance to convert
     * @return ImmutableInfo object
     */
    public ImmutableInfo createImmutable(Info info) {
        if (info == null) {
            throw new IllegalArgumentException("Info cannot be null");
        }
        return new ImmutableInfo(info);
    }
}