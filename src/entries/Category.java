package entries;

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

    public String getHexString() {
        return this.hexString;
    }

}
