package carsharing.car;

import carsharing.DbClient;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DbCarDao implements CarDao {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String CONNECTION_URL = "jdbc:h2:./src/carsharing/db/%s";

    private final DbClient dbClient;

    public DbCarDao(String dbName) {
        try {
            Class.forName(JDBC_DRIVER);
            this.dbClient = new DbClient(DriverManager.getConnection(CONNECTION_URL.formatted(dbName)));
            init();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Car> findAvailable(int companyId) {
        try {
            return dbClient.selectForCarList("SELECT * FROM CAR LEFT JOIN CUSTOMER ON CAR.ID = CUSTOMER.RENTED_CAR_ID" +
                    " WHERE COMPANY_ID = " + companyId +
                    " AND CUSTOMER.ID IS NULL");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car findByCustomerId(int customerId) {
        try {
            return dbClient.selectCar("SELECT * FROM CAR INNER JOIN CUSTOMER ON CAR.ID = CUSTOMER.RENTED_CAR_ID WHERE CUSTOMER.ID = " + customerId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Car> findByCompanyId(int companyId) {
        try {
            return dbClient.selectForCarList("SELECT * FROM CAR WHERE COMPANY_ID = " + companyId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Car car) {
        try {
            dbClient.run("INSERT INTO CAR (NAME, COMPANY_ID) VALUES ('" + car.getName() + "'," + car.getCompanyId() + ")");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() {
        try {
            dbClient.run("CREATE TABLE IF NOT EXISTS CAR (ID INT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR(255) UNIQUE NOT NULL, COMPANY_ID INT NOT NULL, FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID) ON DELETE CASCADE)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
