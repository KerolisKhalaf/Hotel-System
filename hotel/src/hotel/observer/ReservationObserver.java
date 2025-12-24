
package hotel.observer;

import hotel.model.Reservation;


public interface ReservationObserver {
    void onReservationCreated(Reservation reservation);
    void onReservationModified(Reservation reservation);
    void onReservationCancelled(Reservation reservation);
}

class SMSNotification implements ReservationObserver {
    @Override
    public void onReservationCreated(Reservation reservation) {
        System.out.println("SMS: Confirmation sent to " + reservation.getCustomer().getPhone());
    }
    
    @Override
    public void onReservationModified(Reservation reservation) {
        System.out.println("SMS: Modification sent to " + reservation.getCustomer().getPhone());
    }
    
    @Override
    public void onReservationCancelled(Reservation reservation) {
        System.out.println("SMS: Cancellation sent to " + reservation.getCustomer().getPhone());
    }
}