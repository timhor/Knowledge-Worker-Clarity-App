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
    private StringProperty taskName; 
    private StringProperty taskDescription; 
    private StringProperty taskPriority; 
    private StringProperty doDate; 
    private StringProperty dueDate; 
    
    public Task(String taskID, String taskName, String taskDescription, String taskPriority, String doDate, String dueDate) {
        this.taskID = taskID; 
        this.taskName = new SimpleStringProperty(taskName);
        this.taskDescription = new SimpleStringProperty(taskDescription);
        this.taskPriority = new SimpleStringProperty(taskPriority);
        this.doDate = new SimpleStringProperty(doDate);
        this.dueDate = new SimpleStringProperty(dueDate);
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public StringProperty getTaskName() {
        return taskName;
    }

    public void setTaskName(StringProperty taskName) {
        this.taskName = taskName;
    }

    public StringProperty getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(StringProperty taskDescription) {
        this.taskDescription = taskDescription;
    }

    public StringProperty getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(StringProperty taskPriority) {
        this.taskPriority = taskPriority;
    }

    public StringProperty getDoDate() {
        return doDate;
    }

    public void setDoDate(StringProperty doDate) {
        this.doDate = doDate;
    }

    public StringProperty getDueDate() {
        return dueDate;
    }

    public void setDueDate(StringProperty dueDate) {
        this.dueDate = dueDate;
    }
    
    
}
