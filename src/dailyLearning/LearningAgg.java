// THIS FILE ADAPTED FROM INFS2605 19T3 WEEK 5 TUTORIAL
package dailyLearning;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LearningAgg {

    // StringProperty is needed for TableView
    private StringProperty desc;
    private IntegerProperty count;

    public LearningAgg(String desc, Integer count) {

        this.desc = new SimpleStringProperty(desc);
        this.count = new SimpleIntegerProperty(count);
    }

    // to facilitate editing and deleting entries from TableView
    public StringProperty getDescProperty() {
        return desc;
    }

    public void setDescProperty(String desc) {
        this.desc.set(desc);
    }

    public IntegerProperty getCountProperty() {
        return count;
    }

    public void setCountProperty(Integer count) {
        this.count.set(count);
    }

}
