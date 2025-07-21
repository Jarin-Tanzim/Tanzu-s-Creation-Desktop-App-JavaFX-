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

public class View_ordersController implements Initializable {

    @FXML
    private TableView<Order> orderTable;
    @FXML
    private TableColumn<Order, Integer> orderIdColumn;
    @FXML
    private TableColumn<Order, Integer> userColumn;
    @FXML
    private TableColumn<Order, Integer> productColumn;
    @FXML
    private TableColumn<Order, Integer> quantityColumn;
    @FXML
    private TableColumn<Order, Double> totalPriceColumn;

    private final ObservableList<Order> orderList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadOrders();
    }

    private void loadOrders() {
        orderList.clear();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tanzu_app", "root", "1234");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM orders")) {

            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("total_price")
                );
                orderList.add(order);
            }

            orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            userColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
            productColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

            orderTable.setItems(orderList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToAdmin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("admin_panel.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
