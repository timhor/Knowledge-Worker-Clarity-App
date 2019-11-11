package myDay;

import static entries.Entry.parseTimeInMs;
import helper.Database;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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
    public BarChart myDayBarChart;
    
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
    public void initialize() throws SQLException {
 
        // get all the categories available
        //Get all the categories we have in the database
        List categoryNames = new ArrayList();
        try {

            ResultSet rs = Database.getResultSet("SELECT DISTINCT category FROM entries");
            while (rs.next()){
                categoryNames.add(rs.getString("category"));
            }
        } catch (SQLException e){
            e.printStackTrace();            
        }

        //Defining the x axis
        myDayNumberAxis.setLabel("Hours spent (hrs)");
        
        //Defining the y axis
        myDayCategoryAxis.setLabel("Category");

        //We want to find out how much time we've spent on each category
        Map<String,Float> categoryTimeMap = new HashMap<String, Float>();
                
        // get the categories first
        String categorySt = "SELECT DISTINCT category FROM entries WHERE date = '" + LocalDate.now() + "'";
        ResultSet rs = Database.getResultSet(categorySt);
        while (rs.next()){
            System.out.println(rs.getString("category"));
            // sum all the entries in this category
            categoryTimeMap.put(rs.getString("category"), 0.0f);
        }
        System.out.println(categoryTimeMap);
        
        // then go through every single entry, and add to it
        String timeSt = "SELECT category, starttime, endtime FROM entries WHERE date = '" + LocalDate.now() + "'";
        ResultSet timeRs = Database.getResultSet(timeSt);
        while (timeRs.next()){
            // calculate the time it takes 
            String startTime = timeRs.getString("starttime");
            String endTime = timeRs.getString("endtime");
            long duration = parseTimeInMs(endTime) - parseTimeInMs(startTime);
            // convert into hours
            float durationInHours = duration / 3600000.0f;
            // append to the hashmap
            categoryTimeMap.put(timeRs.getString("category"), categoryTimeMap.get(timeRs.getString("category")) + durationInHours);
        }        
        
        System.out.println(categoryTimeMap);
    
        // BAR CHART
        // adapted from https://o7planning.org/en/11107/javafx-barchart-and-stackedbarchart-tutorial 
        System.out.println(categoryTimeMap.keySet());

    }
        



}
