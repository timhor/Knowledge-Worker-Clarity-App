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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;

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
    private Button saveEntryButton;

    @FXML
    private TableView<Learning> learningList;
    
    @FXML
    private TableColumn<Learning, String> dateColumn;

    @FXML
    private TableColumn<Learning, String> wentWellColumn;

    @FXML
    private TableColumn<Learning, String> couldImproveColumn;

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
    public void initialize() {

        wentWellColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        wentWellColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());
        wentWellColumn.setOnEditCommit((CellEditEvent<Learning, String> t) -> {
            String newDescription = t.getNewValue();
            try {
                Database.updateFromPreparedStatement("UPDATE entries SET description = ? WHERE id = ?",
                        new String[] { newDescription, t.getRowValue().getId() });
                ((Learning) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setDescriptionProperty(newDescription);
                populateEntries();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        couldImproveColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        couldImproveColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());
        couldImproveColumn.setOnEditCommit((CellEditEvent<Learning, String> t) -> {
            String newDescription = t.getNewValue();
            try {
                Database.updateFromPreparedStatement("UPDATE entries SET description = ? WHERE id = ?",
                        new String[] { newDescription, t.getRowValue().getId() });
                ((Learning) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setDescriptionProperty(newDescription);
                populateEntries();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        dateColumn.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
        datePicker.setConverter(SharedComponents.getDatePickerConverter());

        populateEntries();
    }
    
    private void populateEntries() {
        try {
            learningList.setItems(getLearningListData());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ObservableList<Learning> getLearningListData() throws SQLException {
        ArrayList<Learning> learningList = new ArrayList<Learning>();
        ResultSet rs = Database.getResultSet(
                "SELECT e.id, c.hexstring AS colour, c.categoryname, e.description, e.date, e.starttime, e.endtime "
                        + "FROM entries e LEFT JOIN categories c ON e.category = c.id;");
        while (rs.next()) {
            String categoryName = rs.getString("categoryname");
            if (rs.wasNull()) {
                categoryName = "Uncategorised";
            }
            Learning entry = new Learning(rs.getString("id"), rs.getString("colour"), categoryName,
                    rs.getString("description"), rs.getString("date"), rs.getString("starttime"),
                    rs.getString("endtime"));
            learningList.add(entry);
        }
        return FXCollections.observableList(learningList);
    }

    @FXML
    private void handleSaveEntryButtonAction(ActionEvent event) {
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
                    "INSERT INTO entries (category, description, date, starttime, endtime) VALUES (?,?,?,?,?)",
                    new String[] { category, description, date.toString(), startTime, endTime });
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            populateEntries();
            wentWellTextField.setText("");
            couldImproveTextField.setText("");
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

}
