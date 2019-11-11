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
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
        ArrayList<String> categoryNames = new ArrayList<>();
        try {

            ResultSet rs = Database.getResultSet("SELECT DISTINCT categoryname FROM categories");
            while (rs.next()){
                categoryNames.add(rs.getString("categoryname"));
            }
        } catch (SQLException e){
            e.printStackTrace();            
        }

        //Defining the x axis
        myDayNumberAxis.setLabel("Hours spent (hrs)");
        
        //Defining the y axis
        //myDayCategoryAxis.setCategories(FXCollections.<String>observableArrayList(categoryNames));
        myDayCategoryAxis.setLabel("Category");

        //We want to find out how much time we've spent on each category
        HashMap<String,Float> categoryTimeMap = new HashMap<String, Float>();
                
        // get the categories first
        String categorySt = "SELECT DISTINCT c.categoryname FROM entries e LEFT JOIN categories c ON e.category = c.id WHERE e.date = '" + LocalDate.now() + "'";
        ResultSet rs = Database.getResultSet(categorySt);
        while (rs.next()){
            // sum all the entries in this category
            categoryTimeMap.put(rs.getString("categoryname"), 0.0f);
        }
        
        // then go through every single entry, and add to it
        String timeSt = "SELECT c.categoryname, e.starttime, e.endtime FROM entries e LEFT JOIN categories c ON e.category = c.id WHERE e.date = '" + LocalDate.now() + "'";
        ResultSet timeRs = Database.getResultSet(timeSt);
        while (timeRs.next()){
            // calculate the time it takes 
            String startTime = timeRs.getString("starttime");
            String endTime = timeRs.getString("endtime");
            long duration = parseTimeInMs(endTime) - parseTimeInMs(startTime);
            // convert into hours
            float durationInHours = duration / 3600000.0f;
            // append to the hashmap
            categoryTimeMap.put(timeRs.getString("categoryname"), categoryTimeMap.get(timeRs.getString("categoryname")) + durationInHours);
        }        
        
        //System.out.println(categoryTimeMap);
        
        HashMap<String,Float> sortedDailyHashMap = nLargest(categoryTimeMap, 5);
    
        // BAR CHART
        // adapted from https://o7planning.org/en/11107/javafx-barchart-and-stackedbarchart-tutorial 
        XYChart.Series<String, Float> series = new XYChart.Series<String, Float>();
        for (Map.Entry<String, Float> entry : sortedDailyHashMap.entrySet()) {
            String key = entry.getKey();
            Float value = entry.getValue();
            series.getData().add(new XYChart.Data(key, value));
        }
        myDayBarChart.getData().add(series);
        myDayBarChart.setLegendVisible(false);
        myDayBarChart.setTitle("Time spent on today's top 5 categories");
    }
    
    // Method for finding the top n values in a hashmap
    // Adapted from 
    static HashMap<String, Float> nLargest(HashMap<String, Float> map, int n) { //map and n largest values to search for
        Float value;
        ArrayList<String> keys = new ArrayList<>(n); //to store keys of the n largest values
        ArrayList<Float> values = new ArrayList<>(n); //to store n largest values (same index as keys)
        int index;
        for (String key : map.keySet()) { //iterate on all the keys (i.e. on all the values)
            value = map.get(key); //get the corresponding value
            index = keys.size() - 1; //initialize to search the right place to insert (in a sorted order) current value within the n largest values
            while (index >= 0 && value > values.get(index)) { //we traverse the array of largest values from smallest to biggest
                index--; //until we found the right place to insert the current value
            }
            index = index + 1; //adapt the index (come back by one)
            values.add(index, value); //insert the current value in the right place
            keys.add(index, key); //and also the corresponding key
            if (values.size() > n) { //if we have already found enough number of largest values
                values.remove(n); //we remove the last largest value (i.e. the smallest within the largest)
                keys.remove(n); //actually we store at most n+1 largest values and therefore we can discard just the last one (smallest)
            }
        }
        HashMap<String, Float> result = new HashMap<>(values.size());
        for (int i = 0; i < values.size(); i++) { //copy keys and value into an HashMap
            result.put(keys.get(i), values.get(i));
        }
        return result;
    }
    
}
