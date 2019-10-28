import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class KanbanScreenController {

    @FXML
    private Button otherScreenButton;

    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();

    @FXML
    private void handleOtherScreenButtonAction(ActionEvent event) throws IOException {
        try {
            pageSwitchHelper.switcher(event, "OtherScreen.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
