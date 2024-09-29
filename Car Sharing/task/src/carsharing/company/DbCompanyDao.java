package carsharing.company;

import carsharing.DbClient;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DbCompanyDao implements CompanyDao {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String CONNECTION_URL = "jdbc:h2:./src/carsharing/db/%s";

    private final DbClient dbClient;

    public DbCompanyDao(String dbName) {
        try {
            Class.forName(JDBC_DRIVER);
            this.dbClient = new DbClient(DriverManager.getConnection(CONNECTION_URL.formatted(dbName)));
            init();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Company findById(int id) {
        try {
            return dbClient.selectCompany("SELECT * FROM COMPANY WHERE ID = " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Company> findAll() {
        try {
            return dbClient.selectForCompanyList("SELECT * FROM COMPANY");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Company company) {
        try {
            dbClient.run("INSERT INTO COMPANY (NAME) VALUES ('" + company.getName() + "')");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() {
        try {
            dbClient.run("CREATE TABLE IF NOT EXISTS COMPANY (ID INT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR(255) UNIQUE NOT NULL)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
