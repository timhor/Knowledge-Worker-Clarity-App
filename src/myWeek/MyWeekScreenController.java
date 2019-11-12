package myWeek;

import static entries.Entry.parseTimeInMs;
import helper.Database;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import layout.LayoutScreenController;
//import org.joda.time.DateTime;

public class MyWeekScreenController {/**

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
    public BarChart myWeekBarChart;
    
    @FXML
    public CategoryAxis myWeekCategoryAxis;
    
    @FXML
    public NumberAxis myWeekNumberAxis;
    
    @FXML
    public Label dateRangeLabel;

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
        myWeekNumberAxis.setLabel("Hours spent (hrs)");
        
        //Defining the y axis
        myWeekCategoryAxis.setLabel("Category");

        //We want to find out how much time we've spent on each category
        HashMap<String,Float> categoryTimeMap = new HashMap<String, Float>();
                
        // get the dates for the last seven days
        org.joda.time.LocalDate weekEarlier = new DateTime().minusDays(7).toLocalDate();
        
        String dateRange = "Date Range: " + weekEarlier + " to " + LocalDate.now();
        dateRangeLabel.setText(dateRange);

        // get the categories first from the last week
        String categorySt = "SELECT DISTINCT c.categoryname FROM entries e LEFT JOIN categories c ON e.category = c.id WHERE e.date BETWEEN '" + weekEarlier + "' AND '" + LocalDate.now() + "'";
        ResultSet rs = Database.getResultSet(categorySt);
        while (rs.next()){
            // sum all the entries in this category
            categoryTimeMap.put(rs.getString("categoryname"), 0.0f);
        }
        
        // then go through every single entry from the last week, and add to it
        String timeSt = "SELECT c.categoryname, e.starttime, e.endtime FROM entries e LEFT JOIN categories c ON e.category = c.id WHERE e.date BETWEEN '" + weekEarlier + "' AND '" + LocalDate.now() + "'";
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
        HashMap<String,Float> sortedWeeklyHashMap = nLargest(categoryTimeMap, 5);
         
        // BAR CHART
        // adapted from https://o7planning.org/en/11107/javafx-barchart-and-stackedbarchart-tutorial 
        XYChart.Series<String, Float> series = new XYChart.Series<String, Float>();
        for (Map.Entry<String, Float> entry : sortedWeeklyHashMap.entrySet()) {
            String key = entry.getKey();
            Float value = entry.getValue();
            series.getData().add(new XYChart.Data(key, value));
        }
        myWeekBarChart.getData().add(series);
        
        // Change colours
        // Get hexkeys of the categories 
        ArrayList<String> colourCodes = new ArrayList<>();
        for (Map.Entry<String, Float> entry : sortedWeeklyHashMap.entrySet()) {
            String key = entry.getKey();
            String colourSt = "SELECT DISTINCT c.hexString FROM entries e LEFT JOIN categories c ON e.category = c.id WHERE c.categoryname = '" + key + "'";
            ResultSet colourRs = Database.getResultSet(colourSt);
            while (colourRs.next()){
                String categoryColour = colourRs.getString("hexString");
                colourCodes.add(categoryColour);
            }
        }
        
        //colour coding each bar depending on category hexString
        //adapted from https://stackoverflow.com/questions/43396419/how-to-change-the-colors-of-specific-bars-using-javafx-barchart
        try {
            Node n = myWeekBarChart.lookup(".data0.chart-bar");
            n.setStyle("-fx-bar-fill: " + colourCodes.get(0));
            n = myWeekBarChart.lookup(".data1.chart-bar");
            n.setStyle("-fx-bar-fill: " + colourCodes.get(1));
            n = myWeekBarChart.lookup(".data2.chart-bar");
            n.setStyle("-fx-bar-fill: " + colourCodes.get(2));
            n = myWeekBarChart.lookup(".data3.chart-bar");
            n.setStyle("-fx-bar-fill: " + colourCodes.get(3));    
            n = myWeekBarChart.lookup(".data4.chart-bar");
            n.setStyle("-fx-bar-fill: " + colourCodes.get(4));
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            myWeekBarChart.setLegendVisible(false);
            myWeekBarChart.setTitle("Time spent on this week's top 5 categories");
        }
    }
    
    // Method for finding the top n values in a hashmap
    // Adapted from https://stackoverflow.com/questions/23805861/finding-the-n-largest-values-in-a-hashmap
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
    
**/}
