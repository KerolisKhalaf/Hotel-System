
package hotel.payment;
import java.util.UUID;
import java.util.Date;

public class Payment {

    private String paymentID;
    private String reservationID;
    private double amount;
    private String paymentMethod;
    private String status;
    private Date paymentDate;
    
    public Payment(String reservationID, double amount, String paymentMethod) {
        this.paymentID = "PAY-" + UUID.randomUUID().toString().substring(0, 8);
        this.reservationID = reservationID;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = "PENDING";
        this.paymentDate = new Date();
    }
    
    public String getPaymentID() { return paymentID; }
    public String getReservationID() { return reservationID; }
    public double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }
    public Date getPaymentDate() { return paymentDate; }
    
    public void setStatus(String status) { this.status = status; }

}
