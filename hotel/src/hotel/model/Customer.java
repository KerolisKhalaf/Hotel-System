package hotel.model;

import java.util.*;

public class Customer {

    // 🔹 الحقول الأساسية من الأصلي
    private String customerID;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String customerType;
    private double discount;

    // 🔹 الحقول الإضافية من شغلك
    private int id;
    private String username;
    private String password;
    private int age;

    // ✅ Constructor الأصلي (Booking)
    public Customer(String firstName, String lastName, String email, String phone, String customerType) {
        this.customerID = "C-" + System.currentTimeMillis();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.customerType = customerType;

        if (customerType != null) {
            if (customerType.equalsIgnoreCase("VIP")) {
                this.discount = 0.15;
            } else if (customerType.equalsIgnoreCase("CORPORATE")) {
                this.discount = 0.10;
            } else {
                this.discount = 0.0;
            }
        } else {
            this.discount = 0.0;
        }
    }

    // ✅ Constructor كامل (Login)
    public Customer(int id, String firstName, String lastName,
                    String username, String password, int age,
                    String phone, String email, String customerType) {
        this(firstName, lastName, email, phone, customerType); // استدعاء الأصلي
        this.id = id;
        this.username = username;
        this.password = password;
        this.age = age;
    }

    // ✅ Constructor لصفحة Staff
    public Customer(int id, String firstName, String lastName,
                    String phone, String email, String customerType) {
        this(firstName, lastName, email, phone, customerType);
        this.id = id;
    }

    // ✅ Constructor لعملية SignUp
    public Customer(String firstName, String lastName,
                    String username, String password, int age,
                    String phone, String email, String customerType) {
        this(firstName, lastName, email, phone, customerType);
        this.username = username;
        this.password = password;
        this.age = age;
    }

    // ✅ Constructor فاضي (مظبوط بدل UnsupportedOperationException)
    public Customer() {
        this.customerID = "C-" + System.currentTimeMillis();
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.phone = "";
        this.customerType = "REGULAR";
        this.discount = 0.0;
        this.id = 0;
        this.username = "";
        this.password = "";
        this.age = 0;
    }

    // ✅ Getters & Setters
    public String getCustomerID() { return customerID; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCustomerType() { return customerType; }
    public void setCustomerType(String customerType) { this.customerType = customerType; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public String getFullName() { return firstName + " " + lastName; }

    @Override
    public String toString() {
        return getFullName() + " (ID: " + id + ", Type: " + customerType + ")";
    }
}