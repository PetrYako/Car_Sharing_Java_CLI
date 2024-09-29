package carsharing;

import carsharing.car.Car;
import carsharing.company.Company;
import carsharing.customer.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbClient {
    private final Connection connection;

    public DbClient(Connection connection) throws SQLException {
        this.connection = connection;
        this.connection.setAutoCommit(true);
    }

    public void run(String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(query);
    }

    public List<Company> selectForCompanyList(String query) throws SQLException {
        List<Company> companies = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            companies.add(new Company(resultSet.getInt("id"), resultSet.getString("name")));
        }
        return companies;
    }

    public Company selectCompany(String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return new Company(resultSet.getInt("id"), resultSet.getString("name"));
        }
        return null;
    }

    public Car selectCar(String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return new Car(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("company_id"));
        }
        return null;
    }

    public List<Car> selectForCarList(String query) throws SQLException {
        List<Car> cars = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            cars.add(new Car(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("company_id")));
        }
        return cars;
    }

    public List<Customer> selectForCustomerList(String query) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            customers.add(new Customer(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getObject("rented_car_id", Integer.class)));
        }
        return customers;
    }
}
