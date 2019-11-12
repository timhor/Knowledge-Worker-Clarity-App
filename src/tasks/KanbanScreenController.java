package tasks;

import helper.Database;
import helper.PageSwitchHelper;
import helper.SharedComponents;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import layout.LayoutScreenController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class KanbanScreenController {

    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();

    LayoutScreenController layoutController = new LayoutScreenController();

    private enum TimePeriod {
        COMPLETED,
        TODAY,
        TOMORROW,
        NEXT_SEVEN_DAYS
    };

    private enum Mode {
        DO_DATE,
        DUE_DATE
    }

    // Navigation
    // Side bar
    @FXML
    public Button kanbanScreenButton;

    @FXML
    public Button focusModeScreenButton;

    // Top Bar
    @FXML
    public Button entriesScreenButton;

    @FXML
    public Button tasksScreenButton;

    @FXML
    public Button aboutScreenButton;

    @FXML
    private ListView<Task> completedListView;

    @FXML
    private Label todayLabel;

    @FXML
    private ListView<Task> todayListView;

    @FXML
    private Label tomorrowLabel;

    @FXML
    private ListView<Task> tomorrowListView;

    @FXML
    private Label nextSevenDaysLabel;

    @FXML
    private ListView<Task> nextSevenDaysListView;

    @FXML
    private Button switchModeButton;

    @FXML
    public void initialize() {
        try {
            populateTasks(Mode.DO_DATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateTasks(Mode mode) throws SQLException {
        completedListView.setItems(getTasks(TimePeriod.COMPLETED, mode));
        todayListView.setItems(getTasks(TimePeriod.TODAY, mode));
        tomorrowListView.setItems(getTasks(TimePeriod.TOMORROW, mode));
        nextSevenDaysListView.setItems(getTasks(TimePeriod.NEXT_SEVEN_DAYS, mode));
    }

    private ObservableList<Task> getTasks(TimePeriod timePeriod, Mode mode) throws SQLException {
        String query;
        String column = mode == Mode.DO_DATE ? "doDate" : "dueDate";
        switch (timePeriod) {
            case COMPLETED:
                query = "SELECT * FROM tasks WHERE completed > 0";
                break;
            case TODAY:
                query = "SELECT * FROM tasks WHERE " + column + " = date('now', 'localtime')";
                break;
            case TOMORROW:
                query = "SELECT * FROM tasks WHERE " + column + " = date('now', 'localtime', '+1 day')";
                break;
            case NEXT_SEVEN_DAYS:
                query = "SELECT * FROM tasks WHERE " + column + " BETWEEN date('now', 'localtime', '+2 day') AND date('now', 'localtime', '+7 day')";
                break;
            default:
                query = "SELECT * FROM tasks";
        }
        ArrayList<Task> tasks = new ArrayList<Task>();
        ResultSet rs = Database.getResultSet(query);
        while (rs.next()) {
            Task task = new Task(rs.getString("taskid"), rs.getString("title"), rs.getString("description"),
                    rs.getString("priority"), rs.getString("dueDate"), rs.getString("doDate"));
            tasks.add(task);
        }
        return FXCollections.observableList(tasks);
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

    @FXML
    public void handleKanbanScreenButtonAction(ActionEvent event) throws IOException {
        try {
            pageSwitchHelper.switcher(event, "/tasks/TasksScreen.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleFocusModeScreenButtonAction(ActionEvent event) throws IOException {
        try {
            pageSwitchHelper.switcher(event, "/tasks/FocusModeScreen.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
