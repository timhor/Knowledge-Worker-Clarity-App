package myLife;

import static entries.Entry.parseTimeInMs;
import helper.Database;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import layout.LayoutScreenController;

public class MyLifeScreenController {

    @FXML
    public PieChart myPieChart;
    
    LayoutScreenController layoutController = new LayoutScreenController();

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
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        layoutController.handleWeeklyTrendsScreenButtonAction(event);
    }
   
    @FXML
    public void initialize() {

        try {
            HashMap<String, Float> data = new HashMap<String, Float>();
            String categorySt = "SELECT DISTINCT c.categoryname FROM entries e LEFT JOIN categories c ON e.category = c.id";
            ResultSet rs = Database.getResultSet(categorySt);
            while (rs.next()) {
                data.put(rs.getString("categoryname"), 0.0f);
            }
            String timeSt = "SELECT c.categoryname, e.starttime, e.endtime FROM entries e LEFT JOIN categories c ON e.category = c.id";
            ResultSet timeRs = Database.getResultSet(timeSt);
            long totalTimeSpent = 0;
            while (timeRs.next()) {
                // calculate the time it takes 
                String startTime = timeRs.getString("starttime");
                String endTime = timeRs.getString("endtime");
                long duration = parseTimeInMs(endTime) - parseTimeInMs(startTime);                
                float durationInHours = duration / 3600000.0f;  
                totalTimeSpent += durationInHours;
                
                // append to the hashmap
                data.put(timeRs.getString("categoryname"), data.get(timeRs.getString("categoryname")) + durationInHours); 
            }
            for (Map.Entry<String, Float> entry : data.entrySet()) {
                String key = entry.getKey();
                Float value = entry.getValue();
                // jeff: % of time spent = value / totalTimeSpent * 100. so try and add the label here
                myPieChart.getData().add(new PieChart.Data(key, value / totalTimeSpent));
            }
            // here you'll probably add the color code work (i think). make sure you do it in a try/catch.
            // you'll need to do a sql query to get the colours from the categories table 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
