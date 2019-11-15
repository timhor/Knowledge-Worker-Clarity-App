/* TODO

- limit the date after choosing the first date - only 1x a day 
- save learning button does not check for what's in the combobox
- data validation - if they choose from the combobox AND enter in the textlabel, should show a status label.
- move items from generate report handler into its own page, design that ui, etc
*/
package dailyLearning;

import helper.Database;
import helper.PageSwitchHelper;
import helper.SharedComponents;
import layout.LayoutScreenController;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import org.joda.time.DateTime;

public class DailyLearningReportScreenController {

    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();

    LayoutScreenController layoutController = new LayoutScreenController();

    @FXML
    private Button myDayScreenButton;
    
    @FXML
    private Button myWeekScreenButton;

    @FXML
    private Button myLifeScreenButton;

    @FXML
    private Button tasksScreenButton;

    @FXML
    private Label statusLabel;

    @FXML
    private Button homeScreenButton;

    @FXML
    private Button entriesScreenButton;

    @FXML
    private Button aboutScreenButton;

    @FXML
    private Button weeklyTrendsScreenButton;
    
    
    //went well list
    @FXML
    private TableView<LearningAgg> wentWellList;

    //could improve list
    @FXML
    private TableView<LearningAgg> couldImproveList;
    
    
    //also new
    @FXML
    private Button EnterDailyLearningsButton;
    
    @FXML
    private TableColumn<LearningAgg, String> wentWellDescColumn;
        
    @FXML
    private TableColumn<LearningAgg, String> wentWellCountColumn;
            
    @FXML
    private TableColumn<LearningAgg, String> couldImproveDescColumn;
        
    @FXML
    private TableColumn<LearningAgg, String> couldImproveCountColumn;
            
        
    
//// all the stuff below is from the DailyLearningScreen
    //this is probably needed
    @FXML
    public void initialize() throws SQLException {
        wentWellDescColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        wentWellDescColumn.setCellValueFactory(cellData -> cellData.getValue().getDescProperty());
        wentWellCountColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        //forced it to be a string - should be integer
        wentWellCountColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountProperty().getValue().toString()));
        
couldImproveDescColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        couldImproveDescColumn.setCellValueFactory(cellData -> cellData.getValue().getDescProperty());
        couldImproveCountColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        //forced it to be a string - should be integer
        couldImproveCountColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountProperty().getValue().toString()));
        
        populateWentWell();
        populateCouldImprove();
    }
    
    private void populateWentWell() {
           try {
            wentWellList.setItems(getWentWellData());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
        private void populateCouldImprove() {
                  try {
            couldImproveList.setItems(getCouldImproveData());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private ObservableList<LearningAgg> getWentWellData() throws SQLException {
        ArrayList<LearningAgg> aggList = new ArrayList<LearningAgg>();
                // we want to only look at the last 30 days 
        // get the dates for the last seven days
        org.joda.time.LocalDate monthEarlier = new DateTime().minusMonths(1).toLocalDate();
        ResultSet rs = Database.getResultSet(
                "SELECT wentWell as desc, COUNT(*) as num from daily_learning WHERE DATE BETWEEN '" + monthEarlier + "' AND '" + LocalDate.now() + "' GROUP BY wentWell ORDER BY num DESC");
        while (rs.next()) {
            LearningAgg aggRow = new LearningAgg(rs.getString("desc"), 
                    rs.getInt("num"));
            aggList.add(aggRow);
        }
        return FXCollections.observableList(aggList);
    }
    
        private ObservableList<LearningAgg> getCouldImproveData() throws SQLException {
        ArrayList<LearningAgg> aggList = new ArrayList<LearningAgg>();
                // we want to only look at the last 30 days 
        // get the dates for the last seven days
        org.joda.time.LocalDate monthEarlier = new DateTime().minusMonths(1).toLocalDate();
               ResultSet rs = Database.getResultSet(
                "SELECT couldImprove as desc, COUNT(*) as num from daily_learning WHERE DATE BETWEEN '" + monthEarlier + "' AND '" + LocalDate.now() + "' GROUP BY couldImprove ORDER BY num DESC");
        while (rs.next()) {
            LearningAgg aggRow = new LearningAgg(rs.getString("desc"), 
                    rs.getInt("num"));
            aggList.add(aggRow);
        }
        return FXCollections.observableList(aggList);
    }
 
    // Navigation
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

    //new for the this controller
        @FXML
    void handleEnterDailyLearningsButtonAction(ActionEvent event) throws IOException {
        layoutController.handleEnterDailyLearningsButtonAction(event);
    }
    
        @FXML
    public void handleDailyLearningScreenButtonAction(ActionEvent event) throws IOException {
        layoutController.handleDailyLearningScreenButtonAction(event);
    }
    
    
}