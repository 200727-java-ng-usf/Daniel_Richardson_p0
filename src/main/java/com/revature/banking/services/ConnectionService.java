package com.revature.banking.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//singleton
public class ConnectionService{
    private static ConnectionService connFactory = new ConnectionService();

    private ConnectionService() {
        super();
    }

    public static ConnectionService getInstance() {
        return connFactory;
    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            // Force the JVM to load the PostGreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://dbrev.ctha7md0f2cd.us-west-1.rds.amazonaws.com:5432/postgres",
                    "drichardson513",
                    "fakepassword");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if (conn == null) {
            throw new RuntimeException("Failed to establish connection.");
        }
        return conn;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

}
