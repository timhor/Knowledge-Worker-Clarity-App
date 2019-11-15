package myLife;

import static entries.Entry.parseTimeInMs;
import helper.Database;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
               Float valuePercentage = 100*value / totalTimeSpent;
               DecimalFormat df = new DecimalFormat("0");
               String valuePercentageString = String.valueOf(df.format(valuePercentage));
                myPieChart.getData().add(new PieChart.Data(key + "\n " + valuePercentageString + "%" , value / totalTimeSpent));
            }
            try {
                ArrayList<String> colourCodes = new ArrayList<>();
                for (Map.Entry<String, Float> entry : data.entrySet()) {
                    String key = entry.getKey();
                    String colourSt = "SELECT DISTINCT c.hexString FROM entries e LEFT JOIN categories c ON e.category = c.id WHERE c.categoryname = '" + key + "'";
                    ResultSet colourRs = Database.getResultSet(colourSt);
                    while (colourRs.next()) {
                        String categoryColour = colourRs.getString("hexString");
                        colourCodes.add(categoryColour);
                    }
                }
                try {
                    Node n = myPieChart.lookup(".data0.chart-pie");
                    for (int i = 0; i < colourCodes.size(); i++){
                        if (i != 0){
                            n = myPieChart.lookup(".data" + i + ".chart-pie");
                            //System.out.println(".data" + i + ".chart-pie");
                        }
                        n.setStyle("-fx-pie-color: " + colourCodes.get(i) + ";");
                        //System.out.println("-fx-pie-color: " + colourCodes.get(i) + ";");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myPieChart.setLegendVisible(false);
                myPieChart.setTitle("Breakdown of my time spent");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
