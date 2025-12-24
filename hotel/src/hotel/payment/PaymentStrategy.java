
package hotel.payment;

import hotel.payment.Payment;

public interface PaymentStrategy {
    boolean validatePayment(Payment payment);
    boolean processPayment(Payment payment);
}

class CreditCardPayment implements PaymentStrategy {
    @Override
    public boolean validatePayment(Payment payment) {
        return payment.getAmount() > 0;
    }
    
    @Override
    public boolean processPayment(Payment payment) {
        System.out.println("Processing Credit Card: $" + payment.getAmount());
        payment.setStatus("COMPLETED");
        return true;
    }
}

class CashPayment implements PaymentStrategy {
    @Override
    public boolean validatePayment(Payment payment) {
        return payment.getAmount() > 0;
    }
    
    @Override
    public boolean processPayment(Payment payment) {
        System.out.println("Processing Cash: $" + payment.getAmount());
        payment.setStatus("COMPLETED");
        return true;
    }
}
