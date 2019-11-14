/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tasks;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Task {
    private String taskID;
    private String title;
    private String description;
    private String priority;
    private String dueDate;
    private String doDate;

    public Task(String taskID, String title, String description, String priority, String dueDate, String doDate) {
        this.taskID = taskID;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.doDate = doDate;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getTitle() {
        return title;
    }

    public StringProperty getTitleProperty() {
        return new SimpleStringProperty(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public StringProperty getDescriptionProperty() {
        return new SimpleStringProperty(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public StringProperty getPriorityProperty() {
        return new SimpleStringProperty(priority);
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDoDate() {
        return doDate;
    }

    public StringProperty getDoDateProperty() {
        return new SimpleStringProperty(doDate);
    }

    public void setDoDate(String doDate) {
        this.doDate = doDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public StringProperty getDueDateProperty() {
        return new SimpleStringProperty(dueDate);
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String toString() {
        return this.title + " [Priority " + this.priority + "]";
    }

}
