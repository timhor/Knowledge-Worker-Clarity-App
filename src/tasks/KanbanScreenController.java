package tasks;

import helper.Database;
import helper.PageSwitchHelper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import layout.LayoutScreenController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

public class KanbanScreenController {

    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();

    LayoutScreenController layoutController = new LayoutScreenController();

    private enum TimePeriod {
        COMPLETED,
        TODAY,
        TOMORROW,
        NEXT_SEVEN_DAYS
    };

    private enum Mode {
        DO_DATE,
        DUE_DATE
    }

    private boolean isDoDate;

    // Navigation
    // Side bar
    @FXML
    public Button kanbanScreenButton;

    @FXML
    public Button focusModeScreenButton;

    // Top Bar
    @FXML
    public Button entriesScreenButton;

    @FXML
    public Button tasksScreenButton;

    @FXML
    public Button aboutScreenButton;

    @FXML
    private ListView<Task> completedListView;

    @FXML
    private Label todayLabel;

    @FXML
    private ListView<Task> todayListView;

    @FXML
    private Label tomorrowLabel;

    @FXML
    private ListView<Task> tomorrowListView;

    @FXML
    private Label nextSevenDaysLabel;

    @FXML
    private ListView<Task> nextSevenDaysListView;

    @FXML
    private Label taskDetailsLabel;

    @FXML
    private Button switchModeButton;

    @FXML
    public void initialize() {
        isDoDate = true;
        populateTasks();

        completedListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todayListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tomorrowListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        nextSevenDaysListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // mouseover handling adapted from: https://stackoverflow.com/a/14019063
        // completedListView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
        //     @Override
        //     public ListCell<Task> call(ListView<Task> list) {
        //         final ListCell<Task> cell = new ListCell<Task>();
        //         cell.setOnMouseEntered(new EventHandler<MouseEvent>() {
        //             @Override
        //             public void handle(MouseEvent event) {
        //                 if (cell.getItem() != null) {
        //                     taskDetailsLabel.setText(cell.getItem().getDescription());
        //                 } else {
        //                     taskDetailsLabel.setText("(mouse over a task)");
        //                 }
        //             }
        //         });
        //         return cell;
        //     }
        // });
    }

    private Mode getMode() {
        return isDoDate ? Mode.DO_DATE : Mode.DUE_DATE;
    }

