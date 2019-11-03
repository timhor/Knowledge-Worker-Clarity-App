package kanban;

import helper.PageSwitchHelper;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class KanbanScreenController {

    @FXML
    private Button aboutScreenButton;

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
