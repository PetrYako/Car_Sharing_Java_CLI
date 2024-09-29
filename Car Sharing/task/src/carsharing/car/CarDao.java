package carsharing.car;

import java.util.List;

public interface CarDao {
    List<Car> findAvailable(int companyId);
    Car findByCustomerId(int customerId);
    List<Car> findByCompanyId(int companyId);
    void create(Car company);
    void init();
}
