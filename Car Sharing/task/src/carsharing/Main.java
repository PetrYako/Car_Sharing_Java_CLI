package carsharing;

import carsharing.car.Car;
import carsharing.car.DbCarDao;
import carsharing.company.Company;
import carsharing.company.DbCompanyDao;
import carsharing.customer.Customer;
import carsharing.customer.DbCustomerDao;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String MENU = """
            
            1. Log in as a manager
            2. Log in as a customer
            3. Create a customer
            0. Exit""";
    private static final String MANAGER_MENU = """
            
            1. Company list
            2. Create a company
            0. Back""";
    private static final String CAR_MENU = """
            1. Car list
            2. Create a car
            0. Back""";
    private static final String CUSTOMER_MENU = """
            
            1. Rent a car
            2. Return a rented car
            3. My rented car
            0. Back""";

    private final static Scanner scanner = new Scanner(System.in);

    private static DbCompanyDao companyDao;
    private static DbCarDao carDao;
    private static DbCustomerDao customerDao;

    public static void main(String[] args) {
        String databaseName = "carshar";
        if (args.length > 0) {
            databaseName = args[1];
        }
        companyDao = new DbCompanyDao(databaseName);
        carDao = new DbCarDao(databaseName);
        customerDao = new DbCustomerDao(databaseName);
        while (true) {
            menu();
        }
    }

    public static void menu() {
        System.out.println(MENU);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                managerMenu();
                break;
            case 2:
                printCustomerList();
                break;
            case 3:
                createCustomer();
                break;
            case 0:
                System.exit(0);
        }
    }

    public static void printCustomerList() {
        List<Customer> customers = customerDao.findAll();
        if (customers.isEmpty()) {
            System.out.println("\nThe customer list is empty!");
        } else {
            System.out.println("\nCustomer list:");
            for (int i = 0; i < customers.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, customers.get(i).getName());
            }
            System.out.println("0. Back");
            int choice = scanner.nextInt();
            if (choice == 0) {
                menu();
            } else {
                Customer customer = customers.get(choice - 1);
                printCustomerMenu(customer);
            }
        }
    }

    public static void printCustomerMenu(Customer customer) {
        System.out.println(CUSTOMER_MENU);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                rentCar(customer);
                printCustomerMenu(customer);
                break;
            case 2:
                returnRentedCar(customer);
                printCustomerMenu(customer);
                break;
            case 3:
                printRentedCarList(customer);
                printCustomerMenu(customer);
                break;
            case 0:
                menu();
                break;
        }
    }

    public static void rentCar(Customer customer) {
        if (customer.getRentedCarId() != null) {
            System.out.println("\nYou've already rented a car!");
            return;
        }
        List<Company> companies = companyDao.findAll();
        if (companies.isEmpty()) {
            System.out.println("\nThe company list is empty!");
        } else {
            System.out.println("\nChoose a company:");
            for (int i = 0; i < companies.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, companies.get(i).getName());
            }
            System.out.println("0. Back");
            int choice = scanner.nextInt();
            if (choice == 0) {
                printCustomerMenu(customer);
            } else {
                Company company = companies.get(choice - 1);
                printCarListForRent(customer, company);
            }
        }
    }

    public static void printCarListForRent(Customer customer, Company company) {
        List<Car> cars = carDao.findAvailable(company.getId());
        if (cars.isEmpty()) {
            System.out.println("\nNo available cars in the '" + company.getName() + "' company");
        } else {
            System.out.println("\nChoose a car:");
            for (int i = 0; i < cars.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, cars.get(i).getName());
            }
            System.out.println("0. Back");
            int choice = scanner.nextInt();
            if (choice == 0) {
                printCustomerMenu(customer);
            } else {
                Car car = cars.get(choice - 1);
                customer.setRentedCarId(car.getId());
                customerDao.update(customer);
                System.out.println("\nYou rented '" + car.getName() + "'");
                printCustomerMenu(customer);
            }
        }
    }

    public static void returnRentedCar(Customer customer) {
        if (customer.getRentedCarId() == null) {
            System.out.println("\nYou didn't rent a car!");
        } else {
            customer.setRentedCarId(null);
            customerDao.update(customer);
            System.out.println("\n You've returned a rented car!");
        }
    }

    public static void printRentedCarList(Customer customer) {
        if (customer.getRentedCarId() == null) {
            System.out.println("\nYou didn't rent a car!");
        } else {
            Car car = carDao.findByCustomerId(customer.getId());
            Company company = companyDao.findById(car.getCompanyId());
            System.out.println("\nYour rented car:");
            System.out.println(car.getName());
            System.out.println("Company:");
            System.out.println(company.getName());
        }
    }

    public static void createCustomer() {
        System.out.println("Enter the customer name:");
        scanner.nextLine();
        String name = scanner.nextLine();
        Customer customer = new Customer(name, null);
        customerDao.create(customer);
        System.out.println("\nThe customer was added!");
    }

    public static void managerMenu() {
        System.out.println(MANAGER_MENU);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                printCompanyList();
                managerMenu();
                break;
            case 2:
                createCompany();
                managerMenu();
                break;
            case 3:
                managerMenu();
                break;
        }
    }

    public static void printCompanyList() {
        List<Company> companies = companyDao.findAll();
        if (companies.isEmpty()) {
            System.out.println("\nThe company list is empty!");
        } else {
            System.out.println("\nChoose a company:");
            for (int i = 0; i < companies.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, companies.get(i).getName());
            }
            System.out.println("0. Back");
            int choice = scanner.nextInt();
            if (choice == 0) {
                managerMenu();
            } else {
                Company company = companies.get(choice - 1);
                System.out.printf("%n'%s' company menu:%n", company.getName());
                printCompanyMenu(company);
            }
        }
    }

    public static void printCompanyMenu(Company company) {
        System.out.println(CAR_MENU);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                printCarList(company);
                printCompanyMenu(company);
                break;
            case 2:
                createCar(company);
                printCompanyMenu(company);
                break;
            case 0:
                managerMenu();
                break;
        }
    }

    public static void createCar(Company company) {
        System.out.println("Enter the car name:");
        scanner.nextLine();
        String name = scanner.nextLine();
        Car car = new Car(name, company.getId());
        carDao.create(car);
        System.out.println("\nThe car was added!");
    }

    public static void printCarList(Company company) {
        System.out.printf("'%s' cars:%n", company.getName());
        List<Car> cars = carDao.findByCompanyId(company.getId());
        if (cars.isEmpty()) {
            System.out.println("\nThe car list is empty!");
        } else {
            for (int i = 0; i < cars.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, cars.get(i).getName());
            }
        }
    }

    public static void createCompany() {
        System.out.println("Enter the company name:");
        scanner.nextLine();
        String name = scanner.nextLine();
        Company company = new Company(name);
        companyDao.create(company);
        System.out.println("\nThe company was created!");
    }
}