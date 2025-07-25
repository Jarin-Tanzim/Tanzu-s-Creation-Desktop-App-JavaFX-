package pkg223071132_project;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class view_productsController implements Initializable {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> idCol;
    @FXML private TableColumn<Product, String> nameCol;
    @FXML private TableColumn<Product, Double> priceCol;
    @FXML private TableColumn<Product, String> imageCol;
    @FXML private TableColumn<Product, Void> actionCol;

    private final ObservableList<Product> productList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("imageFileName"));

        loadProductsFromDatabase();
        addActionButtons();
    }

    private void loadProductsFromDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/tanzu_app", "root", "1234");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {

            while (rs.next()) {
                Product product = new Product(
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getString("image_filename")
                );

                java.lang.reflect.Field idField = Product.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(product, rs.getInt("id"));

                productList.add(product);
            }

            productTable.setItems(productList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addActionButtons() {
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");

            {
                editBtn.setOnAction(e -> {
                    Product p = getTableView().getItems().get(getIndex());
                    editProduct(p);
                });

                deleteBtn.setOnAction(e -> {
                    Product p = getTableView().getItems().get(getIndex());
                    deleteProduct(p);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, editBtn, deleteBtn);
                    setGraphic(buttons);
                }
            }
        });
    }

   private void editProduct(Product product) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("edit_product.fxml"));
        Parent root = loader.load();

        
        edit_productController controller = loader.getController();
        controller.setProduct(product);

        Stage stage = new Stage();
        stage.setTitle("Edit Product");
        stage.setScene(new Scene(root));
        stage.show();

       

    } catch (IOException e) {
        e.printStackTrace();
    }
}


    private void deleteProduct(Product product) {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/tanzu_app", "root", "1234");
         PreparedStatement ps = conn.prepareStatement("DELETE FROM products WHERE id = ?")) {

        ps.setInt(1, product.getId());
        int rows = ps.executeUpdate();
        if (rows > 0) {
            productList.remove(product);
            System.out.println("Product deleted: " + product.getName());

            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deleted");
            alert.setHeaderText(null);
            alert.setContentText("Product \"" + product.getName() + "\" deleted successfully!");
            alert.showAndWait();
        }

    } catch (SQLException e) {
        e.printStackTrace();

        
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Deletion Failed");
        alert.setHeaderText(null);
        alert.setContentText("An error occurred while deleting the product.");
        alert.showAndWait();
    }
}


    @FXML
private void handleBackToAdmin(ActionEvent event) {
    try {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_panel.fxml"));
        Parent root = loader.load();

        
        Stage stage = new Stage();
        stage.setTitle("Admin Panel");
        stage.setScene(new Scene(root));
        stage.show();

       
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}

   

