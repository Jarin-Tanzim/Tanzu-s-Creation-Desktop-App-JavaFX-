package pkg223071132_project;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class OrderView {
    private ImageView productImage;
    private String productName;
    private int quantity;
    private double totalPrice;

    public OrderView(String imagePath, String productName, int quantity, double totalPrice) {
        this.productImage = new ImageView(new Image(imagePath, 70, 70, true, true));
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public ImageView getProductImage() { return productImage; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getTotalPrice() { return totalPrice; }
}
