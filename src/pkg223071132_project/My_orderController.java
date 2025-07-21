package pkg223071132_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.scene.image.ImageView;

public class My_orderController implements Initializable {

    @FXML private TableView<OrderView> orderTable;
@FXML private TableColumn<OrderView, ImageView> imageColumn;
@FXML private TableColumn<OrderView, String> productNameColumn;
@FXML private TableColumn<OrderView, Integer> quantityColumn;
@FXML private TableColumn<OrderView, Double> totalPriceColumn;

private ObservableList<OrderView> orderList = FXCollections.observableArrayList();

@Override
public void initialize(URL url, ResourceBundle rb) {
    loadOrdersForCurrentUser();
}

private void loadOrdersForCurrentUser() {
    orderList.clear();
    int userId = UserSession.getLoggedInUserId();
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tanzu_app", "root", "1234");
         PreparedStatement stmt = conn.prepareStatement(
             "SELECT o.quantity, o.total_price, p.name, p.image_filename " +
             "FROM orders o JOIN products p ON o.product_id = p.id WHERE o.user_id = ?"
         )) {
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String name = rs.getString("name");
            String imageFilename = rs.getString("image_filename");
            String imagePath = "/pkg223071132_project/images/" + imageFilename; // adjust path if needed
            int qty = rs.getInt("quantity");
            double price = rs.getDouble("total_price");
            orderList.add(new OrderView(imagePath, name, qty, price));
        }
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("productImage"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        orderTable.setItems(orderList);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
