// THIS FILE ADAPTED FROM INFS2605 19T3 WEEK 5 TUTORIAL
package dailyLearning;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Learning {

    // StringProperty is needed for TableView
    private String id;
    private StringProperty date;
    private StringProperty wentWell;
    private StringProperty couldImprove;

    public Learning(String id, String date, String wentWell, String couldImprove) {
        this.id = id;
        this.date = new SimpleStringProperty(date);
        this.wentWell = new SimpleStringProperty(wentWell);
        this.couldImprove = new SimpleStringProperty(couldImprove);
    }

    // to facilitate editing and deleting entries from TableView
    public String getId() {
        return this.id;
    }

    public StringProperty getWentWellProperty() {
        return wentWell;
    }

    public StringProperty getCouldImproveProperty() {
        return couldImprove;
    }

    public void setWentWellProperty(String wentWell) {
        this.wentWell.set(wentWell);
    }

    public void setCouldImproveProperty(String couldImprove) {
        this.couldImprove.set(couldImprove);
    }

    public StringProperty getDateProperty() {
        return date;
    }
}
