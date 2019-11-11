package layout;

import helper.PageSwitchHelper;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LayoutScreenController {

    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();

    // Top Bar
    @FXML
    public Button entriesScreenButton;

    @FXML
    public Button tasksScreenButton;

    @FXML
    public Button aboutScreenButton;

    // Side bar
    @FXML
    public Button homeScreenButton;

    @FXML
    public Button myLifeScreenButton;

    @FXML
    public Button myDayScreenButton;

    @FXML
    public Button myWeekScreenButton;

    @FXML
    public Button weeklyTrendsScreenButton;


    // Top Bar Handling
    @FXML
    public void handleEntriesScreenButtonAction(ActionEvent event) throws IOException {
        try {
            pageSwitchHelper.switcher(event, "/entries/EntriesScreen.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleTasksScreenButtonAction(ActionEvent event) throws IOException {
        try {
            pageSwitchHelper.switcher(event, "/tasks/TasksScreen.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleAboutScreenButtonAction(ActionEvent event) throws IOException {
        try {
            pageSwitchHelper.switcher(event, "/about/AboutScreen.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Add Data Handling
    @FXML
    public void handleHomeScreenButtonAction(ActionEvent event) throws IOException {
        try {
            pageSwitchHelper.switcher(event, "/layout/EntriesScreen.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleMyLifeScreenButtonAction(ActionEvent event) throws IOException {
        try {
            pageSwitchHelper.switcher(event, "/myLife/MyLifeScreen.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleMyDayScreenButtonAction(ActionEvent event) throws IOException {
        try {
            pageSwitchHelper.switcher(event, "/myDay/MyDayScreen.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleMyWeekScreenButtonAction(ActionEvent event) throws IOException {
        try {
            pageSwitchHelper.switcher(event, "/myWeek/MyWeekScreen.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleWeeklyTrendsScreenButtonAction(ActionEvent event) throws IOException {
        try {
            pageSwitchHelper.switcher(event, "/weeklyTrends/WeeklyTrendsScreen.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    //Task Input Handling 
     //relevant to the task screen - relevant to task handling
        //do date - havn't done it yet
//    @FXML
//    public void handleDoDateAction(ActionEvent event) throws IOException{
//        try {
//            pageSwitchHelper.switcher(event, "/weeklyTrends/WeeklyTrendsScreen.fxml");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//    }

    //due date - - havn't done it yet
//    @FXML
//    public void handleDueDateAction(ActionEvent event) throws IOException{
//        try {
//            pageSwitchHelper.switcher(event, "/weeklyTrends/WeeklyTrendsScreen.fxml");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//    }

    //sort by -
    @FXML
    public void handleSortDueDateButtonAction(ActionEvent event) throws IOException{
        try {
            pageSwitchHelper.switcher(event, "/weeklyTrends/WeeklyTrendsScreen.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    public void handleSortDoDateButtonAction(ActionEvent event) throws IOException{
       try {
            pageSwitchHelper.switcher(event, "/weeklyTrends/WeeklyTrendsScreen.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

//    @FXML
//    public void handlePriorityButtonAction(ActionEvent event) throws IOException{
//        try {
//            pageSwitchHelper.switcher(event, "/weeklyTrends/WeeklyTrendsScreen.fxml");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}
