package entries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Entry {

    private String id;
    private Category category;
    private String description;
    private String date;
    private String startTime;
    private String endTime;
    private long duration;

    public Entry(String entryId, String categoryId, String categoryColour, String categoryName, String description, String date, String startTime, String endTime) {
        this.id = entryId;
        this.category = new Category(categoryId, categoryName, categoryColour);
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = (parseTimeInMs(endTime) - parseTimeInMs(startTime)) / 1000;
    }

    // to facilitate editing and deleting entries from TableView
    public String getId() {
        return this.id;
    }

    public Category getCategory() {
        return this.category;
    }

    public StringProperty getCategoryNameProperty() {
        return new SimpleStringProperty(this.category.getCategoryName());
    }

    public String getDescription() {
        return description;
    }

    public StringProperty getDescriptionProperty() {
        return new SimpleStringProperty(description);
    }

    public String getDate() {
        return date;
    }

    public StringProperty getDateProperty() {
        return new SimpleStringProperty(date);
    }

    public String getStartTime() {
        return startTime;
    }

    public StringProperty getStartTimeProperty() {
        return new SimpleStringProperty(startTime);
    }

    public String getEndTime() {
        return endTime;
    }

    public StringProperty getEndTimeProperty() {
        return new SimpleStringProperty(endTime);
    }

    public StringProperty getDurationProperty() {
        int remaining = (int) duration;
        String hours = String.format("%d", remaining / 3600);
        remaining %= 3600;
        String minutes = String.format("%02d", remaining / 60);
        return new SimpleStringProperty(String.join(":", hours, minutes));
    }

    public static long parseTimeInMs(String time) {
        try {
            Date date = new SimpleDateFormat("HH:mm").parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
