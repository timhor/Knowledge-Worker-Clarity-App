// THIS FILE ADAPTED FROM INFS2605 19T3 WEEK 5 TUTORIAL
package dailyLearning;

import helper.Database;
import helper.PageSwitchHelper;
import helper.SharedComponents;
import layout.LayoutScreenController;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
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

    @FXML
    private Label ErrorLabel;

    @FXML
    private Label warningLabel;

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

    @FXML
    public Button Delete;

    // table cell editing adapted from:
    // https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/table-view.htm#CJAGAAEE
    @FXML
    public void initialize() throws SQLException, IOException {
        wentWellColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        wentWellColumn.setCellValueFactory(cellData -> cellData.getValue().getWentWellProperty());
        wentWellColumn.setOnEditCommit((CellEditEvent<Learning, String> t) -> {
            String newWentWell = t.getNewValue();
            try {
                Database.updateFromPreparedStatement("UPDATE daily_learning SET wentWell = ? WHERE id = ?",
                        new String[]{newWentWell, t.getRowValue().getId()});
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
                        new String[]{newCouldImprove, t.getRowValue().getId()});
                ((Learning) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setCouldImproveProperty(newCouldImprove);
                populateEntries();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        dateColumn.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
        datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                //disabling of dates in datePicker adapted from:
                //https://stackoverflow.com/questions/48238855/how-to-disable-past-dates-in-datepicker-of-javafx-scene-builder
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                ResultSet rs = null;
//                try {
//                    rs = Database.getResultSet(
//                            "SELECT date from daily_learning where date  = '" + date.format(DateTimeFormatter.ofPattern("yyyy-mm-dd")) + "';"
//                    );
//                            
//                } catch (SQLException ex) {}
//                System.out.println(rs);
//                boolean hasResults = false;
//                if (rs != null) {
//                    try {
//                        hasResults = rs.next() != false;
//                    } catch (SQLException ex) {}
//                }
                setDisable(empty || date.compareTo(today) > 0);
            }
        });

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

    private void populateComboBox() throws SQLException {
        ObservableList<String> wentWellList = FXCollections.observableArrayList();
        ObservableList<String> couldImproveList = FXCollections.observableArrayList();
        ResultSet wwRs = Database.getResultSet(
                "SELECT DISTINCT wentWell from daily_learning;");
        while (wwRs.next()) {
            wentWellList.add(wwRs.getString("wentWell"));
        }
        ResultSet ciRs = Database.getResultSet(
                "SELECT DISTINCT couldImprove from daily_learning;");
        while (ciRs.next()) {
            couldImproveList.add(ciRs.getString("couldImprove"));
        }
        wentWellComboBox.setItems(wentWellList);
        couldImproveComboBox.setItems(couldImproveList);
    }

    private ObservableList<Learning> getLearningListData() throws SQLException {
        ArrayList<Learning> learningList = new ArrayList<Learning>();
        ResultSet rs = Database.getResultSet(
                "SELECT id, date, wentWell, couldImprove from daily_learning ORDER BY date asc;");
        while (rs.next()) {
            Learning learning = new Learning(rs.getString("id"), rs.getString("date"),
                    rs.getString("wentWell"), rs.getString("couldImprove"));
            learningList.add(learning);
        }

        detectMissingDates();
        return FXCollections.observableList(learningList);
    }

    @FXML
    public void handleSaveLearningButtonAction(ActionEvent event) {
        statusLabel.setVisible(false);
        String wentWellString;

        if (wentWellTextField.getText().trim().length() == 0 && wentWellComboBox.getValue() != null) {
            wentWellString = wentWellComboBox.getValue();
        } else if (wentWellComboBox.getValue() == null && wentWellTextField.getText().trim().length() > 0) {
            wentWellString = wentWellTextField.getText();
        } else {
            statusLabel.setVisible(true);
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Went Well field entered incorrectly: cannot be left blank or filled in twice");
            return;
        }

        String couldImproveString;
        if (couldImproveTextField.getText().trim().length() == 0 && couldImproveComboBox.getValue() != null) {
            couldImproveString = couldImproveComboBox.getValue();
        } else if (couldImproveComboBox.getValue() == null && couldImproveTextField.getText().trim().length() > 0) {
            couldImproveString = couldImproveTextField.getText();
        } else {
            statusLabel.setVisible(true);
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Could Improve field entered incorrectly: cannot be left blank or filled in twice");
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
                    new String[]{date.toString(), wentWellString, couldImproveString});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            populateEntries();
            wentWellTextField.setText("");
            couldImproveTextField.setText("");
            couldImproveComboBox.setValue(null);
            wentWellComboBox.setValue(null);
        }
    }

    @FXML
    public void handleGenerateReportButtonAction(ActionEvent event) throws IOException {
        layoutController.handleGenerateReportButtonAction(event);
    }

    public void calculateFrequency() throws SQLException {
        // we want to only look at the last 30 days 
        // get the dates for the last seven days
        org.joda.time.LocalDate monthEarlier = new DateTime().minusMonths(1).toLocalDate();

        // get the unique entries
        ArrayList<String> wentWellList = new ArrayList<>();
        ArrayList<String> couldImproveList = new ArrayList<>();
        ResultSet wwRs = Database.getResultSet(
                "SELECT DISTINCT wentWell from daily_learning WHERE DATE BETWEEN '" + monthEarlier + "' AND '" + LocalDate.now() + "'");
        while (wwRs.next()) {
            wentWellList.add(wwRs.getString("wentWell"));
        }
        ResultSet ciRs = Database.getResultSet(
                "SELECT DISTINCT couldImprove from daily_learning WHERE DATE BETWEEN '" + monthEarlier + "' AND '" + LocalDate.now() + "'");
        while (ciRs.next()) {
            couldImproveList.add(ciRs.getString("couldImprove"));
        }

        Map<String, Integer> wentWellFrequencyMap = new HashMap<String, Integer>();
        Map<String, Integer> couldImproveFrequencyMap = new HashMap<String, Integer>();
        for (String wentWell : wentWellList) {
            ResultSet rs = Database.getResultSet("SELECT COUNT(wentWell) FROM daily_learning WHERE wentWell = '" + wentWell + "' AND DATE BETWEEN '" + monthEarlier + "' AND '" + LocalDate.now() + "'");
            while (rs.next()) {
                wentWellFrequencyMap.put(wentWell, Integer.parseInt(rs.getString("COUNT(wentWell)")));
            }
        }
        for (String couldImprove : couldImproveList) {
            ResultSet rs = Database.getResultSet("SELECT COUNT(couldImprove) FROM daily_learning WHERE couldImprove = '" + couldImprove + "' AND DATE BETWEEN '" + monthEarlier + "' AND '" + LocalDate.now() + "'");
            while (rs.next()) {
                couldImproveFrequencyMap.put(couldImprove, Integer.parseInt(rs.getString("COUNT(couldImprove)")));
            }
        }
        System.out.println(entriesSortedByValues(wentWellFrequencyMap));
        System.out.println(entriesSortedByValues(couldImproveFrequencyMap));
    }

    // sorting the map in descending order so we can display it 
    // adapted from https://stackoverflow.com/questions/11647889/sorting-the-mapkey-value-in-descending-order-based-on-the-value
    static <K, V extends Comparable<? super V>> List<Entry<K, V>> entriesSortedByValues(Map<K, V> map) {

        List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

        Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
            @Override
            public int compare(Entry<K, V> e1, Entry<K, V> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        }
        );
        return sortedEntries;
    }

    private void detectMissingDates() throws SQLException {
        org.joda.time.LocalDate monthEarlier = new DateTime().minusMonths(1).toLocalDate();
        ResultSet rs = Database.getResultSet(
                "SELECT id, date, wentWell, couldImprove from daily_learning WHERE (date BETWEEN '" + monthEarlier + "' AND '" + LocalDate.now() + "')ORDER BY date asc;");
        Date prev = null;
        Date next = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder msg = new StringBuilder();

        while (rs.next()) {
            Learning learning = new Learning(rs.getString("id"), rs.getString("date"),
                    rs.getString("wentWell"), rs.getString("couldImprove"));
            try {
                if (null != prev) {
                    next = df.parse(rs.getString("date"));
                    Period period = Period.between(
                            new java.sql.Date(prev.getTime()).toLocalDate(),
                            new java.sql.Date(next.getTime()).toLocalDate()
                    );
                    if (period.getDays() > 1 || period.getDays() < 1) {
                        warningLabel.setVisible(true);
                        ErrorLabel.setVisible(true);
                        ErrorLabel.setTextFill(Color.RED);
                        warningLabel.setTextFill(Color.RED);
                        //adding multiple lines in javaFX label adapted from: 
                        //https://stackoverflow.com/questions/36568058/how-to-add-multiple-lines-in-label-javafx
                        //conversion from SimpleDateFormat adapted from: 
                        //https://www.mkyong.com/java/java-how-to-get-current-date-time-date-and-calender/?fbclid=IwAR3qATXpl8DyQzW5ZV1yNFNyBzpxwpbGcODIGBTLn1Dn2UqzCgB7fvWCjGk
                        msg.append("Please fill out missing daily learnings between: " + df.format(prev).toString() + " & " + df.format(next).toString());
                        msg.append(".\n");
                        ErrorLabel.setText(msg.toString());
                        warningLabel.setText("Some Daily Learnings are missing from memory.");
                    }
                }
                prev = df.parse(rs.getString("date"));
            } catch (ParseException ex) {
                Logger.getLogger(DailyLearningScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    public void handleDeleteButtonAction(ActionEvent event) {
        Learning learningToDelete = learningList.getSelectionModel().getSelectedItem();
        if (learningToDelete != null) {
            try {
                Database.updateFromPreparedStatement("DELETE FROM daily_learning WHERE id = ?",
                        new String[]{learningToDelete.getId()});
                populateEntries();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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

    public void handleDailyLearningScreenButtonAction(ActionEvent event) throws IOException {
        layoutController.handleDailyLearningScreenButtonAction(event);
    }
}
