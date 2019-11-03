package about;

import helper.PageSwitchHelper;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AboutScreenController {

    @FXML
    private Button backButton;

    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();

    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        try {
            pageSwitchHelper.switcher(event, "/kanban/KanbanScreen.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
