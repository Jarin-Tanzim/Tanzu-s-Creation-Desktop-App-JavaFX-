package pkg223071132_project;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CartController implements Initializable {

    @FXML
    private ListView<Product> Cartlistview;

    @FXML
    private Button continueShoppingBtn;

    @FXML
    private Label totalItemsLabel;

    @FXML
    private Label totalPricelable;

    @FXML
    private Button CheckoutButton;

    private ObservableList<Product> items;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        items = FXCollections.observableArrayList(Cart.getItems());
        Cartlistview.setItems(items);

        Cartlistview.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("CartItemCell.fxml"));
                        Parent root = loader.load();

                        CartItemCellController controller = loader.getController();
                        controller.setData(item, Cartlistview, () -> updateTotals());

                        setGraphic(root);
                    } catch (IOException e) {
                        e.printStackTrace();
                        setGraphic(null);
                    }
                }
            }
        });

        updateTotals();
    }

    private void updateTotals() {
    int totalItems = Cart.getItems().stream()
                         .mapToInt(Product::getQuantity)
                         .sum();

    double totalPrice = Cart.getItems().stream()
    .mapToDouble(Product::getTotalPrice)
    .sum();

    totalItemsLabel.setText("Total items: " + totalItems);
    totalPricelable.setText("Total price: " + String.format("%.2f BDT", totalPrice));
}


    @FXML
    private void HandleContinueShop(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
        Parent root = loader.load();
        
        Stage stage = (Stage) continueShoppingBtn.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Home");
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        
    }
    }

    @FXML
private void HandleCheckout(ActionEvent event) {
    if (!UserSession.isLoggedIn()) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Not Logged In");
        alert.setHeaderText(null);
        alert.setContentText("You must be logged in to proceed to checkout.");
        alert.showAndWait();
        return;
    }

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("checkout.fxml"));
        Parent root = loader.load();

        
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.setTitle("Checkout");
        currentStage.setScene(new Scene(root));
        currentStage.show();

    } catch (IOException e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Failed to load checkout page.");
        alert.showAndWait();
    }
}

}
