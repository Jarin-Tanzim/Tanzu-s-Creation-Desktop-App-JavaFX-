package pkg223071132_project;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class HomepageController implements Initializable {
    @FXML
    private Button logout;
    @FXML
    private Button Home;
    @FXML
    private Button ProductsID;
    @FXML
    private Button CartID;
    @FXML
    private Button Contact;
    @FXML
    private Button LoginHOme;

    private boolean isLoggedIn = false;
    @FXML
    private ImageView Logo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateAuthButtons();
    }

    private void updateAuthButtons() {
        if (LoginHOme != null && logout != null) {
            LoginHOme.setVisible(!isLoggedIn);
            logout.setVisible(isLoggedIn);
        }
    }

    public void setLoggedIn(boolean loggedIn) {
        this.isLoggedIn = loggedIn;
        updateAuthButtons();
    }

    @FXML
    private void HandleHome(ActionEvent event) {
        
    }

    @FXML
    private void HandleProducts(ActionEvent event) {
        
    }

    @FXML
    private void HandleCart(ActionEvent event) {
        
    }

    @FXML
    private void HandleContact(ActionEvent event) {
        // Navigation logic to contact page
    }

    @FXML
    private void HandleLoginHome(ActionEvent event) {
        
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("Login.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        
        setLoggedIn(false);
       
    }
}
