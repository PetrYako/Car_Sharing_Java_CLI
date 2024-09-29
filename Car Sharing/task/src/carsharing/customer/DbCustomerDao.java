package carsharing.customer;

import carsharing.DbClient;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DbCustomerDao implements CustomerDao {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String CONNECTION_URL = "jdbc:h2:./src/carsharing/db/%s";

    private final DbClient dbClient;

    public DbCustomerDao(String dbName) {
        try {
            Class.forName(JDBC_DRIVER);
            this.dbClient = new DbClient(DriverManager.getConnection(CONNECTION_URL.formatted(dbName)));
            init();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Customer customer) {
        try {
            dbClient.run("UPDATE CUSTOMER SET RENTED_CAR_ID = " + customer.getRentedCarId() + " WHERE ID = " + customer.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findAll() {
        try {
            return dbClient.selectForCustomerList("SELECT * FROM CUSTOMER");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Customer customer) {
        try {
            dbClient.run("INSERT INTO CUSTOMER (NAME, RENTED_CAR_ID) VALUES ('" + customer.getName() + "'," + customer.getRentedCarId() + ")");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() {
        try {
            dbClient.run("CREATE TABLE IF NOT EXISTS CUSTOMER (ID INT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR(255) UNIQUE NOT NULL, RENTED_CAR_ID INT, FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID))");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
