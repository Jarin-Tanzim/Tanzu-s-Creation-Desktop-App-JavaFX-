package pkg223071132_project;

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

public class CheckoutController implements Initializable {

    @FXML private Label nameLabel;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> paymentMethodCombo;
    @FXML private Label totalPriceLabel;

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

           
            showAlert(Alert.AlertType.INFORMATION, "Order Confirmed", "Your order has been placed successfully!");

            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) addressField.getScene().getWindow(); // get current window
            stage.setScene(new Scene(root));
            stage.setTitle("Home");
            stage.show();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not complete the order.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load homepage.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
