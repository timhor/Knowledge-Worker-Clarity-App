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
    public CategoryAxis weeklyTrendsCategoryAxis;
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
        
        long maxTimeOnTask = 0;
        
        try {
            ResultSet rs = Database.getResultSet("SELECT * FROM entries");
            while (rs.next()){
                //We want to figure out the highest y axis values
                String startTime = rs.getString("starttime");
                String endTime = rs.getString("endTime");
                long duration = parseTimeInMs(endTime) - parseTimeInMs(startTime);
                if (maxTimeOnTask < duration){
                    maxTimeOnTask = duration;
                }

            }
        } catch (SQLException e){
            e.printStackTrace();            
        }
        
        // ** LINE GRAPH ** 
        // Adapted from https://www.tutorialspoint.com/javafx/line_chart.htm 

        //Defining the y axis. we want to have 10 minor tick marks
        int yAxisMax = (int) (TimeUnit.MILLISECONDS.toMinutes(maxTimeOnTask) + 10);
        int numTickMarks = yAxisMax / 10;
        NumberAxis weeklyTrendsNumberAxis = new NumberAxis(0, yAxisMax, 50); 
        weeklyTrendsNumberAxis.setLabel("Minutes spent"); 
        
        
        //Defining the x axis   
        // We want the last 12 weeks of data - adapted from https://stackoverflow.com/questions/31467524/how-to-get-all-week-dates-for-given-date-java       
        //Find start of each week from that start Date, which will become our x axis
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(3);
        LocalDate thisMonday = startDate.withDayOfWeek(DateTimeConstants.MONDAY);
        if (startDate.isAfter(thisMonday)) {
            startDate = thisMonday.plusWeeks(1); // start on next monday
        } else {
            startDate = thisMonday; // start on this monday
        }
        
        ArrayList<String> weeksAxis = new ArrayList<String>();
        while (startDate.isBefore(endDate)) {
            System.out.println(startDate);
            weeksAxis.add(startDate.toString());
            startDate = startDate.plusWeeks(1);
        }
        
        weeklyTrendsCategoryAxis.setCategories(FXCollections.<String>observableArrayList(weeksAxis));
        weeklyTrendsCategoryAxis.setLabel("Week of"); 
 
        
        //Create a new series for each category.
        List categoryNames = new ArrayList();
        try {

            ResultSet rs = Database.getResultSet("SELECT DISTINCT category FROM entries");
            while (rs.next()){
                categoryNames.add(rs.getString("category"));
                System.out.println(rs.getString("category"));
            }
        } catch (SQLException e){
            e.printStackTrace();            
        }        

        for (int i = 0; i < categoryNames.size(); i++){
            System.out.println(categoryNames.get(i));
            XYChart.Series series = new XYChart.Series(); 
            series.setName("Category: " + categoryNames.get(i)); 

//            series.getData().add(new XYChart.Data("September", 60));
//            series.getData().add(new XYChart.Data("October", 150)); 
//            series.getData().add(new XYChart.Data("November", 300)); 

            weeklyTrendsLineChart.getData().add(series);  
        }                
    }

}
