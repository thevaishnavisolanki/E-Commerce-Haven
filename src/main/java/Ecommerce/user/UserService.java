package Ecommerce.user;

import Ecommerce.db.CreateDBConnection;

import java.sql.*;

public class UserService {
    CreateDBConnection callforconnection;
    Connection myconnection;

    public UserService() {
        try
        {
            callforconnection=new CreateDBConnection();
            myconnection=callforconnection.getConnection();

        }
        catch (Exception ee)
        {
            System.out.println("Getting connection error");
        }
    }


    public boolean registerUser(User u1) {
        int rowAffected = 0;
        try {
            PreparedStatement pst = myconnection.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)");
            pst.setString(1, u1.getUsername());
            pst.setString(2, u1.getPassword());
            pst.setString(3, u1.getEmail());
            rowAffected = pst.executeUpdate();
        } catch (SQLException ff) {
            System.out.println("Insertion Error" + ff);
        }
        return rowAffected > 0;
    }

    public boolean authenticate(String providedUsername, String providedPassword) {
        User user = authenticateUser(providedUsername, providedPassword);
        return user != null && user.getUsername().equals(providedUsername) && user.getPassword().equals(providedPassword);
    }

    public User authenticateUser(String username, String password) {
        try {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pst = myconnection.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String email = resultSet.getString("email");
                return new User(userId, username, password, email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
