// THIS FILE ADAPTED FROM INFS2605 19T3 WEEK 5 TUTORIAL

package entries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Entry {

    // StringProperty is needed for TableView
    private String id;
    private String categoryColour;
    private StringProperty categoryName;
    private StringProperty description;
    private StringProperty date;
    private StringProperty startTime;
    private StringProperty endTime;
    private long duration;

    public Entry(String id, String categoryColour, String categoryName, String description, String date, String startTime, String endTime) {
        this.id = id;
        this.categoryColour = categoryColour;
        this.categoryName = new SimpleStringProperty(categoryName);
        this.description = new SimpleStringProperty(description);
        this.date = new SimpleStringProperty(date);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.duration = (parseTimeInMs(endTime) - parseTimeInMs(startTime)) / 1000;
    }

    // to facilitate editing and deleting entries from TableView
    public String getId() {
        return this.id;
    }

    public String getCategoryColour() {
        return this.categoryColour;
    }

    public StringProperty getCategoryNameProperty() {
        return categoryName;
    }

    public StringProperty getDescriptionProperty() {
        return description;
    }

    public void setDescriptionProperty(String description) {
        this.description.set(description);
    }

    public StringProperty getDateProperty() {
        return date;
    }

    public StringProperty getStartTimeProperty() {
        return startTime;
    }

    public void setStartTimeProperty(String startTime) {
        this.startTime.set(startTime);
    }

    public StringProperty getEndTimeProperty() {
        return endTime;
    }

    public void setEndTimeProperty(String endTime) {
        this.endTime.set(endTime);
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
