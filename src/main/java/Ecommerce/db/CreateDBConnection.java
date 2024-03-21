package Ecommerce.db;

import java.sql.*;


public class CreateDBConnection {

    Connection con;
    public Connection getConnection()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommerce","root","root");
        }
        catch (Exception e1) {
            System.out.println("Connection Error " + e1);
        }
        return con;


    }

    public static void main(String[] args) {
        CreateDBConnection b1=new CreateDBConnection();
        b1.getConnection();
    }

}
