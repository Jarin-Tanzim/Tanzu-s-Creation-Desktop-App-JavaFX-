package pkg223071132_project;

public class Product {
    private int id;
    private String name;
    private double price;
    private String description;
    private String imageFileName;
    private int quantity;

    
    public Product(String name, double price, String imageFileName) {
    this.name = name;
    this.price = price;
    this.imageFileName = imageFileName;
    this.quantity = 1;
}

    
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageFileName() { return imageFileName; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) {
        if (quantity < 1) quantity = 1;
        this.quantity = quantity;
    }

    
    public String getFullImagePath() {
        return "/pkg223071132_project/images/" + imageFileName;
    }

    public double getTotalPrice() {
        return price * quantity;
    }
}
