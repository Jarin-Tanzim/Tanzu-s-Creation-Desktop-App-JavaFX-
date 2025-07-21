package pkg223071132_project;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Add_productController {

    @FXML private TextField productNameField;
    @FXML private TextField productPriceField;
    @FXML private TextField productImageField; 
    @FXML private TextArea productDescField;

    @FXML
    private void handleAddProduct() {
        String name = productNameField.getText().trim();
        String priceStr = productPriceField.getText().trim();
        String imageFilename = productImageField.getText().trim(); 
        String desc = productDescField.getText().trim();

        if (name.isEmpty() || priceStr.isEmpty() || imageFilename.isEmpty() || desc.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Data", "Please fill out all product fields.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Price", "Please enter a valid price.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tanzu_app", "root", "1234")) {
            String sql = "INSERT INTO products (name, price, image_filename, description) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setString(3, imageFilename);  // Save as filename
            stmt.setString(4, desc);
            stmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Product added successfully!");

// Load product list view
try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("product.fxml"));
    Parent root = loader.load();
    Stage stage = (Stage) productNameField.getScene().getWindow();
    stage.setScene(new Scene(root));
} catch (Exception e) {
    e.printStackTrace();
    showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load product page.");
}

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add product.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
