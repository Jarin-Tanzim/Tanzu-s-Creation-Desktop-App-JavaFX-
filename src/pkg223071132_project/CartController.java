/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package pkg223071132_project;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

public class CartController {

    @FXML
    private ListView<CartItem> cartListView;

    @FXML
    private Label totalItemsLabel;       
    @FXML
    private Label totalItemsValueLabel; 

    @FXML
    private Label totalPriceLabel;       
    @FXML
    private Label totalPriceValueLabel;  

    @FXML
    private Button checkoutButton;

    @FXML
    private Button continueShoppingBtn;

    private Button returnBtn;

    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();

    public void initialize() {
        
        cartListView.setItems(cartItems);

        
        cartListView.setCellFactory(param -> new CartItemCell(this));

        
        updateSummary();

       
        continueShoppingBtn.setOnAction(this::HandleContinueShop);
      
    }

    
    public void addToCart(CartItem item) {
        cartItems.add(item);
        updateSummary();
    }

    
    public void updateSummary() {
        int totalItems = cartItems.stream()
                                  .mapToInt(CartItem::getQuantity)
                                  .sum();
        double totalPrice = cartItems.stream()
                                     .mapToDouble(CartItem::getTotalPrice)
                                     .sum();

        totalItemsValueLabel.setText(String.valueOf(totalItems));
        totalPriceValueLabel.setText(String.format("$%.2f", totalPrice));
    }

   
    @FXML
    private void HandleContinueShop(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("homepage.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
