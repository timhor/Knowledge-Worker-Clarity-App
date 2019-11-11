package tasks;

import entries.Entry;
import helper.Database;
import helper.PageSwitchHelper;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import layout.LayoutScreenController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class TasksScreenController {

    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();

    LayoutScreenController layoutController = new LayoutScreenController();

    //Navigation
    // Side bar
    @FXML
    public Button kanbanScreenButton;

    @FXML
    public Button focusModeScreenButton;

    // Top Bar
    @FXML
    public Button entriesScreenButton;

    @FXML
    public Button tasksScreenButton;

    @FXML
    public Button aboutScreenButton;

    //inputting task 
    @FXML
    private TextField TaskDescription;

    @FXML
    private TextField TaskTitle;

    @FXML
    private DatePicker DueDate;

    @FXML
    private Button InputTask;

    @FXML
    private DatePicker DoDate;

    @FXML
    private Button SortDoDate;

    @FXML
    private Button SortDueDate;

    @FXML
    private TextField Priority;

    @FXML
    private Label SortBy;

    @FXML
    private Text InputATask;

    @FXML
    private Text priorityText;

    @FXML
    private Label statusLabelTask;




    //Navigation
    // Top Bar Handling
    @FXML
    public void handleEntriesScreenButtonAction(ActionEvent event) throws IOException {
        layoutController.handleEntriesScreenButtonAction(event);
    }

    @FXML
    public void handleTasksScreenButtonAction(ActionEvent event) throws IOException {
        layoutController.handleTasksScreenButtonAction(event);
    }

    @FXML
    public void handleAboutScreenButtonAction(ActionEvent event) throws IOException {
        layoutController.handleAboutScreenButtonAction(event);
    }

    @FXML
    public void handleKanbanScreenButtonAction(ActionEvent event) throws IOException {
        try {
            pageSwitchHelper.switcher(event, "/tasks/TasksScreen.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleFocusModeScreenButtonAction(ActionEvent event) throws IOException {
        try {
            pageSwitchHelper.switcher(event, "/tasks/FocusModeScreen.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
        
    //everything at the bottom moved up - relevant to the task screen 
        //do date 

    //sort by
    @FXML
    void handleSortDueDateButtonAction(ActionEvent event) throws IOException{
        layoutController.handleSortDueDateButtonAction(event);

    }

    @FXML
    void handleSortDoDateButtonAction(ActionEvent event) throws IOException{
        layoutController.handleSortDoDateButtonAction(event);

    }
    //button button
    
    //not sure if correct - its never used
    @FXML
    private void handleInputTaskButtonAction(ActionEvent event) {
        statusLabelTask.setVisible(false);

        String title = TaskTitle.getText();
        
        //if stuff goes wrong
        if (title.length() == 0) {
            statusLabelTask.setVisible(true);
            statusLabelTask.setTextFill(Color.RED);
            statusLabelTask.setText("Title cannot be empty");
            return;
        }

        String description = TaskDescription.getText();
        if (description.length() == 0) {
            statusLabelTask.setVisible(true);
            statusLabelTask.setTextFill(Color.RED);
            statusLabelTask.setText("Description cannot be empty");
            return;
        }
        
                String priority = Priority.getText();
        if (priority.length() == 0) {
            statusLabelTask.setVisible(true);
            statusLabelTask.setTextFill(Color.RED);
            statusLabelTask.setText("Priority cannot be empty");
            return;
        }
        LocalDate doDate = DoDate.getValue();
        
        //what happens when setuff goes wrong
        if (doDate == null) {
            statusLabelTask.setVisible(true);
            statusLabelTask.setTextFill(Color.RED);
            statusLabelTask.setText("Please set a do date");
            return;
        }
        
                LocalDate dueDate = DueDate.getValue();
        if (dueDate == null) {
            statusLabelTask.setVisible(true);
            statusLabelTask.setTextFill(Color.RED);
            statusLabelTask.setText("Please set a due date");
            return;
        }
        try {
            
            //actually inserting the values
            Database.updateFromPreparedStatement(
                    "INSERT INTO tasks (title, description, priority, dueDate, doDate) VALUES (?,?,?,?,?)",
                    new String[]{title, description, priority, dueDate.toString(), doDate.toString()});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            populateEntries();
            //setting everything back to null 
            TaskTitle.setText("");
            TaskDescription.setText("");
            Priority.setText("");
            DueDate.setValue(null);
          DoDate.setValue(null);
        }
    }
}

