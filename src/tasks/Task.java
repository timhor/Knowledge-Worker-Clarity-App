/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tasks;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author jenniferruan
 */
public class Task {
    private String taskID;
    private StringProperty title;
    private StringProperty description;
    private StringProperty priority;
    private StringProperty dueDate;
    private StringProperty doDate;

    public Task(String taskID, String title, String description, String priority, String dueDate, String doDate) {
        this.taskID = taskID;
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.priority = new SimpleStringProperty(priority);
        this.dueDate = new SimpleStringProperty(dueDate);
        this.doDate = new SimpleStringProperty(doDate);
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public StringProperty getTitleProperty() {
        return title;
    }

    public void setTitleProperty(StringProperty title) {
        this.title = title;
    }

    public StringProperty getDescriptionProperty() {
        return description;
    }

    public void setDescriptionProperty(StringProperty description) {
        this.description = description;
    }

    public StringProperty getPriorityProperty() {
        return priority;
    }

    public void setPriorityProperty(StringProperty priority) {
        this.priority = priority;
    }

    public StringProperty getDoDateProperty() {
        return doDate;
    }

    public void setDoDateProperty(StringProperty doDate) {
        this.doDate = doDate;
    }

    public StringProperty getDueDateProperty() {
        return dueDate;
    }

    public void setDueDateProperty(StringProperty dueDate) {
        this.dueDate = dueDate;
    }

}
