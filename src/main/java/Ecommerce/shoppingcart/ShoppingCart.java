package Ecommerce.shoppingcart;

import java.sql.ResultSet;
import java.util.ArrayList;
import Ecommerce.db.CreateDBConnection;
import Ecommerce.product.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ShoppingCart {
    private List<Product> items;
    private CreateDBConnection dbConnection;
    private Connection connection;

    public ShoppingCart() {
        dbConnection = new CreateDBConnection();
        connection = dbConnection.getConnection();
        items = new ArrayList<>();

    }

    public void addToCart(Product product, int userId) {
        try {
            if (productExists(product.getId())) {
                String productName = product.getName().trim();
                if (productExists(product.getId())) {
                    String query = "INSERT INTO shopping_cart (user_id, product_id) VALUES (?, ?)";
                    PreparedStatement pst = connection.prepareStatement(query);
                    pst.setInt(1, userId);
                    pst.setInt(2, product.getId());
                    pst.executeUpdate();
                    items.add(product);
                    System.out.println(product.getName() + " added to cart.");
                } else {
                    System.out.println("Product does not exist in the catalog.");
                }
            } else {
                System.out.println("Product does not exist in the catalog.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding product to the cart: " + e.getMessage());
        }
    }


    private boolean productExists(int productId) throws SQLException {
        String query = "SELECT COUNT(*) FROM products WHERE product_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, productId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }


    public void removeItem(Product product, int userId) {
        try {
            String query = "DELETE FROM shopping_cart WHERE user_id = ? AND product_id = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, userId);
            pst.setInt(2, product.getId());
            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                items.remove(product);
                System.out.println(product.getName() + " removed from cart.");
            } else {
                System.out.println(product.getName() + " is not in the cart.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearCart(int userId) {
        try {
            String query = "DELETE FROM shopping_cart WHERE user_id = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, userId);
            pst.executeUpdate();
            items.clear();
            System.out.println("Shopping cart cleared.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public double calculateTotalPrice() {
        double totalPrice = 0;
        for (Product item : items) {
            totalPrice += item.getPrice();
        }
        return totalPrice;
    }
    public void displayCart(int userId) {
        if (items.isEmpty()) {
            System.out.println("Shopping cart is empty.");
        } else {
            System.out.println("Shopping Cart:");
            for (int i = 0; i < items.size(); i++) {
                System.out.println((i + 1) + ". " + items.get(i).getName());
            }
            System.out.println("Total Price: $" + calculateTotalPrice());
        }
    }

    public Product getProductByName(String productName) {
        for (Product product : items) {
            if (product.getName().equalsIgnoreCase(productName)) {
                return product;
            }
        }
        return null; // Product not found
    }

    public List<Product> getItems() {
        return items;
    }

}