    private void populateTasks() {
        Mode mode = getMode();
        try {
            completedListView.setItems(getTasks(TimePeriod.COMPLETED, mode));
            todayListView.setItems(getTasks(TimePeriod.TODAY, mode));
            tomorrowListView.setItems(getTasks(TimePeriod.TOMORROW, mode));
            nextSevenDaysListView.setItems(getTasks(TimePeriod.NEXT_SEVEN_DAYS, mode));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ObservableList<Task> getTasks(TimePeriod timePeriod, Mode mode) throws SQLException {
        String query;
        String column = mode == Mode.DO_DATE ? "doDate" : "dueDate";
        switch (timePeriod) {
            case COMPLETED:
                query = "SELECT * FROM tasks WHERE completed = 1";
                break;
            case TODAY:
                query = "SELECT * FROM tasks WHERE " + column + " = date('now', 'localtime') AND completed = 0";
                break;
            case TOMORROW:
                query = "SELECT * FROM tasks WHERE " + column + " = date('now', 'localtime', '+1 day') AND completed = 0";
                break;
            case NEXT_SEVEN_DAYS:
                query = "SELECT * FROM tasks WHERE (" + column + " BETWEEN date('now', 'localtime', '+2 day') AND date('now', 'localtime', '+7 day')) AND completed = 0";
                break;
            default:
                query = "SELECT * FROM tasks";
        }
        ArrayList<Task> tasks = new ArrayList<Task>();
        ResultSet rs = Database.getResultSet(query);
        while (rs.next()) {
            Task task = new Task(rs.getString("taskId"), rs.getString("title"), rs.getString("description"),
                    rs.getString("priority"), rs.getString("dueDate"), rs.getString("doDate"));
            tasks.add(task);
        }
        return FXCollections.observableList(tasks);
    }

    // the following drag-and-drop handlers adapted from:
    // - https://examples.javacodegeeks.com/desktop-java/javafx/event-javafx/javafx-drag-drop-example/
    // - https://docs.oracle.com/javafx/2/drag_drop/jfxpub-drag_drop.htm

    @SuppressWarnings("unchecked")
    @FXML
    private void handleDragDetected(MouseEvent event) {
        ListView<Task> listView = (ListView<Task>) event.getSource();
        Task selection = listView.getSelectionModel().getSelectedItem();
        if (selection == null) {
            event.consume();
            return;
        }
        Dragboard dragboard = listView.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(selection.getTaskID());
        dragboard.setContent(content);
        event.consume();
    }

    @SuppressWarnings("unchecked")
    @FXML
    private void handleDragOver(DragEvent event) {
        ListView<Task> source = (ListView<Task>) event.getGestureSource();
        ListView<Task> target = (ListView<Task>) event.getGestureTarget();
        Dragboard dragboard = event.getDragboard();
        if (source != target && dragboard.hasString()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }

    @SuppressWarnings("unchecked")
    @FXML
    private void handleDragDropped(DragEvent event) {
        ListView<Task> source = (ListView<Task>) event.getGestureSource();
        ListView<Task> target = (ListView<Task>) event.getGestureTarget();
        boolean dragCompleted = false;
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasString()) {
            String draggedTaskId = dragboard.getString();
            try {
                Task draggedTask = getSelectedTask(draggedTaskId);
                String targetColumn = target.getId();
                String column = getMode() == Mode.DO_DATE ? "doDate" : "dueDate";
                switch (targetColumn) {
                    case "completedListView":
                        Database.updateFromPreparedStatement(
                                "UPDATE tasks SET completed = 1 WHERE taskId = ?",
                                new String[] { draggedTaskId });
                        break;
                    case "todayListView":
                        Database.updateFromPreparedStatement(
                                "UPDATE tasks SET " + column + " = date('now', 'localtime'), completed = 0 WHERE taskId = ?",
                                new String[] { draggedTaskId });
                        break;
                    case "tomorrowListView":
                        Database.updateFromPreparedStatement(
                                "UPDATE tasks SET " + column + " = date('now', 'localtime', '+1 day'), completed = 0 WHERE taskId = ?",
                                new String[] { draggedTaskId });
                        break;
                    case "nextSevenDaysListView":
                        Database.updateFromPreparedStatement(
                                "UPDATE tasks SET " + column + " = date('now', 'localtime', '+7 day'), completed = 0 WHERE taskId = ?",
                                new String[] { draggedTaskId });
                        break;
                }
                source.getItems().add(draggedTask);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                dragCompleted = true;
            }
        }
        // transfer not successful
        event.setDropCompleted(dragCompleted);
        event.consume();
    }

    @FXML
    private void handleDragDone(DragEvent event) {
        populateTasks();
        event.consume();
    }

    private Task getSelectedTask(String id) throws SQLException {
        ResultSet rs = Database.getResultSetFromPreparedStatement(
                "SELECT * FROM tasks WHERE taskId = ?",
                new String[] { id });
        Task task = new Task(rs.getString("taskId"), rs.getString("title"), rs.getString("description"),
                rs.getString("priority"), rs.getString("dueDate"), rs.getString("doDate"));
        return task;
    }

    @FXML
    private void handleSwitchModeButtonAction(ActionEvent event) {
        isDoDate = !isDoDate;
        if (isDoDate) {
            switchModeButton.setText("Switch to Due Date mode");
        } else {
            switchModeButton.setText("Switch to Do Date mode");
        }
        populateTasks();
    }

    // Navigation
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
