package carsharing.customer;

import java.util.List;

public interface CustomerDao {
    void update(Customer customer);
    List<Customer> findAll();
    void create(Customer customer);
    void init();
}
