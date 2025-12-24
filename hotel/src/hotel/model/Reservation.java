package hotel.model;

import hotel.model.Customer;
import java.util.*;

public class Reservation {

    // 🔹 الحقول الإضافية من شغلك
    private int bookingId;
    private int individuals;

    // 🔹 الحقول الأساسية من الأصلي
    private List<String> requests;
    private String reservationID;
    private Customer customer;
    private Room room;
    private Date startDate;
    private Date endDate;
    private String status;
    private Date createDate;
    private double totalPrice;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    // ✅ Constructor كامل (تحميل من DB)
    public Reservation(int bookingId, String firstName, String lastName,
                       String phone, String startDate, String endDate,
                       int individuals) {
        this.bookingId = bookingId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.individuals = individuals;

        try {
            this.startDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            this.endDate   = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        } catch (Exception e) {
            this.startDate = new Date();
            this.endDate   = new Date();
        }
    }

    // ✅ Constructor إضافي (إنشاء حجز جديد قبل إدخال ID)
    public Reservation(String firstName, String lastName,
                       String phone, String startDate, String endDate,
                       int individuals) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.individuals = individuals;

        try {
            this.startDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            this.endDate   = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        } catch (Exception e) {
            this.startDate = new Date();
            this.endDate   = new Date();
        }
    }

    // ✅ Constructor الأصلي (مع Customer و Room)
    public Reservation(Customer customer, Room room, Date startDate, Date endDate) {
        this.reservationID = "RES-" + System.currentTimeMillis();
        this.customer = customer;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "ACTIVE";
        this.createDate = new Date();
        this.totalPrice = calculatePrice();
        this.requests = new ArrayList<>();
    }

    // ✅ Getters & Setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getIndividuals() { return individuals; }
    public void setIndividuals(int individuals) { this.individuals = individuals; }

    public String getReservationID() { return reservationID; }
    public void setReservationID(String reservationID) { this.reservationID = reservationID; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<String> getRequests() { return requests; }
    public void setRequests(List<String> requests) { this.requests = requests; }

    // ✅ حساب السعر (من الأصلي)
    private double calculatePrice() {
        if (room == null || customer == null) return 0;
        long no_nights = getNumberOfNights();
        double price = room.getPrice() * no_nights;
        double discount = price * customer.getDiscount();
        return price - discount;
    }

    private long getNumberOfNights() {
        long time = Math.abs(endDate.getTime() - startDate.getTime());
        return time / (24 * 60 * 60 * 1000);
    }

    public boolean reservationOccupied() {
        Date today = new Date();
        return !today.before(startDate) && !today.after(endDate);
    }

    public boolean reserveInFuture() {
        return startDate.after(new Date());
    }

    public void updateReservationDate(Date newStartDate, Date newEndDate) {
        this.startDate = newStartDate;
        this.endDate = newEndDate;
        this.totalPrice = calculatePrice();
    }

    public boolean overlaps(Date otherStart, Date otherEnd) {
        return !(endDate.before(otherStart) || startDate.after(otherEnd));
    }

    private String formatDate(Date date) {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    // ✅ مفيد جدًا للـ Debug أو ComboBox
    @Override
    public String toString() {
        return "Booking #" + bookingId + " - " + firstName + " " + lastName;
    }

    public boolean ReservationOccupied() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean ReserveInFuture() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Object getId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}