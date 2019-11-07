// THIS FILE ADAPTED FROM INFS2605 19T3 WEEK 5 TUTORIAL

package entries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Entry {

    // StringProperty is needed for TableView
    private StringProperty description;
    private StringProperty starttime;
    private StringProperty endtime;
    private StringProperty duration;
    private StringProperty category;

    public Entry(String category, String description, String starttime, String endtime) {
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
        this.starttime = new SimpleStringProperty(starttime);
        this.endtime = new SimpleStringProperty(endtime);

        String duration = Long.toString((parseTimeInMs(endtime) - parseTimeInMs(starttime)) / 1000);
        this.duration = new SimpleStringProperty(duration);
    }

    public StringProperty getCategoryProperty() {
        return category;
    }

    public StringProperty getDescriptionProperty() {
        return description;
    }

    public StringProperty getStartTimeProperty() {
        return starttime;
    }

    public StringProperty getEndTimeProperty() {
        return endtime;
    }

    public StringProperty getDurationProperty() {
        return duration;
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
