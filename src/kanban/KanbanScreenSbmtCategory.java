
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jeffr
 */
public class KanbanScreenSbmtCategory {
    @FXML
    private Button SbmtCategoryButton;
    @FXML
    private TextField categoryName; 
    
    Database database; 
    
    @FXML
    private void SbmtCategoryAction(ActionEvent event){
       try{
         ResultSet rs = database.getResultSetFromPreparedStatement(
        "SELECT * FROM categoryName",
        new String[]{categoryName.getText()}
    );
         JOptionPane.showMessageDialog(null,"Data Saved!");
                 }catch(Exception e){
    }
    };
        
        
       /** String categoryName = txt_categoryName.getText();
        try{
            Database.insertStatement(categoryName);
            JOptionPane.showMessageDialog(null,"Data Saved!");
        }
        catch(Exception e){
            
        }**/
        
        
    }
    
}
