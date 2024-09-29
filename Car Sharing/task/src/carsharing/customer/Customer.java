package carsharing.customer;

public class Customer {
    private int id;
    private String name;
    private Integer rentedCarId;

    public Customer(Integer id, String name, Integer rentedCardId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCardId;
    }

    public Customer(String name, Integer rentedCardId) {
        this.name = name;
        this.rentedCarId = rentedCardId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    public void setRentedCarId(Integer rentedCardId) {
        this.rentedCarId = rentedCardId;
    }
}
