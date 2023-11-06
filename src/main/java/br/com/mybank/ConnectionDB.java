package br.com.mybank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mybank_jdbc?user=root&password=root");

            System.out.println("Connection on: " + connection.getCatalog());

            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
