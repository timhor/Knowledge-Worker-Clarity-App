package tasks;

import helper.Database;
import helper.PageSwitchHelper;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import layout.LayoutScreenController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class FocusModeScreenController {

    @FXML
    private ComboBox<String> taskDropdown;
    @FXML
    private Label timeLabel;
    @FXML
    private Label focusTaskLabel;    
    @FXML
    private Label chooseTaskLabel;
    @FXML
    private Label selectTaskWarningLabel;
    @FXML
    private Button startFocusingButton;
    @FXML
    private Button stopFocusingButton;
    

    Database database;

    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();

    LayoutScreenController layoutController = new LayoutScreenController();


    //Navigation
    // Side bar 
    @FXML
    public Button kanbanScreenButton;

    // Top Bar
    @FXML
    public Button focusModeScreenButton;
    
    
    // Music 
    @FXML
    public ComboBox<String> musicDropdown;
    @FXML
    public Label chooseMusicLabel; 
    MediaPlayer mediaPlayer;
    
//Navigation
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

    
    // when the page loads we want the choice box to populate with all the tasks available
    @FXML
    public void initialize() {
        //ensure stop focusing items are off 
        stopFocusingButton.setVisible(false);
        timeLabel.setVisible(false);
        focusTaskLabel.setVisible(false);
        //selectTaskWarningLabel.setVisible(false);
        
        //we want to populate the combo box with name entries from the database
        try {
            ResultSet rs = Database.getResultSet("SELECT * FROM entries");
            while (rs.next()){
                taskDropdown.getItems().addAll(rs.getString("description"));
            }         
            
            // set the first value
            taskDropdown.setValue("Select a task first...");
        } catch (SQLException e){
            e.printStackTrace();
        }

        
        //MUSIC PLAYER
        try {
            musicDropdown.getItems().addAll("Contemporary", "Dance", "Happy", "Jazz");
            musicDropdown.setValue("Choose a genre");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        // populate the time 
        // adapted from https://stackoverflow.com/questions/42383857/javafx-live-time-and-date
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
        LocalTime currentTime = LocalTime.now();
        timeLabel.setText("Current Time: " + currentTime.truncatedTo(ChronoUnit.SECONDS));
        }),
             new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
    
    
    @FXML
    public void StartFocusing(){
        if (taskDropdown.getValue().equals("Select a task first...")){
            selectTaskWarningLabel.setVisible(true);
            return;
        }
        
        //set other items inactive
        startFocusingButton.setVisible(false);
        chooseTaskLabel.setVisible(false);
        taskDropdown.setVisible(false);
        selectTaskWarningLabel.setVisible(false);

        //turn on stop focusing items 
        stopFocusingButton.setVisible(true);
        timeLabel.setVisible(true);
        //get whatever task we have & set the label text
        String currentFocusTask = taskDropdown.getValue();
        focusTaskLabel.setText("Time to focus on: " + currentFocusTask);
        focusTaskLabel.setVisible(true);
        musicDropdown.setVisible(true);
        chooseMusicLabel.setVisible(true);

    }
    
    @FXML
    public void StopFocusing(){
        //turn off stop focusing button
        stopFocusingButton.setVisible(false);
        timeLabel.setVisible(false);
        focusTaskLabel.setVisible(false);
        musicDropdown.setVisible(false);
        chooseMusicLabel.setVisible(false);

        
        //turn on other items 
        startFocusingButton.setVisible(true);
        chooseTaskLabel.setVisible(true);
        taskDropdown.setVisible(true);
    }
    
    /*
    Music from https://filmmusic.io, downloaded from (https://incompetech.com)
    License: CC BY (http://creativecommons.org/licenses/by/4.0/)

    Contemporary music: "On My Way" by Kevin MacLeod
    Happy music: "Wholesome" by Kevin MacLeod
    Dance music: "The Lift" by Kevin MacLeod
    Jazz music: "Airport Lounge" by Kevin MacLeod
    
    
    */
    
    @FXML
    public void handleMusicAction(ActionEvent event) {
        // if there is music playing already, stop the music
        if(mediaPlayer != null){
            // check status - stop playing if something is playing 
            // adapted from https://stackoverflow.com/questions/18340125/how-to-tell-if-mediaplayer-is-playing
            if (mediaPlayer.getStatus().equals(Status.PLAYING)){
                mediaPlayer.stop();
            }
        }
        // get whatever the user has chosen to play
        String songTitle = musicDropdown.getValue();
        Media sound = new Media(new File("music//" + songTitle + ".mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
}
