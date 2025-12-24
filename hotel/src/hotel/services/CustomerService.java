package hotel.services;

import hotel.model.Customer; // ← استخدم Customer الموحد بعد الدمج
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    private final String url =
        "jdbc:sqlserver://localhost\\MSSQLSERVER14;databaseName=Hotel;user=sa;password=123456;";

    public CustomerService() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            System.err.println("❌ SQL Driver not found: " + ex.getMessage());
        }
    }

    // ----------------------------------------------------
    // ✅ Login
    // ----------------------------------------------------
    public Customer login(String username, String password) {

        if (username == null || password == null ||
            username.isEmpty() || password.isEmpty()) {
            return null;
        }

        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.trim());
            ps.setString(2, password.trim());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Customer(
                        rs.getInt("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("age"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        "REGULAR" // ← افتراضي، ممكن يتعدل حسب نوع العميل
                );
            }

        } catch (SQLException ex) {
            System.err.println("❌ Login Error: " + ex.getMessage());
        }

        return null;
    }

    // ----------------------------------------------------
    // ✅ Sign-Up
    // ----------------------------------------------------
    public boolean signUp(Customer customer) {

        if (customer == null) return false;

        String sql = "INSERT INTO Users (firstname, lastname, username, password, age, phone, email) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getUsername());
            ps.setString(4, customer.getPassword());
            ps.setInt(5, customer.getAge());
            ps.setString(6, customer.getPhone());
            ps.setString(7, customer.getEmail());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("❌ SignUp Error: " + ex.getMessage());
            return false;
        }
    }

    // ✅ Check if email exists
    public boolean emailExists(String email) {

        String sql = "SELECT id FROM Users WHERE email = ?";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim());

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException ex) {
            System.err.println("❌ EmailExists Error: " + ex.getMessage());
            return false;
        }
    }

    // ----------------------------------------------------
    // ✅ Get All Users (Staff Page)
    // ----------------------------------------------------
    public List<Customer> getAllUsers() {

        List<Customer> list = new ArrayList<>();

        String sql = "SELECT id, firstname, lastname, phone, email FROM Users";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Customer c = new Customer(
                        rs.getInt("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        null,        // username مش محتاجينه هنا
                        null,        // password مش محتاجينه هنا
                        0,           // age مش محتاجينه هنا
                        rs.getString("phone"),
                        rs.getString("email"),
                        "REGULAR"    // افتراضي
                );
                list.add(c);
            }

        } catch (SQLException ex) {
            System.err.println("❌ GetAllUsers Error: " + ex.getMessage());
        }

        return list;
    }

    // ----------------------------------------------------
    // ✅ Delete User
    // ----------------------------------------------------
    public boolean deleteUser(int id) {

        if (id <= 0) return false;

        String sql = "DELETE FROM Users WHERE id = ?";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("❌ DeleteUser Error: " + ex.getMessage());
            return false;
        }
    }

    // ----------------------------------------------------
    // ✅ Update User
    // ----------------------------------------------------
    public boolean updateUser(int id, String fname, String lname, String phone, String email) {

        if (id <= 0) return false;

        String sql = "UPDATE Users SET firstname=?, lastname=?, phone=?, email=? WHERE id=?";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, fname);
            ps.setString(2, lname);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setInt(5, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("❌ UpdateUser Error: " + ex.getMessage());
            return false;
        }
    }
}

