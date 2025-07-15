package pkg223071132_project;

public class Product {

    private String name;
    private double price;
    private String imagePath;
    private int quantity;

    public Product(String name, double price, String imagePath) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.quantity = 1; // default quantity
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for price
    public double getPrice() {
        return price;
    }

    // Getter for imagePath
    public String getImagePath() {
        return imagePath;
    }

    // Getter and setter for quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 1) quantity = 1; // minimum 1
        this.quantity = quantity;
    }

    // Calculate total price = price * quantity
    public double getTotalPrice() {
        return price * quantity;
    }
}
