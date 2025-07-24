package pkg223071132_project;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.*;

public class edit_productController implements Initializable {

    @FXML
    private Label idLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField imageField;

    private Product selectedProduct;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code if needed
    }

    public void setProduct(Product product) {
        this.selectedProduct = product;
        if (product != null) {
            idLabel.setText(String.valueOf(product.getId()));
            nameField.setText(product.getName());
            priceField.setText(String.valueOf(product.getPrice()));
            imageField.setText(product.getImageFileName());
        }
    }

    @FXML
private void handleSave(ActionEvent event) {
    String name = nameField.getText();
    String priceText = priceField.getText();
    String image = imageField.getText();

    try {
        double price = Double.parseDouble(priceText);

        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Tanzu_app", "root", "1234");
        String sql = "UPDATE products SET name = ?, price = ?, image_filename = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);
        stmt.setDouble(2, price);
        stmt.setString(3, image);
        stmt.setInt(4, selectedProduct.getId());

        int rows = stmt.executeUpdate();
        if (rows > 0) {
            System.out.println("Product updated successfully.");
        }

        stmt.close();
        conn.close();

        // Close the edit window
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();

    } catch (NumberFormatException e) {
        System.err.println("Invalid price format.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}
