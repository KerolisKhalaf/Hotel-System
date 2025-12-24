package hotel.services;

import hotel.model.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomService {

    private final String url =
        "jdbc:sqlserver://localhost\\MSSQLSERVER14;databaseName=Hotel;user=sa;password=123456;";

    public RoomService() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            System.err.println("❌ SQL Driver not found: " + ex.getMessage());
        }
    }

    // ----------------------------------------------------
    // ✅ Get ALL Rooms
    // ----------------------------------------------------
    public List<Room> getAllRooms() {

        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT roomId, type, capacity, price, status FROM Rooms";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Room r = new Room(
                        rs.getInt("roomId"),
                        rs.getString("type"),
                        rs.getInt("capacity"),
                        rs.getDouble("price"),
                        rs.getString("status")
                );
                rooms.add(r);
            }

        } catch (SQLException ex) {
            System.err.println("❌ GetAllRooms Error: " + ex.getMessage());
        }

        return rooms;
    }

    // ----------------------------------------------------
    // ✅ Add Room
    // ----------------------------------------------------
    public boolean addRoom(Room room) {

        if (!validateRoom(room)) {
            System.err.println("❌ Room validation failed");
            return false;
        }

        String sql = "INSERT INTO Rooms (type, capacity, price, status) VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, room.getType());
            ps.setInt(2, room.getCapacity());
            ps.setDouble(3, room.getPrice());
            ps.setString(4, room.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("❌ AddRoom Error: " + ex.getMessage());
            return false;
        }
    }

    // ----------------------------------------------------
    // ✅ Update Room
    // ----------------------------------------------------
    public boolean updateRoom(Room room) {

        if (room == null || room.getRoomId() <= 0) {
            System.err.println("❌ Invalid room ID");
            return false;
        }

        if (!validateRoom(room)) {
            System.err.println("❌ Room validation failed");
            return false;
        }

        String sql = "UPDATE Rooms SET type=?, capacity=?, price=?, status=? WHERE roomId=?";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, room.getType());
            ps.setInt(2, room.getCapacity());
            ps.setDouble(3, room.getPrice());
            ps.setString(4, room.getStatus());
            ps.setInt(5, room.getRoomId());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("❌ UpdateRoom Error: " + ex.getMessage());
            return false;
        }
    }
    public boolean updateRoomStatus(int roomId, String status) {
    String sql = "UPDATE Rooms SET status=? WHERE roomId=?";
    try (Connection con = DriverManager.getConnection(url);
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, status);
        ps.setInt(2, roomId);

        return ps.executeUpdate() > 0;

    } catch (SQLException ex) {
        System.err.println("❌ UpdateRoomStatus Error: " + ex.getMessage());
        return false;
    }
}

    // ----------------------------------------------------
    // ✅ Delete Room
    // ----------------------------------------------------
    public boolean deleteRoom(int roomId) {

        if (roomId <= 0) {
            System.err.println("❌ Invalid room ID");
            return false;
        }

        String sql = "DELETE FROM Rooms WHERE roomId=?";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("❌ DeleteRoom Error: " + ex.getMessage());
            return false;
        }
    }

    // ----------------------------------------------------
    // ✅ Search Room by Type
    // ----------------------------------------------------
    public List<Room> searchRoom(String type) {

        List<Room> rooms = new ArrayList<>();

        if (type == null || type.trim().isEmpty()) {
            return getAllRooms(); // ✅ لو مفيش Type رجّع كل الغرف
        }

        String sql = "SELECT roomId, type, capacity, price, status FROM Rooms WHERE type LIKE ?";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + type + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Room r = new Room(
                        rs.getInt("roomId"),
                        rs.getString("type"),
                        rs.getInt("capacity"),
                        rs.getDouble("price"),
                        rs.getString("status")
                );
                rooms.add(r);
            }

        } catch (SQLException ex) {
            System.err.println("❌ SearchRoom Error: " + ex.getMessage());
        }

        return rooms;
    }

    // ----------------------------------------------------
    // ✅ Validation Helper
    // ----------------------------------------------------
    private boolean validateRoom(Room room) {

        return room != null &&
               room.getType() != null && !room.getType().isEmpty() &&
               room.getCapacity() > 0 &&
               room.getPrice() > 0 &&
               room.getStatus() != null && !room.getStatus().isEmpty();
    }
}