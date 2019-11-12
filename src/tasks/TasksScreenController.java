package tasks;

import helper.Database;
import helper.PageSwitchHelper;
import helper.SharedComponents;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import layout.LayoutScreenController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
    public void initialize() {
        doDatePicker.setConverter(SharedComponents.getDatePickerConverter());
        dueDatePicker.setConverter(SharedComponents.getDatePickerConverter());
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
                    "INSERT INTO tasks (title, description, priority, dueDate, doDate) VALUES (?,?,?,?,?)",
                    new String[] { title, description, priority, dueDate.toString(), doDate.toString() });
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            taskTitleTextField.setText("");
            taskDescriptionTextField.setText("");
            prioritySlider.setValue(0);
            dueDatePicker.setValue(null);
            doDatePicker.setValue(null);
        }
    }
}
