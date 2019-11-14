import java.sql.SQLException;

import helper.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KnowledgeWorkerClarityApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        loadDatabase();
        Parent root = FXMLLoader.load(getClass().getResource("entries/EntriesScreen.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void loadDatabase() throws SQLException {
        Database.createCategoriesTable();
        Database.createEntriesTable();
        Database.createTasksTable();
        Database.createDailyLearningTable();
    }

}
