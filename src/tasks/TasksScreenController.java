package tasks;

import helper.Database;
import helper.PageSwitchHelper;
import java.io.IOException;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class TasksScreenController {

    @FXML
    private Button aboutScreenButton;

    @FXML
    private Button submitCategoryButton;

    @FXML
    private TextField categoryNameTextField;

    Database database;

    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();

    @FXML
    private void handleAboutScreenButtonAction(ActionEvent event) throws IOException {
        try {
            pageSwitchHelper.switcher(event, "/about/AboutScreen.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleSubmitCategoryButtonAction(ActionEvent event) {
        try {
            ResultSet rs = database.getResultSetFromPreparedStatement("SELECT * FROM categoryName",
                    new String[] { categoryNameTextField.getText() });
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

}
