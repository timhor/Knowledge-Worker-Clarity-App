package tasks;

import helper.PageSwitchHelper;
import java.io.IOException;
import layout.LayoutScreenController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TasksScreenController {

    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();

    LayoutScreenController layoutController = new LayoutScreenController();

    //Navigation
    // Side bar
    @FXML
    public Button kanbanScreenButton;

    // Top Bar
    @FXML
    public Button focusModeScreenButton;

    // Top Bar
    @FXML
    public Button entriesScreenButton;

    @FXML
    public Button tasksScreenButton;

    @FXML
    public Button aboutScreenButton;

    //Navigation
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
