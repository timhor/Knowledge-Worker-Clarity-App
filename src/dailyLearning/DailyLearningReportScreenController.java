///* TODO
//
//- limit the date after choosing the first date - only 1x a day 
//- save learning button does not check for what's in the combobox
//- data validation - if they choose from the combobox AND enter in the textlabel, should show a status label.
//- move items from generate report handler into its own page, design that ui, etc
//*/
//package dailyLearning;
//
//import helper.Database;
//import helper.PageSwitchHelper;
//import helper.SharedComponents;
//import layout.LayoutScreenController;
//
//import java.io.IOException;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.DatePicker;
//import javafx.scene.control.Label;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.TextField;
//import javafx.scene.control.TableColumn.CellEditEvent;
//import javafx.scene.control.cell.TextFieldTableCell;
//import javafx.scene.paint.Color;
//import org.joda.time.DateTime;
//
//public class DailyLearningReportScreenController {
//
//    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();
//
//    LayoutScreenController layoutController = new LayoutScreenController();
//
//    @FXML
//    private Button myDayScreenButton;
//
//    @FXML
//    private TableColumn<Learning, String> wentWellColumn;
//
//    @FXML
//    private Button myWeekScreenButton;
//
//    @FXML
//    private Button myLifeScreenButton;
//
//    @FXML
//    private Button tasksScreenButton;
//
//    @FXML
//    private Label statusLabel;
//
//    @FXML
//    private Button homeScreenButton;
//
//    @FXML
//    private Button entriesScreenButton;
//
//    @FXML
//    private Button aboutScreenButton;
//
//    @FXML
//    private Button weeklyTrendsScreenButton;
//    
//    
//    //went well list
//    @FXML
//    private TableView<Learning> wentWellList;
//    
//    @FXML
//    private TableColumn<Learning, String> countColumn1;
//        
//    @FXML
//    private TableColumn<Learning, String> wentWellColumn1;
//        
//    @FXML
//    private TableColumn<Learning, String> dateColumn1;
//    
//    //could improve list
//    @FXML
//    private TableView<Learning> couldImproveList;
//    
//    @FXML
//    private TableColumn<Learning, String> countColumn;
//    
//    @FXML
//    private TableColumn<Learning, String> couldImproveColumn;
//    
//    @FXML
//    private TableColumn<Learning, String> dateColumn;
//    
//    //also new
//    @FXML
//    private Button EnterDailyLearningsButton;
//            
//        
//    
////// all the stuff below is from the DailyLearningScreen
//    //this is probably needed
//    @FXML
//    public void initialize() throws SQLException {
//        // defining where the data is coming from and what its doing - talking directly to the database
//        wentWellColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        wentWellColumn.setCellValueFactory(cellData -> cellData.getValue().getWentWellProperty());
//        wentWellColumn.setOnEditCommit((CellEditEvent<Learning, String> t) -> {
//            //anonynomous function - edit the underlying data it executes the sequel statement written in orange
//            String newWentWell = t.getNewValue();
//            try {
//                Database.updateFromPreparedStatement("UPDATE daily_learning SET wentWell = ? WHERE id = ?",
//                        new String[] { newWentWell, t.getRowValue().getId() });
//                ((Learning) t.getTableView().getItems().get(t.getTablePosition().getRow()))
//                        .setWentWellProperty(newWentWell);
//                populateEntries();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        });
//        
//        //
//        couldImproveColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        couldImproveColumn.setCellValueFactory(cellData -> cellData.getValue().getCouldImproveProperty());
//        couldImproveColumn.setOnEditCommit((CellEditEvent<Learning, String> t) -> {
//            String newCouldImprove = t.getNewValue();
//            try {
//                Database.updateFromPreparedStatement("UPDATE daily_learning SET couldImprove = ? WHERE id = ?",
//                        new String[] { newCouldImprove, t.getRowValue().getId() });
//                ((Learning) t.getTableView().getItems().get(t.getTablePosition().getRow()))
//                        .setCouldImproveProperty(newCouldImprove);
//                populateEntries();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        });
//
//        dateColumn.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
//        datePicker.setConverter(SharedComponents.getDatePickerConverter());
//
//        populateWentWell();
//        populateCouldImprove();
//    }
//    
//    private void populateWentWell() {
//        try {
//            wentWellList.setItems(getWentWellData());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//    
//        private void populateCouldImprove() {
//        try {
//            couldImproveList.setItems(getCouldImproveData());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//    
////
//    private ObservableList<Learning> getWentWellData() throws SQLException {
//        ArrayList<Learning> learningList = new ArrayList<Learning>();
//        ResultSet rs = Database.getResultSet(
//                "SELECT wentWell, COUNT(*) from daily_learning GROUP BY wentWell ORDER BY COUNT(*) DESC;");
//        while (rs.next()) {
//            Learning learning = new Learning(rs.getString("id"), rs.getString("date"), 
//                    rs.getString("wentWell"), rs.getString("couldImprove"));
//            learningList.add(learning);
//        }
//        return FXCollections.observableList(learningList);
//    }
//    
//        private ObservableList<Learning> getCouldImproveData() throws SQLException {
//        ArrayList<Learning> learningList = new ArrayList<Learning>();
//        ResultSet rs = Database.getResultSet(
//                "SELECT couldImprove, COUNT(*) from daily_learning GROUP BY couldImprove ORDER BY COUNT(*) DESC;");
//        while (rs.next()) {
//            Learning learning = new Learning(rs.getString("id"), rs.getString("date"), 
//                    rs.getString("wentWell"), rs.getString("couldImprove"));
//            learningList.add(learning);
//        }
//        return FXCollections.observableList(learningList);
//    }
//    
//        
//        //is not needed? 
//    public void calculateFrequency() throws SQLException{
//        // we want to only look at the last 30 days 
//        // get the dates for the last seven days
//        org.joda.time.LocalDate monthEarlier = new DateTime().minusMonths(1).toLocalDate();
//        
//        // get the unique entries
//        ArrayList<String>  wentWellList = new ArrayList<>();
//        ArrayList<String>  couldImproveList = new ArrayList<>();
//        ResultSet wwRs = Database.getResultSet(
//                "SELECT DISTINCT wentWell from daily_learning WHERE DATE BETWEEN '" + monthEarlier + "' AND '" + LocalDate.now() + "'");
//        while (wwRs.next()){
//            wentWellList.add(wwRs.getString("wentWell"));
//        }
//        ResultSet ciRs = Database.getResultSet(
//                "SELECT DISTINCT couldImprove from daily_learning WHERE DATE BETWEEN '" + monthEarlier + "' AND '" + LocalDate.now() + "'");
//        while (ciRs.next()){
//            couldImproveList.add(ciRs.getString("couldImprove"));
//        }
//        
//        // for each entry, count how many times it occurs
//        Map<String,Integer> wentWellFrequencyMap =  new HashMap<String,Integer>(); 
//        Map<String,Integer> couldImproveFrequencyMap =  new HashMap<String,Integer>(); 
//        for (String wentWell : wentWellList) {
//            ResultSet rs = Database.getResultSet("SELECT COUNT(wentWell) FROM daily_learning WHERE wentWell = '" + wentWell + "' AND DATE BETWEEN '" + monthEarlier + "' AND '" + LocalDate.now() + "'");
//            while (rs.next()){
//                wentWellFrequencyMap.put(wentWell, Integer.parseInt(rs.getString("COUNT(wentWell)")));
//            }
//        }
//        for (String couldImprove : couldImproveList) {
//            ResultSet rs = Database.getResultSet("SELECT COUNT(couldImprove) FROM daily_learning WHERE couldImprove = '" + couldImprove + "' AND DATE BETWEEN '" + monthEarlier + "' AND '" + LocalDate.now() + "'");
//            while (rs.next()){
//                couldImproveFrequencyMap.put(couldImprove, Integer.parseInt(rs.getString("COUNT(couldImprove)")));
//            }
//        }
//        System.out.println(entriesSortedByValues(wentWellFrequencyMap));
//        System.out.println(entriesSortedByValues(couldImproveFrequencyMap));
//    }
//    
////    // sorting the map in descending order so we can display it 
////    // adapted from https://stackoverflow.com/questions/11647889/sorting-the-mapkey-value-in-descending-order-based-on-the-value
////    static <K,V extends Comparable<? super V>> List<Entry<K, V>> entriesSortedByValues(Map<K,V> map) {
////
////        List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());
////
////        Collections.sort(sortedEntries, new Comparator<Entry<K,V>>() {
////                    @Override
////                    public int compare(Entry<K,V> e1, Entry<K,V> e2) {
////                        return e2.getValue().compareTo(e1.getValue());
////                    }
////                }
////        );
////        return sortedEntries;
////    }
//
//    // Navigation
//    // Top Bar Handling
//    @FXML
//    public void handleEntriesScreenButtonAction(ActionEvent event) throws IOException {
//        layoutController.handleEntriesScreenButtonAction(event);
//    }
//
//    @FXML
//    public void handleTasksScreenButtonAction(ActionEvent event) throws IOException {
//        layoutController.handleTasksScreenButtonAction(event);
//    }
//
//    @FXML
//    public void handleAboutScreenButtonAction(ActionEvent event) throws IOException {
//        layoutController.handleAboutScreenButtonAction(event);
//    }
//
//    // Add Data Handling
//    @FXML
//    public void handleHomeScreenButtonAction(ActionEvent event) throws IOException {
//        layoutController.handleHomeScreenButtonAction(event);
//    }
//
//    @FXML
//    public void handleMyLifeScreenButtonAction(ActionEvent event) throws IOException {
//        layoutController.handleMyLifeScreenButtonAction(event);
//    }
//
//    @FXML
//    public void handleMyDayScreenButtonAction(ActionEvent event) throws IOException {
//        layoutController.handleMyDayScreenButtonAction(event);
//    }
//
//    @FXML
//    public void handleMyWeekScreenButtonAction(ActionEvent event) throws IOException {
//        layoutController.handleMyWeekScreenButtonAction(event);
//    }
//
//    @FXML
//    public void handleWeeklyTrendsScreenButtonAction(ActionEvent event) throws IOException {
//        layoutController.handleWeeklyTrendsScreenButtonAction(event);
//    }
//
//    //new for the this controller
//        @FXML
//    void handleEnterDailyLearningsButtonAction(ActionEvent event) throws IOException {
//        layoutController.handleEnterDailyLearningsButtonAction(event);
//    }
//    
//}
