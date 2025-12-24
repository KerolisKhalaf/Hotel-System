package hotel.services;

public class PaymentService {

    // ✅ Fake balance (محاكاة حساب بنكي)
    private double fakeBalance = 5000.0;

    // ✅ Fake stored credit card + password (محاكاة بنك)
    private final String validCard = "1234567890123456";
    private final String validPass = "4321";

    public PaymentService() {}

    // ----------------------------------------------------
    // ✅ Process Payment (محاكاة دفع حقيقية)
    // ----------------------------------------------------
    public boolean processPayment(String creditCard, String password, double amount) {

        // ✅ Basic validation
        if (!isValidCard(creditCard)) {
            System.err.println("❌ Invalid credit card number");
            return false;
        }

        if (!isValidPassword(password)) {
            System.err.println("❌ Invalid card password");
            return false;
        }

        if (amount <= 0) {
            System.err.println("❌ Invalid payment amount");
            return false;
        }

        // ✅ Check if card matches our fake bank
        if (!creditCard.equals(validCard) || !password.equals(validPass)) {
            System.err.println("❌ Card authentication failed");
            return false;
        }

        // ✅ Check balance
        if (amount > fakeBalance) {
            System.err.println("❌ Insufficient balance");
            return false;
        }

        // ✅ Deduct amount
        fakeBalance -= amount;

        System.out.println("✅ Payment successful! Remaining balance: " + fakeBalance);
        return true;
    }

    // ----------------------------------------------------
    // ✅ Validation Helpers
    // ----------------------------------------------------
    private boolean isValidCard(String card) {
        return card != null && card.trim().length() >= 8;
    }

    private boolean isValidPassword(String pass) {
        return pass != null && pass.trim().length() >= 4;
    }

    // ----------------------------------------------------
    // ✅ Getter for balance (ممكن تستخدمينه في UI لو حبيتي)
    // ----------------------------------------------------
    public double getBalance() {
        return fakeBalance;
    }
}