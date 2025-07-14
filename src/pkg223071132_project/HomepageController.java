package pkg223071132_project;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import pkg223071132_project.CartController;
import pkg223071132_project.CartItem;

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
    @FXML
    private Button AddtoCart;

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

    @FXML
private void HandleAddtoCart(ActionEvent event) {
    if (!isLoggedIn) {
        Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Login Required");
    alert.setHeaderText(null);
    alert.setContentText("Please log in to add items to your cart.");
    alert.showAndWait();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return; // Stop further execution
    }

    
    try {
        Node clickedButton = (Node) event.getSource();
        VBox productCard = (VBox) clickedButton.getParent();

        Label nameLabel = (Label) productCard.getChildren().get(1);
        Label priceLabel = (Label) productCard.getChildren().get(2);
        ImageView imageView = (ImageView) productCard.getChildren().get(0);

        String name = nameLabel.getText();
        String priceText = priceLabel.getText().replace("BDT", "").trim();
        double price = Double.parseDouble(priceText);
        String imageUrl = imageView.getImage().getUrl();

        CartItem newItem = new CartItem(name, 1, price, imageUrl);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("cart.fxml"));
        Parent root = loader.load();

        CartController cartController = loader.getController();
        cartController.addToCart(newItem);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    } catch (Exception e) {
        e.printStackTrace();
    }
}


   }

