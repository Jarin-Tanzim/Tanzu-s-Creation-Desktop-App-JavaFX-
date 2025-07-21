package pkg223071132_project;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class CheckoutController implements Initializable {

    @FXML private Label nameLabel;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> paymentMethodCombo;
    @FXML private Label totalPriceLabel;
    @FXML
    private Button checkoutConfirmButton;
    @FXML
    private Hyperlink backhome;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadUserDetails();
        loadTotalPrice();
        loadPaymentMethods();
    }

    private void loadUserDetails() {
        int userId = UserSession.getLoggedInUserId();

        if (userId == -1) {
            showAlert(Alert.AlertType.ERROR, "User Not Logged In", "Please log in first.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tanzu_app", "root", "1234")) {

            String query = "SELECT full_name, address, phone FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nameLabel.setText(rs.getString("full_name"));
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                addressField.setText(address != null ? address : "");
                phoneField.setText(phone != null ? phone : "");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not load user information.");
        }
    }

    private void loadTotalPrice() {
        double totalPrice = Cart.getItems().stream()
                .mapToDouble(Product::getTotalPrice)
                .sum();
        totalPriceLabel.setText(String.format("%.2f BDT", totalPrice));
    }

    private void loadPaymentMethods() {
        paymentMethodCombo.setItems(FXCollections.observableArrayList(
                "Cash on Delivery",
                "Credit/Debit Card",
                "bKash",
                "Nagad"
        ));
    }

    @FXML
private void handleConfirmOrder() {
    String address = addressField.getText().trim();
    String phone = phoneField.getText().trim();
    String paymentMethod = paymentMethodCombo.getValue();
    int userId = UserSession.getLoggedInUserId();

    if (address.isEmpty() || phone.isEmpty() || paymentMethod == null) {
        showAlert(Alert.AlertType.WARNING, "Missing Info", "Please fill in all fields.");
        return;
    }

    try (Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/tanzu_app", "root", "1234")) {

       
        String updateUserSql = "UPDATE users SET address = ?, phone = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(updateUserSql);
        stmt.setString(1, address);
        stmt.setString(2, phone);
        stmt.setInt(3, userId);
        stmt.executeUpdate();

        
        String insertOrderSql = "INSERT INTO orders (user_id, product_id, quantity, total_price) VALUES (?, ?, ?, ?)";
        PreparedStatement orderStmt = conn.prepareStatement(insertOrderSql);

        boolean orderSuccess = true;
        for (Product product : Cart.getItems()) {
            int productId = getProductIdByName(conn, product.getName());
            if (productId == -1) {
                showAlert(Alert.AlertType.ERROR, "Order Error", "Product ID not found for: " + product.getName());
                orderSuccess = false;
                break;
            }
            orderStmt.setInt(1, userId);
            orderStmt.setInt(2, productId);
            orderStmt.setInt(3, product.getQuantity());
            orderStmt.setDouble(4, product.getTotalPrice());
            orderStmt.executeUpdate();
        }

        if (orderSuccess) {
            showAlert(Alert.AlertType.INFORMATION, "Order Confirmed", "Your order has been placed successfully!");
            Cart.clearCart();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addressField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home");
            stage.show();
        }

    } catch (SQLException e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Database Error", "Could not complete the order.");
    } catch (Exception e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load homepage.");
    }
}


private int getProductIdByName(Connection conn, String name) {
    try (PreparedStatement stmt = conn.prepareStatement("SELECT id FROM products WHERE name = ? LIMIT 1")) {
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return rs.getInt("id");
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1;
}

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handlebacktohome(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Home");
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Unable to load Home page.");
        alert.showAndWait();
    }
    }
}
