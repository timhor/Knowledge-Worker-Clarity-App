package entries;

import helper.Database;
import helper.PageSwitchHelper;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class EntriesScreenController {

    @FXML
    private Button aboutScreenButton;

    @FXML
    private TextField entryDescriptionTextField;

    @FXML
    private TextField startTimeTextField;

    @FXML
    private TextField endTimeTextField;

    @FXML
    private Button saveEntryButton;

    @FXML
    private TableView<Entry> entryList;

    @FXML
    private TableColumn<Entry, String> categoryColumn;

    @FXML
    private TableColumn<Entry, String> descriptionColumn;

    @FXML
    private TableColumn<Entry, String> startTimeColumn;

    @FXML
    private TableColumn<Entry, String> endTimeColumn;

    @FXML
    private TableColumn<Entry, String> durationColumn;

    Database database = new Database();

    @FXML
    public void initialize() {
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().getCategoryProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());
        startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getStartTimeProperty());
        endTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getEndTimeProperty());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().getDurationProperty());

        try {
            entryList.setItems(getEntryListData());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ObservableList<Entry> getEntryListData() throws SQLException {
        ArrayList<Entry> entryList = new ArrayList<Entry>();
        ResultSet rs = database.getResultSet("SELECT * FROM entries");
        while (rs.next()) {
            String category = rs.getString("category");
            if (rs.wasNull()) {
                category = "Not set";
            }
            Entry entry = new Entry(category, rs.getString("desc"), rs.getString("starttime"), rs.getString("endtime"));
            entryList.add(entry);
        }
        return FXCollections.observableList(entryList);
    }

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
