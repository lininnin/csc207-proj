import java.time.LocalDate;
import java.util.Random;

public class info extends beginAndDueDates {
    String id;
    String name;
    String description;
    String category;
    LocalDate createDate;

    public info(String name, String description, String category) {
        Random random = new Random();
        this.id = String.valueOf(random.nextInt(100000000));
        this.name = name == null || name.isEmpty() ? null : name;
        this.description = description == null || description.isEmpty() ? null : description;
        this.category = category == null || category.isEmpty() ? null : category;
        this.createDate = LocalDate.now(); // or use this.setBeginDate().toLocalDate();
        this.setBeginDate(); // optional if you want to set it in parent class too
    }

    public String getCategory() {
        return this.category;
    }
}