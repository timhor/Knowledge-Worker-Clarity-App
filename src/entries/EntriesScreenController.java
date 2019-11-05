package entries;

import helper.Database;
import helper.PageSwitchHelper;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class EntriesScreenController {

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

}
