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

public class DailyLearningScreenController {

    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();

    LayoutScreenController layoutController = new LayoutScreenController();

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField wentWellTextField;

    @FXML
    private TextField couldImproveTextField;

    @FXML
    private Button saveLearningButton;

    @FXML
    private ComboBox<String> wentWellComboBox;

    @FXML
    private ComboBox<String> couldImproveComboBox;

    @FXML
    private TableView<Learning> learningList;

    @FXML
    private TableColumn<Learning, String> dateColumn;

    @FXML
    private TableColumn<Learning, String> wentWellColumn;

    @FXML
    private TableColumn<Learning, String> couldImproveColumn;

    @FXML
    private Button generateReportButton;

    // Navigation
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

    @FXML
    private Label statusLabel;

    // table cell editing adapted from:
    // https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/table-view.htm#CJAGAAEE
    @FXML
    public void initialize() throws SQLException {
        wentWellColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        wentWellColumn.setCellValueFactory(cellData -> cellData.getValue().getWentWellProperty());
        wentWellColumn.setOnEditCommit((CellEditEvent<Learning, String> t) -> {
            String newWentWell = t.getNewValue();
            try {
                Database.updateFromPreparedStatement("UPDATE daily_learning SET wentWell = ? WHERE id = ?",
                        new String[] { newWentWell, t.getRowValue().getId() });
                ((Learning) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setWentWellProperty(newWentWell);
                populateEntries();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        couldImproveColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        couldImproveColumn.setCellValueFactory(cellData -> cellData.getValue().getCouldImproveProperty());
        couldImproveColumn.setOnEditCommit((CellEditEvent<Learning, String> t) -> {
            String newCouldImprove = t.getNewValue();
            try {
                Database.updateFromPreparedStatement("UPDATE daily_learning SET couldImprove = ? WHERE id = ?",
                        new String[] { newCouldImprove, t.getRowValue().getId() });
                ((Learning) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setCouldImproveProperty(newCouldImprove);
                populateEntries();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        dateColumn.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
        datePicker.setConverter(SharedComponents.getDatePickerConverter());

        populateEntries();

        populateComboBox();
    }

    private void populateEntries() {
        try {
            learningList.setItems(getLearningListData());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void populateComboBox() throws SQLException{
        ObservableList<String>  wentWellList = FXCollections.observableArrayList();
        ObservableList<String>  couldImproveList = FXCollections.observableArrayList();
        ResultSet wwRs = Database.getResultSet(
                "SELECT DISTINCT wentWell from daily_learning;");
        while (wwRs.next()){
            wentWellList.add(wwRs.getString("wentWell"));
        }
        ResultSet ciRs = Database.getResultSet(
                "SELECT DISTINCT couldImprove from daily_learning;");
        while (ciRs.next()){
            couldImproveList.add(ciRs.getString("couldImprove"));
        }
        wentWellComboBox.setItems(wentWellList);
        couldImproveComboBox.setItems(couldImproveList);
    }

    private ObservableList<Learning> getLearningListData() throws SQLException {
        ArrayList<Learning> learningList = new ArrayList<Learning>();
        ResultSet rs = Database.getResultSet(
                "SELECT id, date, wentWell, couldImprove from daily_learning;");
        while (rs.next()) {
            Learning learning = new Learning(rs.getString("id"), rs.getString("date"),
                    rs.getString("wentWell"), rs.getString("couldImprove"));
            learningList.add(learning);
        }
        return FXCollections.observableList(learningList);
    }

    @FXML
    public void handleSaveLearningButtonAction(ActionEvent event) {
        statusLabel.setVisible(false);

        String wentWellString = wentWellTextField.getText();
        if (wentWellString.length() == 0) {
            statusLabel.setVisible(true);
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Went Well field cannot be empty");
            return;
        }

        String couldImproveString = couldImproveTextField.getText();
        if (couldImproveString.length() == 0) {
            statusLabel.setVisible(true);
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Could Improve field cannot be empty");
            return;
        }
        LocalDate date = datePicker.getValue();
        if (date == null) {
            statusLabel.setVisible(true);
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Please set a date");
            return;
        }

        try {
            Database.updateFromPreparedStatement(
                    "INSERT INTO daily_learning (date, wentWell, couldImprove) VALUES (?,?,?)",
                    new String[] { date.toString(), wentWellString, couldImproveString});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            populateEntries();
            wentWellTextField.setText("");
            couldImproveTextField.setText("");
        }
    }

    @FXML
    public void handleGenerateReportButtonAction(ActionEvent event) throws IOException, SQLException{
        // should load a new page and then do this but for the sake of time...
        calculateFrequency();
    }

    public void calculateFrequency() throws SQLException{
        // we want to only look at the last 30 days
        // get the dates for the last seven days
        org.joda.time.LocalDate monthEarlier = new DateTime().minusMonths(1).toLocalDate();

        // get the unique entries
        ArrayList<String>  wentWellList = new ArrayList<>();
        ArrayList<String>  couldImproveList = new ArrayList<>();
        ResultSet wwRs = Database.getResultSet(
                "SELECT DISTINCT wentWell from daily_learning WHERE DATE BETWEEN '" + monthEarlier + "' AND '" + LocalDate.now() + "'");
        while (wwRs.next()){
            wentWellList.add(wwRs.getString("wentWell"));
        }
        ResultSet ciRs = Database.getResultSet(
                "SELECT DISTINCT couldImprove from daily_learning WHERE DATE BETWEEN '" + monthEarlier + "' AND '" + LocalDate.now() + "'");
        while (ciRs.next()){
            couldImproveList.add(ciRs.getString("couldImprove"));
        }

        // for each entry, count how many times it occurs
        Map<String,Integer> wentWellFrequencyMap =  new HashMap<String,Integer>();
        Map<String,Integer> couldImproveFrequencyMap =  new HashMap<String,Integer>();
        for (String wentWell : wentWellList) {
            ResultSet rs = Database.getResultSet("SELECT COUNT(wentWell) FROM daily_learning WHERE wentWell = '" + wentWell + "' AND DATE BETWEEN '" + monthEarlier + "' AND '" + LocalDate.now() + "'");
            while (rs.next()){
                wentWellFrequencyMap.put(wentWell, Integer.parseInt(rs.getString("COUNT(wentWell)")));
            }
        }
        for (String couldImprove : couldImproveList) {
            ResultSet rs = Database.getResultSet("SELECT COUNT(couldImprove) FROM daily_learning WHERE couldImprove = '" + couldImprove + "' AND DATE BETWEEN '" + monthEarlier + "' AND '" + LocalDate.now() + "'");
            while (rs.next()){
                couldImproveFrequencyMap.put(couldImprove, Integer.parseInt(rs.getString("COUNT(couldImprove)")));
            }
        }
        System.out.println(entriesSortedByValues(wentWellFrequencyMap));
        System.out.println(entriesSortedByValues(couldImproveFrequencyMap));
    }

    // sorting the map in descending order so we can display it
    // adapted from https://stackoverflow.com/questions/11647889/sorting-the-mapkey-value-in-descending-order-based-on-the-value
    static <K,V extends Comparable<? super V>> List<Entry<K, V>> entriesSortedByValues(Map<K,V> map) {

        List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());

        Collections.sort(sortedEntries, new Comparator<Entry<K,V>>() {
                    @Override
                    public int compare(Entry<K,V> e1, Entry<K,V> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                }
        );
        return sortedEntries;
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


}
