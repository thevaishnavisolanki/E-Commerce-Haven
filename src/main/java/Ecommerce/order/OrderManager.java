package Ecommerce.order;

import Ecommerce.db.CreateDBConnection;
import Ecommerce.product.Product;
import Ecommerce.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderManager {
    private CreateDBConnection dbConnection;
    private Connection connection;

    public OrderManager() {
        dbConnection = new CreateDBConnection();
        connection = dbConnection.getConnection();
    }

    public void displayOrderHistory(int userId) {
        System.out.println("Order History for User ID: " + userId);
        // Implement your logic here to display order history for the given user ID
    }

    public void placeOrder(User user, List<Product> products) {
        try {
            String insertOrderQuery = "INSERT INTO orders (user_id, created_at) VALUES (?, NOW())";
            PreparedStatement pst = connection.prepareStatement(insertOrderQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, user.getUserId());
            pst.executeUpdate();

            // Get the generated order ID
            ResultSet rs = pst.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            String insertOrderDetailsQuery = "INSERT INTO order_details (order_id, product_id) VALUES (?, ?)";
            PreparedStatement pstOrderDetails = connection.prepareStatement(insertOrderDetailsQuery);
            pstOrderDetails.setInt(1, orderId);
            for (Product product : products) {
                pstOrderDetails.setInt(2, product.getProductId());
                pstOrderDetails.executeUpdate();
            }

            System.out.println("Order placed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
