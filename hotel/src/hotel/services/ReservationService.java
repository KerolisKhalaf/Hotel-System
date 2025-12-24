package hotel.services;

import hotel.model.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {

    private final String url =
        "jdbc:sqlserver://localhost\\MSSQLSERVER14;databaseName=Hotel;user=sa;password=123456;";

    public ReservationService() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            System.err.println("❌ SQL Driver not found: " + ex.getMessage());
        }
    }

    // ----------------------------------------------------
    // ✅ Create Reservation
    // ----------------------------------------------------
    public boolean createReservation(String firstName, String lastName, String email,
                                     String phone, String startDate, String endDate,
                                     String creditCard, String creditPass, int individuals) {

        if (!validateReservation(firstName, lastName, email, phone, startDate, endDate, individuals)) {
            System.err.println("❌ Reservation validation failed");
            return false;
        }

        String sql = "INSERT INTO Booking_info (fname, lname, email, phone, sdate, edate, creditcard, creditpass, individual_no) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, startDate);
            ps.setString(6, endDate);
            ps.setString(7, creditCard);
            ps.setString(8, creditPass);
            ps.setInt(9, individuals);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("❌ CreateReservation Error: " + ex.getMessage());
            return false;
        }
    }

    // ----------------------------------------------------
    // ✅ Get All Reservations
    // ----------------------------------------------------
    public List<Reservation> getAllReservations() {

        List<Reservation> list = new ArrayList<>();

        String sql = "SELECT booking_id, fname, lname, phone, sdate, edate, individual_no FROM Booking_info";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Reservation r = new Reservation(
                        rs.getInt("booking_id"),
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getString("phone"),
                        rs.getString("sdate"),
                        rs.getString("edate"),
                        rs.getInt("individual_no")
                );
                list.add(r);
            }

        } catch (SQLException ex) {
            System.err.println("❌ GetAllReservations Error: " + ex.getMessage());
        }

        return list;
    }

    // ----------------------------------------------------
    // ✅ Delete Reservation
    // ----------------------------------------------------
    public boolean deleteReservation(int id) {

        if (id <= 0) return false;

        String sql = "DELETE FROM Booking_info WHERE booking_id = ?";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("❌ DeleteReservation Error: " + ex.getMessage());
            return false;
        }
    }

    // ----------------------------------------------------
    // ✅ Update Reservation
    // ----------------------------------------------------
    public boolean updateReservation(int id, String fname, String lname, String phone,
                                     String sdate, String edate, int individuals) {

        if (id <= 0) return false;

        String sql = "UPDATE Booking_info SET fname=?, lname=?, phone=?, sdate=?, edate=?, individual_no=? "
                   + "WHERE booking_id=?";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, fname);
            ps.setString(2, lname);
            ps.setString(3, phone);
            ps.setString(4, sdate);
            ps.setString(5, edate);
            ps.setInt(6, individuals);
            ps.setInt(7, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("❌ UpdateReservation Error: " + ex.getMessage());
            return false;
        }
    }

    // ----------------------------------------------------
    // ✅ Count Bookings by Date
    // ----------------------------------------------------
    public int countBookingsByDate(String date) {

        if (date == null || date.isEmpty()) return 0;

        String sql = "SELECT COUNT(*) AS booking_count FROM Booking_info WHERE sdate = ?";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, date);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("booking_count");
            }

        } catch (SQLException ex) {
            System.err.println("❌ CountBookings Error: " + ex.getMessage());
        }

        return 0;
    }

    // ----------------------------------------------------
    // ✅ Max Individuals
    // ----------------------------------------------------
    public int getMaxIndividuals() {

        String sql = "SELECT MAX(individual_no) AS max_individual_no FROM Booking_info";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("max_individual_no");
            }

        } catch (SQLException ex) {
            System.err.println("❌ MaxIndividuals Error: " + ex.getMessage());
        }

        return 0;
    }

    // ----------------------------------------------------
    // ✅ Min Individuals
    // ----------------------------------------------------
    public int getMinIndividuals() {

        String sql = "SELECT MIN(individual_no) AS min_individual_no FROM Booking_info";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("min_individual_no");
            }

        } catch (SQLException ex) {
            System.err.println("❌ MinIndividuals Error: " + ex.getMessage());
        }

        return 0;
    }

    // ----------------------------------------------------
    // ✅ Validation Helper
    // ----------------------------------------------------
    private boolean validateReservation(String fname, String lname, String email,
                                        String phone, String sdate, String edate,
                                        int individuals) {

        return fname != null && !fname.isEmpty() &&
               lname != null && !lname.isEmpty() &&
               email != null && !email.isEmpty() &&
               phone != null && !phone.isEmpty() &&
               sdate != null && !sdate.isEmpty() &&
               edate != null && !edate.isEmpty() &&
               individuals > 0;
    }

    public boolean createReservation(String firstName, String lastName, String email, String phone, String startDate, String endDate, String creditCard, String creditPass) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean updateReservation(int id, String fname, String lname, String phone, String sdate, String edate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}