package carsharing.company;

import java.util.List;

public interface CompanyDao {
    Company findById(int id);
    List<Company> findAll();
    void create(Company car);
    void init();
}
