package pkg223071132_project;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static final List<Product> items = new ArrayList<>();

    public static void addItem(Product product) {
        items.add(product);
    }

    public static void removeItem(Product product) {
        items.remove(product);
    }

    public static List<Product> getItems() {
        return new ArrayList<>(items); 
    }

    public static int getTotalItems() {
        return items.size();
    }

    public static double getTotalPrice() {
        return items.stream()
                    .mapToDouble(Product::getPrice)
                    .sum();
    }

    public static void clearCart() {
        items.clear();
    }

    public static boolean contains(Product product) {
        return items.contains(product);
    }
}
