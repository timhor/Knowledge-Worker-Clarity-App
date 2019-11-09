package entries;

import helper.Database;
import helper.PageSwitchHelper;
import layout.LayoutScreenController;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

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
import javafx.util.StringConverter;

public class EntriesScreenController {

    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();

    LayoutScreenController layoutController = new LayoutScreenController();

    @FXML
    private TextField entryDescriptionTextField;

    @FXML
    private DatePicker datePicker = new DatePicker(LocalDate.now());

    @FXML
    private TextField startTimeTextField;

    @FXML
    private TextField endTimeTextField;

    @FXML
    private Button saveEntryButton;

    @FXML
    private TableView<Entry> entryList;

    @FXML
    private TableColumn<Entry, String> categoryColumn;

    @FXML
    private TableColumn<Entry, String> descriptionColumn;

    @FXML
    private TableColumn<Entry, String> dateColumn;

    @FXML
    private TableColumn<Entry, String> startTimeColumn;

    @FXML
    private TableColumn<Entry, String> endTimeColumn;

    @FXML
    private TableColumn<Entry, String> durationColumn;

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
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().getCategoryProperty());

        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());
        descriptionColumn.setOnEditCommit((CellEditEvent<Entry, String> t) -> {
            String newDescription = t.getNewValue();
            try {
                Database.updateFromPreparedStatement("UPDATE entries SET description = ? WHERE id = ?",
                        new String[] { newDescription, t.getRowValue().getId() });
                ((Entry) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setDescriptionProperty(newDescription);
                populateEntries();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        dateColumn.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());

        startTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getStartTimeProperty());
        startTimeColumn.setOnEditCommit((CellEditEvent<Entry, String> t) -> {
            String newStartTime = t.getNewValue();
            try {
                Database.updateFromPreparedStatement("UPDATE entries SET starttime = ? WHERE id = ?",
                        new String[] { newStartTime, t.getRowValue().getId() });
                ((Entry) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setStartTimeProperty(newStartTime);
                populateEntries();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        endTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        endTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getEndTimeProperty());
        endTimeColumn.setOnEditCommit((CellEditEvent<Entry, String> t) -> {
            String newEndTime = t.getNewValue();
            try {
                Database.updateFromPreparedStatement("UPDATE entries SET endtime = ? WHERE id = ?",
                        new String[] { newEndTime, t.getRowValue().getId() });
                ((Entry) t.getTableView().getItems().get(t.getTablePosition().getRow())).setEndTimeProperty(newEndTime);
                populateEntries();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        durationColumn.setCellValueFactory(cellData -> cellData.getValue().getDurationProperty());

        // date picker formatting adapted from: https://code.makery.ch/blog/javafx-8-date-picker/
        datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        populateEntries();
    }

    private void populateEntries() {
        try {
            entryList.setItems(getEntryListData());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ObservableList<Entry> getEntryListData() throws SQLException {
        ArrayList<Entry> entryList = new ArrayList<Entry>();
        ResultSet rs = Database.getResultSet("SELECT * FROM entries");
        while (rs.next()) {
            String category = rs.getString("category");
            if (rs.wasNull()) {
                category = "Not set";
            }
            Entry entry = new Entry(rs.getString("id"), category, rs.getString("description"), rs.getString("date"),
                    rs.getString("starttime"), rs.getString("endtime"));
            entryList.add(entry);
        }
        return FXCollections.observableList(entryList);
    }

    @FXML
    private void handleSaveEntryButtonAction(ActionEvent event) {
        statusLabel.setVisible(false);

        String category = null;
        String description = entryDescriptionTextField.getText();
        if (description.length() == 0) {
            statusLabel.setVisible(true);
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Description cannot be empty");
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
            String startTime = validateAndFormatTime(startTimeTextField.getText().trim());
            String endTime = validateAndFormatTime(endTimeTextField.getText().trim());
            long duration = Entry.parseTimeInMs(endTime) - Entry.parseTimeInMs(startTime);
            if (duration <= 0) {
                statusLabel.setVisible(true);
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("End time must later than start time");
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
                entryDescriptionTextField.setText("");
                startTimeTextField.setText("");
                endTimeTextField.setText("");
            }
        } catch (ParseException e) {
            statusLabel.setVisible(true);
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Invalid time format, should be HH:MM");
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        Entry entryToDelete = entryList.getSelectionModel().getSelectedItem();
        if (entryToDelete != null) {
            try {
                Database.updateFromPreparedStatement("DELETE FROM entries WHERE id = ?",
                        new String[] { entryToDelete.getId() });
                populateEntries();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String validateAndFormatTime(String time) throws ParseException {
        String timeFormat = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";
        String timeFormatNoLeadingZero = "^[0-9]:[0-5][0-9]$";
        if (time.matches(timeFormatNoLeadingZero)) {
            return "0" + time;
        }
        if (time.matches(timeFormat)) {
            return time;
        }
        throw new ParseException("Invalid time format", 0);
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
