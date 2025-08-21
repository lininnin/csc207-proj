package entity.info;

import java.time.LocalDate;

public interface InfoInterf {
    String getId();
    String getName();
    String getDescription();
    String getCategory();
    LocalDate getCreatedDate();

    /**
     * @deprecated Use immutable update methods instead
     */
    @Deprecated
    void setName(String name);
    
    /**
     * @deprecated Use immutable update methods instead
     */
    @Deprecated
    void setDescription(String description);
    
    /**
     * @deprecated Use immutable update methods instead
     */
    @Deprecated
    void setCategory(String category);
}

