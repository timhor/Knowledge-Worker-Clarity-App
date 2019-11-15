package entries;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Category {

    private String id;
    private String categoryName;
    private String hexString;

    public Category(String id, String categoryName, String hexString) {
        this.id = id;
        this.categoryName = categoryName;
        this.hexString = hexString;
    }

    public String getId() {
        return this.id;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public StringProperty getCategoryNameProperty() {
        return new SimpleStringProperty(this.categoryName);
    }

    public String getHexString() {
        return this.hexString.trim();
    }

    public StringProperty getHexStringProperty() {
        return new SimpleStringProperty(this.hexString);
    }

}
