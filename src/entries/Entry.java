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
    private StringProperty category;
    private StringProperty description;
    private StringProperty starttime;
    private StringProperty endtime;
    private long duration;

    public Entry(String id, String category, String description, String starttime, String endtime) {
        this.id = id;
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
        this.starttime = new SimpleStringProperty(starttime);
        this.endtime = new SimpleStringProperty(endtime);
        this.duration = (parseTimeInMs(endtime) - parseTimeInMs(starttime)) / 1000;
    }

    // to facilitate editing and deleting entries from TableView
    public String getId() {
        return this.id;
    }

    public StringProperty getCategoryProperty() {
        return category;
    }

    public StringProperty getDescriptionProperty() {
        return description;
    }

    public void setDescriptionProperty(String description) {
        this.description.set(description);
    }

    public StringProperty getStartTimeProperty() {
        return starttime;
    }

    public StringProperty getEndTimeProperty() {
        return endtime;
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
