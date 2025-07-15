package pkg223071132_project;

import java.io.InputStream;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.ResourceBundle;

public class CartItemCellController implements Initializable {

    @FXML
    private ImageView productImage;

    @FXML
    private Label productName;

    @FXML
    private Label productPrice;

    @FXML
    private Label productQuantity;

    @FXML
    private Button increaseBtn;

    @FXML
    private Button decreaseBtn;

    @FXML
    private Button removeButton;

    private Product product;
    private ListView<Product> parentListView;
    private Runnable updateTotalsCallback;

    private int quantity;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // No initialization needed here for now
    }

    public void setData(Product product, ListView<Product> parentListView, Runnable updateTotalsCallback) {
    this.product = product;
    this.parentListView = parentListView;
    this.updateTotalsCallback = updateTotalsCallback;

    // Initialize quantity from product if stored, else default to 1
    this.quantity = product.getQuantity() > 0 ? product.getQuantity() : 1;

    productName.setText(product.getName());
    productQuantity.setText(String.valueOf(quantity));
    updatePriceLabel();

    // Load image from resources folder correctly
       String imageFileName = product.getImagePath(); // e.g. "Braclet 1.jpg"
String resourcePath = "/pkg223071132_project/images/" + imageFileName.replace(" ", "%20");

InputStream imageStream = getClass().getResourceAsStream(resourcePath);

if (imageStream == null) {
    System.err.println("Image not found: " + resourcePath);
    productImage.setImage(null);
} else {
    productImage.setImage(new Image(imageStream));
}
    increaseBtn.setOnAction(e -> {
        quantity++;
        product.setQuantity(quantity);
        productQuantity.setText(String.valueOf(quantity));
        updatePriceLabel();
        if (updateTotalsCallback != null) updateTotalsCallback.run();
    });

    decreaseBtn.setOnAction(e -> {
        if (quantity > 1) {
            quantity--;
            product.setQuantity(quantity);
            productQuantity.setText(String.valueOf(quantity));
            updatePriceLabel();
            if (updateTotalsCallback != null) updateTotalsCallback.run();
        }
    });

    removeButton.setOnAction(e -> {
        Cart.removeItem(product);
        parentListView.getItems().remove(product);
        if (updateTotalsCallback != null) updateTotalsCallback.run();
    });
}

private void updatePriceLabel() {
    double totalPrice = product.getPrice() * quantity;
    productPrice.setText(String.format("%.2f BDT", totalPrice));
}


}
