package pkg223071132_project;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    @FXML
    private ImageView Logo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateAuthButtons();
    }

    private void updateAuthButtons() {
        if (LoginHOme != null && logout != null) {
            boolean isLoggedIn = UserSession.isLoggedIn();
            LoginHOme.setVisible(!isLoggedIn);
            logout.setVisible(isLoggedIn);
        }
    }

    
    public void setLoggedIn(boolean loggedIn, int userId, String role) {
        if (loggedIn) {
            UserSession.login(userId, role);  
        } else {
            UserSession.logout();
        }
        updateAuthButtons();
    }

    @FXML
    private void HandleHome(ActionEvent event) {
        // TODO: Add navigation if needed
    }

    @FXML
    private void HandleProducts(ActionEvent event) {
        // TODO: Add navigation if needed
    }

    @FXML
    private void HandleCart(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cart.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) CartID.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Your Shopping Cart");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load cart page.");
        }
    }

    @FXML
    private void HandleContact(ActionEvent event) {
        // TODO: Add contact page navigation if needed
    }

    @FXML
    public void HandleLoginHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load Login page.");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        setLoggedIn(false, -1, "");  // Pass empty string for role on logout
        showAlert("Logged Out", "You have been successfully logged out.");
    }

    @FXML
    private void handleAddToCart(ActionEvent event) {
        if (!UserSession.isLoggedIn()) {
            showAlert("Login Required", "Please log in to add items to the cart.");
            return;
        }

        Button clickedButton = (Button) event.getSource();
        VBox productCard = (VBox) clickedButton.getParent();

        String name = "";
        double price = 0.0;
        String imagePath = "";

        for (javafx.scene.Node node : productCard.getChildren()) {
            if (node instanceof Label label) {
                String text = label.getText();
                if (text.matches(".*\\d+\\s*BDT.*")) {
                    price = parsePrice(text);
                } else {
                    name = text;
                }
            } else if (node instanceof ImageView iv) {
                if (iv.getImage() != null && iv.getImage().getUrl() != null) {
                    imagePath = iv.getImage().getUrl();
                }
            }
        }

        Cart.addItem(new Product(name, price, imagePath));
        showAlert("Success", name + " added to cart!");
    }

    private double parsePrice(String text) {
        try {
            return Double.parseDouble(text.replaceAll("[^\\d.]", ""));
        } catch (Exception e) {
            return 0.0;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
