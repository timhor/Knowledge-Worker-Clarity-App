package myLife;

import entries.EntriesScreenController;
import entries.Entry;
import static entries.Entry.parseTimeInMs;
import helper.Database;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.Chart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import layout.LayoutScreenController;

public class MyLifeScreenController {

    @FXML
    public PieChart pieChart;

    @FXML
    public Pane pane;

    LayoutScreenController layoutController = new LayoutScreenController();

    @FXML
    public void initialize() {

        //EntriesScreenController entriesScreenControllerObject = new EntriesScreenController();
        //List categoryNames = new ArrayList();
        //ArrayList entries = new ArrayList<Entry>();
        try {

            HashMap<String, Float> data = new HashMap<String, Float>();

            String categorySt = "SELECT DISTINCT c.categoryname FROM entries e LEFT JOIN categories c ON e.category = c.id";
            ResultSet rs = Database.getResultSet(categorySt);
            while (rs.next()) {
                data.put(rs.getString("categoryname"), 0.0f);
            }

            String timeSt = "SELECT c.categoryname, e.starttime, e.endtime FROM entries e LEFT JOIN categories c ON e.category = c.id";
            ResultSet timeRs = Database.getResultSet(timeSt);
            long total; 
            
            while (timeRs.next()) {
                // calculate the time it takes 
                String startTime = timeRs.getString("starttime");
                String endTime = timeRs.getString("endtime");
                long duration = parseTimeInMs(endTime) - parseTimeInMs(startTime);

                // append to the hashmap
                data.put(timeRs.getString("categoryname"), data.get(timeRs.getString("categoryname")) + duration);
                System.out.println(data);
            }
            ArrayList<PieChart.Data> pieChartData = new ArrayList<>();

            for (Map.Entry<String, Float> entry : data.entrySet()) {
                String cat = entry.getKey();

                Float duration = entry.getValue();

                pieChartData.add(new PieChart.Data(cat, duration));

            }
            final PieChart pieChart = new PieChart(FXCollections.observableArrayList(pieChartData));

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

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

}
