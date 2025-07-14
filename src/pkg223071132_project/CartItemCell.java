package pkg223071132_project;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

import java.io.IOException;

public class CartItemCell extends ListCell<CartItem> {
    private HBox root;
    private ImageView productImage;
    private Label productName;
    private Label productQuantity;
    private Label productPrice;
    private Button removeButton;
    private Button increaseBtn;
    private Button decreaseBtn;

    private CartItem currentItem;
    private CartController cartController;

    public CartItemCell(CartController controller) {
        this.cartController = controller;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CartItemCell.fxml"));
            root = loader.load();

            productImage = (ImageView) root.lookup("#productImage");
            productName = (Label) root.lookup("#productName");
            productQuantity = (Label) root.lookup("#productQuantity");
            productPrice = (Label) root.lookup("#productPrice");
            removeButton = (Button) root.lookup("#removeButton");
            increaseBtn = (Button) root.lookup("#increaseBtn");
            decreaseBtn = (Button) root.lookup("#decreaseBtn");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(CartItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
            currentItem = null;
        } else {
            currentItem = item;

            productImage.setImage(new Image(item.getImageUrl()));
            productName.setText(item.getName());
            productQuantity.setText(String.valueOf(item.getQuantity()));
            productPrice.setText(String.format("$%.2f", item.getTotalPrice()));

           
            removeButton.setOnAction(e -> {
                getListView().getItems().remove(item);
                cartController.updateSummary(); 
            });

            
            increaseBtn.setOnAction(e -> {
                item.setQuantity(item.getQuantity() + 1);
                updateQuantityAndPrice();
                cartController.updateSummary(); 
            });

            
            decreaseBtn.setOnAction(e -> {
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    updateQuantityAndPrice();
                    cartController.updateSummary(); 
                }
            });

            setGraphic(root);
        }
    }

    private void updateQuantityAndPrice() {
        if (currentItem != null) {
            productQuantity.setText(String.valueOf(currentItem.getQuantity()));
            productPrice.setText(String.format("$%.2f", currentItem.getTotalPrice()));
        }
    }
}
