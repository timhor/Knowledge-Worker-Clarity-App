package weeklyTrends;

import static entries.Entry.parseTimeInMs;
import helper.Database;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.control.Button;
import layout.LayoutScreenController;
import org.joda.time.*;


import javafx.scene.chart.LineChart; 
import javafx.scene.chart.NumberAxis; 
import javafx.scene.chart.XYChart; 

public class WeeklyTrendsScreenController {

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
    public LineChart weeklyTrendsLineChart;
    
    @FXML
    public CategoryAxis weeklyTrendsCategoryAxis;
    
    @FXML
    public NumberAxis weeklyTrendsNumberAxis;
    

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

        // ** LINE GRAPH ** 
        // Adapted from https://www.tutorialspoint.com/javafx/line_chart.htm 

        //Defining the y axis
        weeklyTrendsNumberAxis.setLabel("Time spent on each category (min)"); 
        
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
        
        //Defining the x axis (categories)  
        // We want the last 12 weeks of data - adapted from https://stackoverflow.com/questions/31467524/how-to-get-all-week-dates-for-given-date-java       
        //Find start of each week from that start Date, which will become our x axis
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(2);
        LocalDate thisMonday = startDate.withDayOfWeek(DateTimeConstants.MONDAY);
        if (startDate.isAfter(thisMonday)) {
            startDate = thisMonday.plusWeeks(1); // start on next monday
        } else {
            startDate = thisMonday; // start on this monday
        }
        ArrayList<String> weeksAxis = new ArrayList<>();
        while (startDate.isBefore(endDate)) {
            weeksAxis.add(startDate.toString());
            startDate = startDate.plusWeeks(1);
        }  
        weeklyTrendsCategoryAxis.setCategories(FXCollections.<String>observableArrayList(weeksAxis));
        weeklyTrendsCategoryAxis.setLabel("Week of");        
        
        // Data entry 
        // Data to add to before we put them into the plot 
        HashMap<String, ArrayList<Float>> dataHashmap = new HashMap<>();
        
        for (int j = 0; j < categoryNames.size(); j++){
            // create an array list, which will store all the hours 
            ArrayList<Float> categoryArrayList = new ArrayList<>();
            for (int i = 0; i < weeksAxis.size(); i++){
                long categorySumInMs = 0;
                try {
                    String st = "";
                    if (i < weeksAxis.size() - 1)
                    {
                        st = "SELECT starttime, endtime FROM entries WHERE date BETWEEN '" + weeksAxis.get(i) + "' AND '" + weeksAxis.get(i + 1) + "' AND category = '" + categoryNames.get(j) + "'";
                    } else if (i == weeksAxis.size() - 1) {
                        st = "SELECT starttime, endtime FROM entries WHERE date BETWEEN '" + weeksAxis.get(i) + "' AND '" + LocalDate.now() + "' AND category = '" + categoryNames.get(j) + "'";
                    }
                    ResultSet rs = Database.getResultSet(st);
                    while (rs.next()){
                        // sum all the entries in this category
                        String startTime = rs.getString("starttime");
                        String endTime = rs.getString("endtime");
                        long duration = parseTimeInMs(endTime) - parseTimeInMs(startTime);
                        categorySumInMs += duration;
                    }
                } catch (SQLException e){
                    e.printStackTrace();
                }         
                // convert into minutes 
                float timeSpent = 0;
                timeSpent = TimeUnit.MILLISECONDS.toMinutes(categorySumInMs);
                categoryArrayList.add(timeSpent);
            }           
            // add it to the hashmap
            String category = (String) categoryNames.get(j);
            dataHashmap.put(category, categoryArrayList);
        }
        
        // now create series for each category and add to them
        dataHashmap.keySet().stream().forEach((key) -> {
            //iterate over keys
            XYChart.Series series = new XYChart.Series(); 
            series.setName("Category: " + key);
            // iterate over the array list, and add the date to it from weeksAxis
            for (int i = 0; i < weeksAxis.size(); i++){
                series.getData().add(new XYChart.Data( weeksAxis.get(i), dataHashmap.get(key).get(i)));
            }
            weeklyTrendsLineChart.getData().add(series);
        });       
    }
}
