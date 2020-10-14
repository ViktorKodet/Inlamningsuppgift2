import org.junit.jupiter.api.Test;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Viktor Kodet <br>
 * Date: 2020-10-09 <br>
 * Time: 14:33 <br>
 * Project: IntelliJ IDEA <br>
 */
class BestGymEverTest {

    @Test
    public void customerTest(){
        Customer customer = new Customer("7502031234",  "Anna",  "Andersson", LocalDate.parse("2020-01-30"));
        assertEquals(customer.getId(), "7502031234");
        assertNotEquals(customer.getId(), "1234567890");

        assertEquals(customer.getFirstName(), "Anna");
        assertNotEquals(customer.getFirstName(), "Jens");

        assertEquals(customer.getLastName(), "Andersson");
        assertNotEquals(customer.getLastName(), "Schmeid");
    }

    @Test
    public void customerListTest(){
        BestGymEver gym = new BestGymEver();
        gym.getCustomers().add(new Customer("7502031234",  "Anna",  "Andersson", LocalDate.parse("2020-01-30")));
        gym.getCustomers().add(new Customer("1234567890",  "Jens",  "Schmeid", LocalDate.parse("2020-01-30")));
        assertEquals(gym.getCustomers().get(0).getId(), "7502031234");
        assertEquals(gym.getCustomers().get(1).getId(), "1234567890");

    }


    @Test
    public void getInputTest(){
        BestGymEver gym = new BestGymEver();
        assertTrue(gym.getInput(true).equalsIgnoreCase("bear belle"));
        assertFalse(gym.getInput(true).equalsIgnoreCase("bear bell"));
    }

    @Test
    public void writerTest() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter
                (new File("test.txt")));
        String s = "testtext";
        writer.write(s);
        writer.close();
    }

    @Test
    public void checkCustomerTest() throws IOException {
        BestGymEver gym = new BestGymEver();
        gym.getCustomers().add(new Customer("1234567890", "Pelle", "Svanslös", LocalDate.parse("2020-01-30")));
        gym.getCustomers().add(new Customer("7502031234",  "Anna",  "Andersson", LocalDate.parse("2020-01-30")));

        String output = gym.checkCustomer("123456-7890", true);
        assertEquals(output, "Välkommen Pelle. Besök registrerat.");

        output = gym.checkCustomer("svanslös pelle", true);
        assertEquals(output, "Välkommen Pelle. Besök registrerat.");

        output = gym.checkCustomer("7502031234", true);
        assertEquals(output, "Välkommen Anna. Besök registrerat.");
    }

    @Test
    public void registerWorkoutTest() throws IOException {
        BestGymEver gym = new BestGymEver();
        gym.getCustomers().add(new Customer("1234567890", "Pelle", "Svanslös", LocalDate.parse("2020-01-30")));
        gym.registerWorkout(gym.getCustomers().get(0), true);

        BufferedReader reader = new BufferedReader(new FileReader("PelleSvanslös1234567890.txt"));
        assertEquals(reader.readLine(), "Pelle Svanslös 1234567890 2020-10-10");
        assertNotEquals(reader.readLine(), "Pelle Svanslös 1234567890 2020-10-11");
    }

    @Test
    public void dateTest() {         //Skräpkod, användes bara för att experimentera,
        String s1 = "2017-05-03";
        String s2 = "2016-05-02";

        LocalDate date1 = LocalDate.parse(s1);
        LocalDate date2 = LocalDate.parse(s2);
        Period p = Period.between(date2, date1);
        System.out.println(p.getYears());
    }

    @Test
    public void checkMembershipTest(){
        BestGymEver gym = new BestGymEver();
        gym.getCustomers().add(new Customer("7502031234",  "Anna",  "Andersson", LocalDate.parse("2020-01-30")));
        gym.getCustomers().add(new Customer("1234567890",  "Jens",  "Schmeid", LocalDate.parse("2019-01-30")));
        assertTrue(gym.checkMembership(gym.getCustomers().get(0), true));
        assertFalse(gym.checkMembership(gym.getCustomers().get(1), true));
    }

    @Test
    public void fileNameIsNullTest(){
        assertThrows(NullPointerException.class, () -> {
            BestGymEver gym = new BestGymEver();
            gym.parseCustomerFile(null);
        } );
    }

    @Test
    public void fileNotFoundTest(){
        assertThrows(FileNotFoundException.class, () -> {
            BestGymEver gym = new BestGymEver();
            gym.parseCustomerFile("asdf");
        });
    }

}