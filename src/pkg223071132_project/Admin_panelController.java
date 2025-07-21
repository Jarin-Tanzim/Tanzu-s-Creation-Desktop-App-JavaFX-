/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package pkg223071132_project;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Apollo Gadget
 */
public class Admin_panelController implements Initializable {

    @FXML
    private Button adminLogout;
    @FXML
    private Button ViewUserButton;
    @FXML
    private Button AddProductsButton;
    @FXML
    private Button ViewCardButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML

private void handleAdminLogout(ActionEvent event) {
    try {
        
        UserSession.logout();

        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
        Parent root = loader.load();

        HomepageController controller = loader.getController();
        controller.setLoggedIn(false, -1, "");

       
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.setTitle("Home Page");
        newStage.show();

       
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
}


    @FXML
    private void HandleUsers(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view_users.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("User List");
        stage.setScene(new Scene(root));
        stage.show();
        
        
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    @FXML
private void HandleAddProducts(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add_product.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Add New Product");
        stage.setScene(new Scene(root));
        stage.show();

        

    } catch (IOException e) {
        e.printStackTrace();
    }
}

    @FXML
    private void handleVieworders(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view_orders.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("View Orders");
        stage.setScene(new Scene(root));
        stage.show();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    
}
