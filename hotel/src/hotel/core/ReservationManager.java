package hotel.core;
import hotel.model.Customer;
import java.util.*;
import hotel.model.Reservation;
import hotel.observer.ReservationObserver;
import hotel.model.Room;
import hotel.services.CustomerService;
import hotel .observer.ReservationObserver;

public class ReservationManager {
    
    private static final ReservationManager RM = new ReservationManager();
    private List<Reservation> reservations;
    
    
    
private List<ReservationObserver> observers = new ArrayList<>();

public void addObserver(ReservationObserver observer) {
    observers.add(observer);
}

private void notifyCreated(Reservation res) {
    for (ReservationObserver observer : observers) {
        observer.onReservationCreated(res);
    }
}

private void notifyModified(Reservation res) {
    for (ReservationObserver observer : observers) {
        observer.onReservationModified(res);
    }
}     
    
    
    
    
    private ReservationManager(){
        this.reservations = new ArrayList<>();
    }
    
    public static ReservationManager getInstance(){
        return RM;
    }
    
    public Reservation createReservation(Customer customer , Room room , Date startDate ,Date endDate){
        Date today = new Date();
        
        if(startDate.before(today)){
        System.out.println(" Start date must be in the future");
        return null;
    }
    if (endDate.before(startDate) || endDate.equals(startDate)){
        System.out.println(" End date must be after start date");
        return null;
    }
    long no_nights = (endDate.getTime() - startDate.getTime()) / (24*60*60*1000);
    if(no_nights < 1){
        System.out.println(" Minimum stay is 1 night");
        return null;
    }
    if(no_nights > 30){
        System.out.println(" Maximum stay is 30 days");
        return null;
    }
    if(!isroomAvailable(room, startDate, endDate)){
        System.out.println(" Room is not available for these dates");
        return null;
    }
    
    Reservation reservation = new Reservation(customer, room, startDate, endDate);
    reservations.add(reservation);
    room.setStatus("booked");
    notifyCreated(reservation);
    return reservation;
    }
    
    public boolean cancelReservation(String reservationID){
        for(int i = 0; i < reservations.size(); i++){
            Reservation res = reservations.get(i);
            if(res.getReservationID().equals(reservationID)){
                if(res.ReservationOccupied() && new Date().after(res.getStartDate())){
                    System.out.println("can't cancel reservation");
                    return false;
                }
                res.setStatus("cancelled");
                res.getRoom().setStatus("available");
                return true;        
            }
        }
        return false;
    }

public boolean modifyReservation(String reservationID, Date newStartDate, Date newEndDate) {
    String targetID = reservationID.trim();
    
    for (Reservation res : reservations) {
        System.out.println("Checking: " + res.getReservationID() + " against " + targetID);
        
        if (res.getReservationID().trim().equals(targetID)) {
            if (!newEndDate.after(newStartDate)) {
                return false; 
            }
            res.updateReservationDate(newStartDate, newEndDate);
            res.setStatus("MODIFIED");
            notifyModified(res);
            return true;
        }
    }
    return false;
}

public void addReservationDirectly(Reservation res) {
    this.reservations.add(res);
}
    public List<Reservation>getCustomerReservations(String customerID){
        List<Reservation>customerReservations = new ArrayList<>();

        for(int i = 0; i<reservations.size();i++){
            Reservation reservation = reservations.get(i);
            String resCustomerID = reservation.getCustomer().getCustomerID();
            if(resCustomerID.equals(customerID)){
                customerReservations.add(reservation);
            }
        }
        return customerReservations;
    }
    public List<Reservation>getActiveReservations(String customerID){
        List<Reservation>ActiveReservations = new ArrayList<>();
        List<Reservation> customerReservations = getCustomerReservations(customerID);

        for(int i = 0; i<customerReservations.size();i++){
            Reservation reservation = customerReservations.get(i);
            if(reservation.ReservationOccupied()){
                ActiveReservations.add(reservation);
            }
        }    
        return ActiveReservations;
    }

    public List<Reservation>getFutureReservations(String customerID){
        List<Reservation>futureReservations = new ArrayList<>();
        List<Reservation>customerReservations = getCustomerReservations(customerID); 
        for(int i = 0; i < customerReservations.size(); i++){
            Reservation reservation = customerReservations.get(i);
            if(reservation.ReserveInFuture()){ 
                futureReservations.add(reservation);
            }
        }    
        return futureReservations;
    }

    public Reservation getReservation(String reservationID){
        for(int i = 0; i<reservations.size();i++){
            Reservation reservation = reservations.get(i);
            if(reservation.getReservationID().equals(reservationID)){
                return reservation;
            }
        }    
        return null;
    }
    public Reservation getReservationByEmail(String email){
    for (Reservation reservation : reservations) { 
        if (reservation.getEmail().equalsIgnoreCase(email)) {
            return reservation;
        }
    }
    return null;
    }
    
    public List<Reservation> getReservationsByName(String firstName, String lastName) {
        List<Reservation> matches = new ArrayList<>();

        for (Reservation reservation : reservations) {
            String resFirstName = reservation.getFirstName();
            String resLastName = reservation.getLastName();
            if (resFirstName.equalsIgnoreCase(firstName) && resLastName.equalsIgnoreCase(lastName)) {
                matches.add(reservation);
            }
        }
        return matches;
    }

    public List<Reservation>getAllReservations(){
        return new ArrayList<>(reservations);
    }

    private boolean isroomAvailable(Room room, Date startDate, Date endDate){
        String roomNumber = room.getRoomNumber();
        for (int i = 0; i < reservations.size(); i++){
            Reservation reservation = reservations.get(i);
            String resRoomNumber = reservation.getRoom().getRoomNumber();  
            if(resRoomNumber.equals(roomNumber)){
                if(!reservation.getStatus().equals("cancelled")){
                    if(reservation.overlaps(startDate, endDate)){  
                        return false;
                    }
                }
            }
        }
        return true;
    }

   public List<Integer> getCustomerCountsOnDate(Date specificDate) {
       List<Integer> customerCounts = new ArrayList<>();
       for (int i = 0; i < reservations.size(); i++) {
           Reservation reservation = reservations.get(i);
           boolean dateIsInRange = !specificDate.before(reservation.getStartDate()) && 
                                  !specificDate.after(reservation.getEndDate());

           boolean isActive = !reservation.getStatus().equals("cancelled");
           if (dateIsInRange && isActive) {
               int customerCount = 1;  
               customerCounts.add(customerCount);
           }
       }

       return customerCounts;
   }

   public int getMinCustomerCountOnDate(Date specificDate) {
       List<Integer> counts = getCustomerCountsOnDate(specificDate);
       if (counts.size() == 0) {
           return 0; 
       }
       int minCount = counts.get(0);
       for (int i = 1; i < counts.size(); i++) {
           int count = counts.get(i);
           if (count < minCount) {
               minCount = count;
           }
       }
       return minCount;
   }


   public int getMaxCustomerCountOnDate(Date specificDate) {
       List<Integer> counts = getCustomerCountsOnDate(specificDate);
       if (counts.size() == 0) {
           return 0;  
       }
       int maxCount = 0;
       for (int i = 0; i < counts.size(); i++) {
           int count = counts.get(i);
           if (count > maxCount) {
               maxCount = count;
           }
       }

       return maxCount;
   }
}    