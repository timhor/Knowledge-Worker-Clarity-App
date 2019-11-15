package entries;

import helper.Database;
import helper.PageSwitchHelper;
import helper.SharedComponents;
import layout.LayoutScreenController;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class EntriesScreenController {

    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();

    LayoutScreenController layoutController = new LayoutScreenController();

    private String selectedEntryId = "";

    @FXML
    private ComboBox<Category> categoryDropdown;

    @FXML
    private TextField entryDescriptionTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField startTimeTextField;

    @FXML
    private TextField endTimeTextField;

    @FXML
    private Button saveEntryButton;

    @FXML
    private TextField categoryNameTextField;

    @FXML
    private ColorPicker categoryColourPicker;

    @FXML
    private Button saveCategoryButton;

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

    @FXML
    public void initialize() {
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().getCategoryNameProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
        startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getStartTimeProperty());
        endTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getEndTimeProperty());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().getDurationProperty());

        datePicker.setConverter(SharedComponents.getDatePickerConverter());

        // StringConverter adapted from: https://stackoverflow.com/a/38367739
        categoryDropdown.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return category.getCategoryName();
            }

            @Override
            public Category fromString(String string) {
                return null;
            }
        });

        // custom row colur adapted from: https://stackoverflow.com/a/56309916
        // deselect rows adapted from: https://stackoverflow.com/a/30194680
        entryList.setRowFactory(new Callback<TableView<Entry>, TableRow<Entry>>() {
            @Override
            public TableRow<Entry> call(TableView<Entry> tableView2) {
                TableRow<Entry> row = new TableRow<Entry>() {
                    @Override
                    protected void updateItem(Entry item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || item.getCategory() == null || item.getCategory().getHexString() == null) {
                            setStyle("");
                        } else {
                            setStyle("-fx-background-color: " + item.getCategory().getHexString() + ";");
                        }
                    }
                };
                row.setOnMouseClicked(event -> {
                    if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                        saveEntryButton.setText("Update Entry");
                        Entry selectedEntry = row.getItem();
                        selectedEntryId = selectedEntry.getId();

                        categoryDropdown.setValue(selectedEntry.getCategory());
                        entryDescriptionTextField.setText(selectedEntry.getDescription());
                        datePicker.setValue(LocalDate.parse(selectedEntry.getDate()));
                        startTimeTextField.setText(selectedEntry.getStartTime());
                        endTimeTextField.setText(selectedEntry.getEndTime());
                    } else {
                        entryList.getSelectionModel().clearSelection();
                        clearInputFields();
                        saveEntryButton.setText("New Entry");
                        selectedEntryId = "";
                    }
                });
                return row;
            }
        });

        populateCategoriesIntoDropdown();
        populateEntries();
    }

    private void populateCategoriesIntoDropdown() {
        try {
            categoryDropdown.setItems(getCategories());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ObservableList<Category> getCategories() throws SQLException {
        ArrayList<Category> categories = new ArrayList<Category>();
        ResultSet rs = Database.getResultSet("SELECT * FROM categories");
        while (rs.next()) {
            Category category = new Category(rs.getString("id"), rs.getString("categoryname"),
                    rs.getString("hexstring"));
            categories.add(category);
        }
        return FXCollections.observableList(categories);
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
        ResultSet rs = Database.getResultSet(
                "SELECT e.id AS entryid, c.id AS categoryid, c.hexstring AS colour, c.categoryname, e.description, e.date, e.starttime, e.endtime "
                        + "FROM entries e LEFT JOIN categories c ON e.category = c.id;");
        while (rs.next()) {
            String categoryName = rs.getString("categoryname");
            String categoryId = rs.getString("categoryid");
            String categoryColour = rs.getString("colour");
            if (rs.wasNull()) {
                categoryName = "Uncategorised";
                categoryId = "-1";
                categoryColour = "#FFF";
            }
            Entry entry = new Entry(rs.getString("entryid"), categoryId, categoryColour, categoryName,
                    rs.getString("description"), rs.getString("date"), rs.getString("starttime"),
                    rs.getString("endtime"));
            entryList.add(entry);
        }
        return FXCollections.observableList(entryList);
    }

    @FXML
    private void handleSaveEntryButtonAction(ActionEvent event) {
        statusLabel.setVisible(false);

        String category = null;
        if (categoryDropdown.getValue() != null) {
            category = categoryDropdown.getValue().getId();
        }
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
                statusLabel.setText("End time must be later than start time");
                return;
            }
            try {
                if (selectedEntryId.length() > 0) {
                    Database.updateFromPreparedStatement(
                            "UPDATE entries SET category = ?, description = ?, date = ?, starttime = ?, endtime = ? WHERE id = ?",
                            new String[] { category, description, date.toString(), startTime, endTime, selectedEntryId });
                } else {
                    Database.updateFromPreparedStatement(
                            "INSERT INTO entries (category, description, date, starttime, endtime) VALUES (?,?,?,?,?)",
                            new String[] { category, description, date.toString(), startTime, endTime });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                populateEntries();
                clearInputFields();
            }
        } catch (ParseException e) {
            statusLabel.setVisible(true);
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Invalid time format, should be HH:MM");
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        if (selectedEntryId.length() > 0) {
            try {
                Database.updateFromPreparedStatement("DELETE FROM entries WHERE id = ?",
                        new String[] { selectedEntryId });
                populateEntries();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                clearInputFields();
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

    private void clearInputFields() {
        categoryDropdown.setValue(null);
        entryDescriptionTextField.setText("");
        startTimeTextField.setText("");
        endTimeTextField.setText("");
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
