
package hotel.core;

import java.sql.*;

public class DatabaseConnection {
    
    private static DatabaseConnection instance;
    
    private Connection connection;
    
    private static final String DB_URL = "jdbc:sqlserver://localhost\\MSSQLSERVER14;databaseName=Hotel;";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "123456";
    
    private DatabaseConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("✓ JDBC Driver loaded");
            
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✓ Database connected successfully");
            
        } catch (ClassNotFoundException ex) {
            System.out.println("❌ JDBC Driver not found: " + ex.getMessage());
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("❌ Database connection failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("✓ Connection reconnected");
            }
        } catch (SQLException ex) {
            System.out.println("❌ Connection error: " + ex.getMessage());
        }
        
        return this.connection;
    }
    
    public boolean isConnected() {
        try {
            return this.connection != null && !this.connection.isClosed();
        } catch (SQLException ex) {
            return false;
        }
    }
    
    public void closeConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
                System.out.println("✓ Connection closed");
            }
        } catch (SQLException ex) {
            System.out.println("❌ Error closing connection: " + ex.getMessage());
        }
    }
}
