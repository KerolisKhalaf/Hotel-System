
package hotel.factory;

import hotel.model.Room;

public class RoomFactory {
    public static Room createRoom(String roomType, String roomNumber) {
        if (roomType.equalsIgnoreCase("STANDARD")) {
            return new Room(roomNumber, "STANDARD", 2, 100.0);
        } 
        else if (roomType.equalsIgnoreCase("DELUXE")) {
            return new Room(roomNumber, "DELUXE", 3, 150.0);
        } 
        else if (roomType.equalsIgnoreCase("SUITE")) {
            return new Room(roomNumber, "SUITE", 4, 250.0);
        }
        return null;
    }
}
