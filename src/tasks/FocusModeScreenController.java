package tasks;

import entries.Entry;
import helper.Database;
import helper.PageSwitchHelper;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import layout.LayoutScreenController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class FocusModeScreenController {

    @FXML
    private ComboBox<String> taskDropdown;

    @FXML
    private Button startFocusingButton;
     
    @FXML
    private Button stopFocusingButton;   
    
    // Focus items
    @FXML
    private Label timeLabel;
    
    @FXML
    private Label chooseTaskLabel;
    
    @FXML
    private Label focusTaskLabel;
    
    @FXML
    private Label selectTaskWarningLabel;

    Database database;

    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();

    LayoutScreenController layoutController = new LayoutScreenController();

    //Navigation
    // Side bar 
    @FXML
    public Button kanbanScreenButton;

    // Top Bar
    @FXML
    public Button focusModeScreenButton;

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

    
    // when the page loads we want the choice box to populate with all the tasks available
    @FXML
    public void initialize() {
        //ensure stop focusing items are off 
        stopFocusingButton.setVisible(false);
        timeLabel.setVisible(false);
        focusTaskLabel.setVisible(false);
        //selectTaskWarningLabel.setVisible(false);
        
        //we want to populate the combo box with name entries from the database
        try {
            ResultSet rs = Database.getResultSet("SELECT * FROM entries");
            while (rs.next()){
                taskDropdown.getItems().addAll(rs.getString("description"));
            }         
            
            // set the first value
            taskDropdown.setValue("Select a task first...");
        } catch (SQLException e){
            e.printStackTrace();
        }

        
//        
//        try {
//            taskDropdown.setItems(list);
//            taskDropdown.setValue("A");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        
        // populate the time 
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
        LocalTime currentTime = LocalTime.now();
        timeLabel.setText("Current Time: " + currentTime.truncatedTo(ChronoUnit.SECONDS));
        }),
             new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
    
    
    @FXML
    public void StartFocusing(){
        if (taskDropdown.getValue().equals("Select a task first...")){
            selectTaskWarningLabel.setVisible(true);
            return;
        }
        
        //set other items inactive
        startFocusingButton.setVisible(false);
        chooseTaskLabel.setVisible(false);
        taskDropdown.setVisible(false);
        selectTaskWarningLabel.setVisible(false);

        //turn on stop focusing items 
        stopFocusingButton.setVisible(true);
        timeLabel.setVisible(true);
        //get whatever task we have & set the label text
        String currentFocusTask = taskDropdown.getValue();
        focusTaskLabel.setText("Time to focus on: " + currentFocusTask);
        focusTaskLabel.setVisible(true);

    }
    
    @FXML
    public void StopFocusing(){
        //turn off stop focusing button
        stopFocusingButton.setVisible(false);
        timeLabel.setVisible(false);
        focusTaskLabel.setVisible(false);

        
        //turn on other items 
        startFocusingButton.setVisible(true);
        chooseTaskLabel.setVisible(true);
        taskDropdown.setVisible(true);
    }
}
