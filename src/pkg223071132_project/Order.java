package pkg223071132_project;

public class Order {
    private int id;
    private int userId;
    private int productId;
    private int quantity;
    private double totalPrice;

    public Order(int id, int userId, int productId, int quantity, double totalPrice) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public double getTotalPrice() { return totalPrice; }
}
