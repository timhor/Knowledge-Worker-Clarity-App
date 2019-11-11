package myDay;

import helper.Database;
import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import layout.LayoutScreenController;

public class MyDayScreenController {

    LayoutScreenController layoutController = new LayoutScreenController();

    // Side bar 
    @FXML
    public Button homeScreenButton;

    @FXML
    public Button myLifeScreenButton;

    @FXML
    public Button myDayScreenButton;

    @FXML
    public Button myWeekScreenButton;
    
    @FXML
    public Button weeklyTrendsScreenButton;

    // Top Bar
    @FXML
    public Button entriesScreenButton;

    @FXML
    public Button tasksScreenButton;
    
    @FXML
    public Button aboutScreenButton;
    
   
    // Chart items
    @FXML
    public BarChart myDayLineChart;
    
    @FXML
    public CategoryAxis myDayCategoryAxis;
    
    @FXML
    public NumberAxis myDayNumberAxis;

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

    // Add Data Handling  
    @FXML
    public void handleHomeScreenButtonAction(ActionEvent event) throws IOException {
        layoutController.handleHomeScreenButtonAction(event);
    }
    
    @FXML
    public void handleMyLifeScreenButtonAction(ActionEvent event) throws IOException {
        layoutController.handleMyLifeScreenButtonAction(event);
    }

    @FXML
    public void handleMyDayScreenButtonAction(ActionEvent event) throws IOException {
        layoutController.handleMyDayScreenButtonAction(event);
    }

    @FXML
    public void handleMyWeekScreenButtonAction(ActionEvent event) throws IOException {
        layoutController.handleMyWeekScreenButtonAction(event);
    }

    @FXML
    public void handleWeeklyTrendsScreenButtonAction(ActionEvent event) throws IOException {
        layoutController.handleWeeklyTrendsScreenButtonAction(event);
    }
    
    
      
    @FXML
    public void initialize() {
        // add in some entries first 
        try {
                Database.updateFromPreparedStatement(
                        "INSERT INTO entries (category, description, date, starttime, endtime) VALUES (?,?,?,?,?)",
                        new String[]{"Social", "Shopping at Westfield", "2019-11-11", "13:00", "16:00"});
                Database.updateFromPreparedStatement(
                        "INSERT INTO entries (category, description, date, starttime, endtime) VALUES (?,?,?,?,?)",
                        new String[]{"Study", "Doing my java assignment", "2019-11-11", "16:00", "21:00"});
                Database.updateFromPreparedStatement(
                        "INSERT INTO entries (category, description, date, starttime, endtime) VALUES (?,?,?,?,?)",
                        new String[]{"Work", "Shift in the morning", "2019-11-11", "09:00", "12:00"});
                Database.updateFromPreparedStatement(
                        "INSERT INTO entries (category, description, date, starttime, endtime) VALUES (?,?,?,?,?)",
                        new String[]{"Exercise", "Went for a jog", "2019-11-11", "12:00", "13:00"});
                Database.updateFromPreparedStatement(
                        "INSERT INTO entries (category, description, date, starttime, endtime) VALUES (?,?,?,?,?)",
                        new String[]{"Relax", "Meditated", "2019-11-11", "22:00", "23:15"});
                Database.updateFromPreparedStatement(
                        "INSERT INTO entries (category, description, date, starttime, endtime) VALUES (?,?,?,?,?)",
                        new String[]{"Travel", "going to work", "2019-11-11", "07:30", "09:00"});
                
            } catch (SQLException e) {
                e.printStackTrace();
        }
        
        // BAR CHART
        // adapted from https://o7planning.org/en/11107/javafx-barchart-and-stackedbarchart-tutorial 
        
        
    }
        



}
