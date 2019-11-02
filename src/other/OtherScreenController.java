package other;

import helper.PageSwitchHelper;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class OtherScreenController {

    @FXML
    private Button kanbanScreenButton;

    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();

    @FXML
    private void handleKanbanScreenButtonAction(ActionEvent event) throws IOException {
        try {
            pageSwitchHelper.switcher(event, "/kanban/KanbanScreen.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
