import java.time.LocalDate;

/**
 * Created by Viktor Kodet <br>
 * Date: 2020-10-09 <br>
 * Time: 14:32 <br>
 * Project: IntelliJ IDEA <br>
 */
public class Customer {

    private String id;
    private String firstName;
    private String lastName;
    private LocalDate lastPayment;

    Customer(String id, String firstName, String lastName, LocalDate lastPayment){
        setId(id.trim());
        setFirstName(firstName.trim());
        setLastName(lastName.trim());
        setLastPayment(lastPayment);
    }

    public String printMe(){
        return firstName + " " + lastName + " " + id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(LocalDate lastPayment) {
        this.lastPayment = lastPayment;
    }
}
