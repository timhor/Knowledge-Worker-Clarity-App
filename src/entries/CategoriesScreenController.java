package entries;

import helper.Database;
import helper.PageSwitchHelper;
import layout.LayoutScreenController;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class CategoriesScreenController {

    PageSwitchHelper pageSwitchHelper = new PageSwitchHelper();

    LayoutScreenController layoutController = new LayoutScreenController();

    private String selectedCategoryId = "";

    @FXML
    private TextField categoryNameTextField;

    @FXML
    private ColorPicker categoryColourPicker;

    @FXML
    private Button saveCategoryButton;

    @FXML
    private TableView<Category> categoryList;

    @FXML
    private TableColumn<Category, String> categoryColumn;

    @FXML
    private TableColumn<Category, String> colourColumn;

    // Navigation
    // Side bar
    
    @FXML
    public Button categoriesButton;
    
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
    
    @FXML
    public Button dailyLearningScreenButton;

    // Top Bar
    @FXML
    public Button entriesScreenButton;

    @FXML
    public Button tasksScreenButton;

    @FXML
    public Button aboutScreenButton;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().getCategoryNameProperty());
        colourColumn.setCellValueFactory(cellData -> cellData.getValue().getHexStringProperty());

        // custom row colur adapted from: https://stackoverflow.com/a/56309916
        // deselect rows adapted from: https://stackoverflow.com/a/30194680
        categoryList.setRowFactory(new Callback<TableView<Category>, TableRow<Category>>() {
            @Override
            public TableRow<Category> call(TableView<Category> tableView2) {
                TableRow<Category> row = new TableRow<Category>() {
                    @Override
                    protected void updateItem(Category item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || item.getHexString() == null) {
                            setStyle("");
                        } else {
                            setStyle("-fx-background-color: " + item.getHexString() + ";");
                        }
                    }
                };
                row.setOnMouseClicked(event -> {
                    if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                        saveCategoryButton.setText("Update Category");
                        Category selectedCategory = row.getItem();
                        selectedCategoryId = selectedCategory.getId();

                        categoryNameTextField.setText(selectedCategory.getCategoryName());
                        categoryColourPicker.setValue(Color.web(selectedCategory.getHexString()));
                    } else {
                        categoryList.getSelectionModel().clearSelection();
                        clearInputFields();
                        saveCategoryButton.setText("New Category");
                        selectedCategoryId = "";
                    }
                });
                return row;
            }
        });

        populateCategories();
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        if (selectedCategoryId.length() > 0) {
            try {
                Database.updateFromPreparedStatement("DELETE FROM categories WHERE id = ?",
                        new String[] { selectedCategoryId });
                // ON DELETE SET NULL didn't work for some reason...
                Database.updateFromPreparedStatement("UPDATE entries SET category = NULL WHERE category = ?",
                        new String[] { selectedCategoryId });
                populateCategories();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                clearInputFields();
            }
        }
    }

    @FXML
    private void handleSaveCategoryButtonAction(ActionEvent event) {
        statusLabel.setVisible(false);

        String categoryName = categoryNameTextField.getText();
        if (categoryName.length() == 0) {
            statusLabel.setVisible(true);
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Category name cannot be empty");
            return;
        }

        // colour conversion adapted from: https://stackoverflow.com/a/18803814
        Color colourValue = categoryColourPicker.getValue();
        String hexString = String.format("#%02X%02X%02X", (int) (colourValue.getRed() * 255),
                (int) (colourValue.getGreen() * 255), (int) (colourValue.getBlue() * 255));

        try {
            if (selectedCategoryId.length() > 0) {
                Database.updateFromPreparedStatement(
                        "UPDATE categories SET categoryname = ?, hexString = ? WHERE id = ?",
                        new String[] { categoryName, hexString, selectedCategoryId });
            } else {
                Database.updateFromPreparedStatement(
                        "INSERT INTO categories (categoryname, hexString) VALUES (?,?)",
                        new String[] { categoryName, hexString });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            populateCategories();
            clearInputFields();
        }
    }

    private void populateCategories() {
        try {
            categoryList.setItems(getCategoryListData());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ObservableList<Category> getCategoryListData() throws SQLException {
        ArrayList<Category> categoryList = new ArrayList<Category>();
        ResultSet rs = Database.getResultSet("SELECT * FROM categories");
        while (rs.next()) {
            Category category = new Category(rs.getString("id"), rs.getString("categoryname"), rs.getString("hexString"));
            categoryList.add(category);
        }
        return FXCollections.observableList(categoryList);
    }

    private void clearInputFields() {
        categoryNameTextField.setText("");
        categoryColourPicker.setValue(Color.web("#FFF"));
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

    // Add Data Handling
    
    @FXML
    public void handleCategoriesScreenButtonAction(ActionEvent event) throws IOException {
        layoutController.handleCategoriesScreenButtonAction(event);
    }
    
    @FXML
    public void handleMyLifeScreenButtonAction(ActionEvent event) throws IOException {
        layoutController.handleMyLifeScreenButtonAction(event);
    }

    @FXML
    public void handleMyDayScreenButtonAction(ActionEvent event) throws IOException {
        layoutController.handleMyDayScreenButtonAction(event);
    }

    @FXML
    public void handleMyWeekScreenButtonAction(ActionEvent event) throws IOException {
        layoutController.handleMyWeekScreenButtonAction(event);
    }

    @FXML
    public void handleWeeklyTrendsScreenButtonAction(ActionEvent event) throws IOException {
        layoutController.handleWeeklyTrendsScreenButtonAction(event);
    }
    
    @FXML
    public void handleDailyLearningScreenButtonAction(ActionEvent event) throws IOException {
        layoutController.handleDailyLearningScreenButtonAction(event);
    }

}
