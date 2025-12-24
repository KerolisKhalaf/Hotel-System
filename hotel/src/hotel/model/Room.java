package hotel.model;

import java.util.*;

public class Room {

    // 🔹 الحقول من كودك
    private int roomId;

    // 🔹 الحقول من الكود الأصلي
    private String roomNumber;
    private String roomType;
    private int capacity;
    private double price;
    private String status;

    // ✅ Constructor الأصلي
    public Room(String roomNumber, String type, int capacity, double price) {
        this.roomNumber = roomNumber;
        this.roomType = type;
        this.capacity = capacity;
        this.price = price;
        this.status = "AVAILABLE";
    }

    // ✅ Constructor كامل (تحميل من DB) من كودك
    public Room(int roomId, String type, int capacity, double price, String status) {
        this.roomId = roomId;
        this.roomType = type;
        this.capacity = capacity;
        this.price = price;
        this.status = status;
        this.roomNumber = String.valueOf(roomId); // ربط الـ roomNumber بالـ roomId
    }

    // ✅ Constructor للإضافة (ID لسه مش موجود) من كودك
    public Room(String type, int capacity, double price, String status) {
        this.roomType = type;
        this.capacity = capacity;
        this.price = price;
        this.status = status;
        this.roomNumber = "0"; // رقم مؤقت
    }

    // ✅ Getters & Setters من النسختين
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getType() { return roomType; }
    public void setType(String type) { this.roomType = type; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // ✅ toString من النسختين
    @Override
    public String toString() {
        return "Room " + (roomId > 0 ? roomId : roomNumber) +
               " (" + roomType + ") - $" + price + "/night";
    }
}