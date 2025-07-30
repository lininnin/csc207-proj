package entity.info;

import java.time.LocalDate;

public interface InfoInterf {
    String getId();
    String getName();
    String getDescription();
    String getCategory();
    LocalDate getCreatedDate();

    void setName(String name);
    void setDescription(String description);
    void setCategory(String category);
}

