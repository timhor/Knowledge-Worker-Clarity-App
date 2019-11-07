package tasks;

import helper.Database;
import helper.PageSwitchHelper;
import java.io.IOException;
import java.sql.ResultSet;
import layout.LayoutScreenController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class FocusModeScreenController {

    @FXML
    private Button submitCategoryButton;

    @FXML
    private TextField categoryNameTextField;

    Database database;

    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();

    LayoutScreenController layoutController = new LayoutScreenController();

    @FXML
    private void handleSubmitCategoryButtonAction(ActionEvent event) {
        try {
            ResultSet rs = database.getResultSetFromPreparedStatement("SELECT * FROM categoryName",
                    new String[] { categoryNameTextField.getText() });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Navigation
    // Side bar 
    @FXML
    public Button kanbanScreenButton;

    // Top Bar
    @FXML
    public Button focusModeScreenButton;

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
