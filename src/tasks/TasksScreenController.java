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
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class TasksScreenController {

    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();

    LayoutScreenController layoutController = new LayoutScreenController();

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

    // inputting task
    @FXML
    private TextField taskDescriptionTextField;

    @FXML
    private TextField taskTitleTextField;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private DatePicker doDatePicker;

    @FXML
    private Button saveTaskButton;

    @FXML
    private Slider prioritySlider;

    @FXML
    private Label statusLabel;

    @FXML
    private TableView<Task> taskList;

    @FXML
    private TableColumn<Task, String> titleColumn;

    @FXML
    private TableColumn<Task, String> descriptionColumn;

    @FXML
    private TableColumn<Task, String> priorityColumn;

    @FXML
    private TableColumn<Task, String> doDateColumn;

    @FXML
    private TableColumn<Task, String> dueDateColumn;

    // table cell editing adapted from:
    // https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/table-view.htm#CJAGAAEE
    @FXML
    public void initialize() {
        doDatePicker.setConverter(SharedComponents.getDatePickerConverter());
        dueDatePicker.setConverter(SharedComponents.getDatePickerConverter());

        titleColumn.setCellValueFactory(cellData -> cellData.getValue().getTitleProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());
        priorityColumn.setCellValueFactory(cellData -> cellData.getValue().getPriorityProperty());
        doDateColumn.setCellValueFactory(cellData -> cellData.getValue().getDoDateProperty());
        dueDateColumn.setCellValueFactory(cellData -> cellData.getValue().getDueDateProperty());

        populateTasks();
    }

    private void populateTasks() {
        try {
            taskList.setItems(getTasks());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ObservableList<Task> getTasks() throws SQLException {
        ArrayList<Task> tasks = new ArrayList<Task>();
        ResultSet rs = Database.getResultSet("SELECT * FROM tasks WHERE completed = '0'");
        while (rs.next()) {
            Task task = new Task(rs.getString("taskId"), rs.getString("title"), rs.getString("description"),
                    rs.getString("priority"), rs.getString("dueDate"), rs.getString("doDate"));
            tasks.add(task);
        }
        return FXCollections.observableList(tasks);
    }

    @FXML
    private void handleSaveTaskButtonAction(ActionEvent event) {
        statusLabel.setVisible(false);

        String title = taskTitleTextField.getText();

        if (title.length() == 0) {
            statusLabel.setVisible(true);
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Title cannot be empty");
            return;
        }

        String description = taskDescriptionTextField.getText();
        if (description.length() == 0) {
            statusLabel.setVisible(true);
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Description cannot be empty");
            return;
        }

        String priority = Integer.toString((int) prioritySlider.getValue());

        LocalDate doDate = doDatePicker.getValue();
        if (doDate == null) {
            statusLabel.setVisible(true);
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Please set a do date");
            return;
        }

        LocalDate dueDate = dueDatePicker.getValue();
        if (dueDate == null) {
            statusLabel.setVisible(true);
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Please set a due date");
            return;
        }

        try {
            Database.updateFromPreparedStatement(
                    "INSERT INTO tasks (title, description, priority, dueDate, doDate, completed) VALUES (?,?,?,?,?,?)",
                    new String[] { title, description, priority, dueDate.toString(), doDate.toString(), "0" });
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            populateTasks();
            taskTitleTextField.setText("");
            taskDescriptionTextField.setText("");
            prioritySlider.setValue(0);
            dueDatePicker.setValue(null);
            doDatePicker.setValue(null);
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        Task taskToDelete = taskList.getSelectionModel().getSelectedItem();
        if (taskToDelete != null) {
            try {
                Database.updateFromPreparedStatement("DELETE FROM tasks WHERE taskId = ?",
                        new String[] { taskToDelete.getTaskID() });
                populateTasks();
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
