
package hotel.factory;

import hotel.model.Customer;

public class CustomerFactory {
    public static Customer createCustomer(String firstName, String lastName, 
                                         String email, String phone, String type) {
        return new Customer(firstName, lastName, email, phone, type);
    }
}