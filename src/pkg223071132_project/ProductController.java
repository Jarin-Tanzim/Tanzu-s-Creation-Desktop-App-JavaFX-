package pkg223071132_project;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProductController implements Initializable {

    @FXML
    private Button logout;
    @FXML
    private Button Home;
    @FXML
    private Button CartID;
    @FXML
    private Button LoginHOme;
    @FXML
    private TilePane productTilePane;
    @FXML
    private ImageView Logo;
    @FXML
    private Button Myorders;
    

    @Override
public void initialize(URL url, ResourceBundle rb) {
    updateAuthButtons();
    loadDefaultProducts();
    loadProductsFromDatabase(); 
   
}

private void loadDefaultProducts() {
    addProductToPane("Braclet", 350, "Braclet 4.jpg");
    addProductToPane("Earring", 350, "Ear 7.jpg");
    addProductToPane("Earring", 350, "Ear 12.jpg");
    addProductToPane("Earring", 350, "Ear 1.jpg");
    addProductToPane("Dress", 980, "dress.png");
    addProductToPane("Braclet", 350, "Braclet 1.jpg");
    addProductToPane("Earring", 350, "Ear 9.jpg");
    addProductToPane("Braclet", 350, "braclet 2.jpg");
    addProductToPane("Braclet", 350, "braclet 1.jpg");
    addProductToPane("Earring", 350, "Ear 7.jpg");
    addProductToPane("Earring", 350, "Ear 12.jpg");
    addProductToPane("Braclet", 350, "Ear 12.jpg");
}
private void loadProductsFromDatabase() {
    try {
       
        java.sql.Connection conn = java.sql.DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/tanzu_app", "root", "1234"
        );

        String sql = "SELECT name, price, image_filename FROM products";
        java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
        java.sql.ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String name = rs.getString("name");
            double price = rs.getDouble("price");
            String imageFile = rs.getString("image_filename"); 
            addProductToPane(name, price, imageFile);
        }

        rs.close();
        stmt.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
        showAlert("DB Error", "Could not load products from database.");
    }
}

private void addProductToPane(String name, double price, String imageFile) {
    VBox productCard = new VBox(6);
    productCard.getStyleClass().add("product-card");
    productCard.setMinWidth(165);
    productCard.setPrefHeight(141);
    productCard.setPrefWidth(165);
    productCard.setAlignment(Pos.TOP_CENTER);

   
    URL imgUrl = getClass().getResource("/pkg223071132_project/images/" + imageFile);
    ImageView imgView;
    if (imgUrl != null) {
        imgView = new ImageView(imgUrl.toExternalForm());
    } else {
       
        imgView = new ImageView();
    }
    imgView.setFitHeight(70);
    imgView.setFitWidth(200);
    imgView.setPreserveRatio(true);
    imgView.setPickOnBounds(true);

    Label nameLabel = new Label(name);
    nameLabel.getStyleClass().add("product-name");

    Label priceLabel = new Label(String.format("%.0f BDT", price));
    priceLabel.getStyleClass().add("product-price");

    Button addToCartBtn = new Button("Add to Cart");
    addToCartBtn.getStyleClass().add("add-to-cart-button");
    addToCartBtn.setOnAction(e -> {
    if (!UserSession.isLoggedIn()) {
        showAlert("Login Required", "Please log in to add items to the cart.");
        return;
    }
    Cart.addItem(new Product(name, price, imageFile)); // ✅ Only filename
    showAlert("Success", name + " added to cart!");
});


    productCard.getChildren().addAll(imgView, nameLabel, priceLabel, addToCartBtn);
    productTilePane.getChildren().add(productCard);
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
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) Home.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Homepage");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load home page.");
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

    @FXML
    private void handleLogout(ActionEvent event) {
        setLoggedIn(false, -1, "");  // Pass empty string for role on logout
        showAlert("Logged Out", "You have been successfully logged out.");
    }

private void handleAddToCart(ActionEvent event) {
    if (!UserSession.isLoggedIn()) {
        showAlert("Login Required", "Please log in to add items to the cart.");
        return;
    }

    Button clickedButton = (Button) event.getSource();
    VBox productCard = (VBox) clickedButton.getParent();

    String name = "";
    double price = 0.0;
    String imageFileName = "";

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
                String fullPath = iv.getImage().getUrl();
                imageFileName = fullPath.substring(fullPath.lastIndexOf('/') + 1);
            }
        }
    }

   
    System.out.println("Image file name stored: " + imageFileName);

    Cart.addItem(new Product(name, price, imageFileName));
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
