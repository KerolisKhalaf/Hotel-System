
package hotel.payment;
import hotel.core.DatabaseConnection;
import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import hotel.payment.Payment;

public class PaymentProcessor  {
    
    private static final PaymentProcessor Pprocessor = new PaymentProcessor();
    private List<Payment> payments;  
    private PaymentStrategy paymentStrategy;
    private DatabaseConnection dbConnection;
    
    private PaymentProcessor() {
        this.payments = new ArrayList<>();
        this.dbConnection = DatabaseConnection.getInstance();
        System.out.println("PaymentProcessor initialized");
    }
    
    public static PaymentProcessor getInstance() {
        return Pprocessor;
    }
    
    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }
    
   
    public boolean processPayment(String reservationID, double amount, String paymentMethod) {
        
        System.out.println("Amount: $" + amount);
        
        if(paymentStrategy == null) {
            System.out.println("Error: Payment strategy not set");
            return false;
        }
        
        Payment payment = new Payment(reservationID, amount, paymentMethod);
        
        if(!paymentStrategy.validatePayment(payment)) {
            payment.setStatus("FAILED");
            payments.add(payment);  
            System.out.println("Payment validation failed");
            return false;
        }
        
        if(!paymentStrategy.processPayment(payment)) {
            payment.setStatus("FAILED");
            payments.add(payment); 
            System.out.println("Payment processing failed");
            return false;
        }
        
        payment.setStatus("COMPLETED");
        payments.add(payment);
        
        System.out.println("Payment processed successfully!");
        System.out.println("Amount: $" + amount);
        System.out.println("Method: " + paymentMethod);
        System.out.println("Status: COMPLETED\n");
        
        return true;
    }
    
   
    public List<Payment> getPaymentHistory(String reservationID) {
        List<Payment> history = new ArrayList<>();
        for(Payment p : payments) {
            if(p.getReservationID().equals(reservationID)) {
                history.add(p);
            }
        }
        return history;
    }
   
    public List<Payment> getAllPayments() {
        return new ArrayList<>(payments);
    }
  
    public double getTotalPaymentAmount(String reservationID) {
        double total = 0;
        for(Payment p : getPaymentHistory(reservationID)) {
            if(p.getStatus().equals("COMPLETED")) {
                total += p.getAmount();
            }
        }
        return total;
    }
}