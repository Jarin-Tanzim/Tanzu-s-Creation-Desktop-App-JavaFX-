package pkg223071132_project;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomepageController implements Initializable {

    private Button logout;
    @FXML
    private Button Home;
    @FXML
    private Button ProductsID;
    @FXML
    private Button CartID;
    @FXML
    private Button LoginHOme;
    @FXML
    private ImageView Logo;
    @FXML
    private Region spacer;
    @FXML
    private Button Myorders;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateAuthButtons();
    }

    private void updateAuthButtons() {
    if (LoginHOme != null) {
        boolean isLoggedIn = UserSession.isLoggedIn();
        if (isLoggedIn) {
            LoginHOme.setText("Logout");
            LoginHOme.setOnAction(this::handleLogout);
        } else {
            LoginHOme.setText("Login");
            LoginHOme.setOnAction(this::HandleLoginHome);
        }
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
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("product.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ProductsID.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Products");
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        showAlert("Error", "Failed to load products page.");
    }
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

public void handleLogout(ActionEvent event) {
    setLoggedIn(false, -1, "");  // Clear session
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
        String url = iv.getImage().getUrl();
       
        imagePath = url.substring(url.lastIndexOf('/') + 1);
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

    @FXML
private void Handlemyorders(ActionEvent event) {
    if (!UserSession.isLoggedIn()) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Not Logged In");
        alert.setHeaderText(null);
        alert.setContentText("Please log in to view your orders.");
        alert.showAndWait();
        return;
    }
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("my_order.fxml")); // or "my_order.fxml" if that's your filename
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("My Orders");
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Could not load the orders page.");
        alert.showAndWait();
    }
}

}
