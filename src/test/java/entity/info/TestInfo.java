package entity.info;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Test implementation of InfoInterf that allows empty strings and preserves spaces.
 * Used only for testing InfoFactory behavior with edge cases.
 */
class TestInfo implements InfoInterf {
    private final String id;
    private String name;
    private String description;
    private String category;
    private final LocalDate createdDate;

    public TestInfo(String name, String description, String category) {
        this.id = UUID.randomUUID().toString();
        this.name = name; // No validation - allow empty and spaces
        this.description = description;
        this.category = category;
        this.createdDate = LocalDate.now();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public LocalDate getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setName(String name) {
        this.name = name; // No validation
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }
}