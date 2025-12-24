package hotel.payment;

import hotel.model.Customer;
import hotel.model.Room;
import hotel.model.Reservation;

import hotel.services.CustomerService;
import hotel.services.ReservationService;
import hotel.services.RoomService;
import hotel.services.PaymentService;

import hotel.payment.PaymentProcessor;
import hotel.payment.PaymentStrategy;
import hotel.payment.CreditCardPayment; // ✅ خليهم كلاس مستقل أو public داخل PaymentStrategy
import hotel.payment.CashPayment;
import hotel.payment.PaymentProcessor;

import java.util.List;

public class HotelFacade {

    private final CustomerService customerService;
    private final ReservationService reservationService;
    private final RoomService roomService;
    private final PaymentService paymentService;

    public HotelFacade() {
        this.customerService = new CustomerService();
        this.reservationService = new ReservationService();
        this.roomService = new RoomService();
        this.paymentService = new PaymentService();
    }

    // ----------------------------------------------------
    // ✅ Login
    // ----------------------------------------------------
    public Customer login(String username, String password) {
        if (username == null || password == null ||
            username.isEmpty() || password.isEmpty()) {
            return null;
        }
        return customerService.login(username, password);
    }

    // ----------------------------------------------------
    // ✅ Sign-Up
    // ----------------------------------------------------
    public boolean signUp(Customer customer) {
        if (customer == null) return false;
        return customerService.signUp(customer);
    }

    public boolean emailExists(String email) {
        return customerService.emailExists(email);
    }

    // ----------------------------------------------------
    // ✅ Users
    // ----------------------------------------------------
    public List<Customer> getAllUsers() {
        return customerService.getAllUsers();
    }

    public boolean deleteUser(int id) {
        if (id <= 0) return false;
        return customerService.deleteUser(id);
    }

    public boolean updateUser(int id, String fname, String lname, String phone, String email) {
        if (id <= 0) return false;
        return customerService.updateUser(id, fname, lname, phone, email);
    }

    // ----------------------------------------------------
    // ✅ Rooms
    // ----------------------------------------------------
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    public boolean addRoom(Room room) {
        if (room == null) return false;
        return roomService.addRoom(room);
    }

    public boolean updateRoom(Room room) {
        if (room == null || room.getRoomId() <= 0) return false;
        return roomService.updateRoom(room);
    }

    public boolean deleteRoom(int roomId) {
        if (roomId <= 0) return false;
        return roomService.deleteRoom(roomId);
    }

    public List<Room> searchRoom(String type) {
        if (type == null || type.isEmpty()) return getAllRooms();
        return roomService.searchRoom(type);
    }

    public boolean updateRoomStatus(int roomId, String status) {
        return roomService.updateRoomStatus(roomId, status);
    }

    // ----------------------------------------------------
    // ✅ Reservations
    // ----------------------------------------------------
    public boolean createReservation(String firstName, String lastName, String email,
                                     String phone, String startDate, String endDate,
                                     String creditCard, String creditPass, int individuals) {
        if (firstName == null || lastName == null || email == null || phone == null ||
            startDate == null || endDate == null || creditCard == null || creditPass == null ||
            individuals <= 0) {
            return false;
        }

        return reservationService.createReservation(
                firstName, lastName, email, phone,
                startDate, endDate, creditCard, creditPass, individuals
        );
    }

    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    public boolean deleteReservation(int id) {
        if (id <= 0) return false;
        return reservationService.deleteReservation(id);
    }

    public boolean updateReservation(int id, String fname, String lname, String phone,
                                     String sdate, String edate, int individuals) {
        if (id <= 0) return false;
        return reservationService.updateReservation(id, fname, lname, phone, sdate, edate, individuals);
    }

    public int countBookingsByDate(String date) {
        if (date == null || date.isEmpty()) return 0;
        return reservationService.countBookingsByDate(date);
    }

    // ----------------------------------------------------
    // ✅ Payment (Basic)
    // ----------------------------------------------------
    public boolean processPayment(String creditCard, String password, double amount) {
        if (creditCard == null || password == null || amount <= 0) return false;
        return paymentService.processPayment(creditCard, password, amount);
    }

    // ----------------------------------------------------
    // ✅ Payment with Strategy (CreditCard / Cash)
    // ----------------------------------------------------
    public boolean processPaymentStrategy(String reservationID, double amount, String method) {
        if (reservationID == null || reservationID.isEmpty() || amount <= 0 || method == null) return false;

        PaymentProcessor processor = PaymentProcessor.getInstance();

        if (method.equalsIgnoreCase("Credit Card")) {
            processor.setPaymentStrategy(new CreditCardPayment());
        } else if (method.equalsIgnoreCase("Cash")) {
            processor.setPaymentStrategy(new CashPayment());
        } else {
            return false;
        }

        return processor.processPayment(reservationID, amount, method);
    }

    // ----------------------------------------------------
    // ✅ Individuals (مكملين)
    // ----------------------------------------------------
    public int getMaxIndividuals() {
        return reservationService.getMaxIndividuals();
    }

    public int getMinIndividuals() {
        return reservationService.getMinIndividuals();
    }

    public boolean updateReservation(int booking_id, String fname, String lname, String phone, String sdate, String edate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean createReservation(String fname, String lname, String email, String phone, String sdate, String edate, String creditno, String creditpass) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}