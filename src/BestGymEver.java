import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Viktor Kodet <br>
 * Date: 2020-10-09 <br>
 * Time: 14:33 <br>
 * Project: IntelliJ IDEA <br>
 */
public class BestGymEver {

    List<Customer> customers = new ArrayList<>();
    boolean testingMode;


    public String getInput(boolean test) {
        testingMode = test;
        String input;

        if (testingMode) {
            input = "bear belle";
        } else {
            input = JOptionPane.showInputDialog("Mata in personens för- och efternamn \n Alternativt personnummer:");
            if (input == null) {
                System.exit(0);
            }
        }
        return input;
    }

    public boolean checkMembership(Customer c, boolean test) {
        testingMode = test;
        LocalDate today;

        if (testingMode) {
            today = LocalDate.parse("2020-10-10");
        } else {
            today = LocalDate.now();
        }

        Period p = Period.between(c.getLastPayment(), today);
        return p.getYears() <= 0;
    }

    public void parseCustomerFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        try (Scanner scanner = new Scanner(file)) {                    // try with resources

            while (scanner.hasNext()) {
                String id = scanner.next();
                id = id.replace(",", "");
                String firstName = scanner.next();
                String lastName = scanner.nextLine();
                String date = scanner.nextLine();
                LocalDate lastPayment = LocalDate.parse(date);
                getCustomers().add(new Customer(id, firstName, lastName, lastPayment));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Filen \"" + fileName + "\" kunde inte hittas.");
            throw e;
        }
    }

//TODO måste kunna mata in personnr
    public void checkCustomer(String input) throws IOException {
        input = input.trim();
        input = input.replace("-", "");
        Scanner scanner = new Scanner(input);
        if(Character.isDigit(input.charAt(0))){
            checkCustomerById(scanner);
        } else {
            checkCustomerByName(scanner);
        }

    }

    public void checkCustomerById(Scanner scanner) throws IOException {
        String id = scanner.next();
        if (scanner.hasNext()) {
            JOptionPane.showMessageDialog(null, "Oj då, nu vart det mycket." +
                    "\n Mata endast in för- och efternamn, eller personnummer.");
            return;
        }
        if (id.length() != 10){
            JOptionPane.showMessageDialog(null, "Personnummret måste innehålla 10 siffror.");
            return;
        }
        for (Customer c : getCustomers()){
            if(c.getId().equals(id)){
                if (checkMembership(c, false)) {
                    registerWorkout(c, false);
                    JOptionPane.showMessageDialog(null, "Välkommen " + c.getFirstName() + ". Besök registrerat.");
                    return;
                } else {
                    JOptionPane.showMessageDialog(null, "Oj då, " + c.getFirstName() +
                            " har inte betalat sin årsavgift. \n Senaste betalningen : " + c.getLastPayment());
                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Personen finns inte i systemet.");
    }

    public void checkCustomerByName(Scanner scanner) throws IOException {
        String name1;
        String name2;
        try {
            name1 = scanner.next();
            name2 = scanner.next();
        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "Felaktig inmatning, måste innehålla för- och efternamn, eller personnummer");
            return;
        }

        if (scanner.hasNext()) {
            JOptionPane.showMessageDialog(null, "Oj då, nu vart det mycket." +
                    "\n Mata endast in för- och efternamn, eller personnummer");
            return;
        }

        for (Customer c : getCustomers()) {
            if ((name1.equalsIgnoreCase(c.getFirstName()) && name2.equalsIgnoreCase(c.getLastName())) ||
                    (name2.equalsIgnoreCase(c.getFirstName()) && name1.equalsIgnoreCase(c.getLastName()))) {

                if (checkMembership(c, false)) {
                    registerWorkout(c, false);
                    JOptionPane.showMessageDialog(null, "Välkommen " + c.getFirstName() + ". Besök registrerat.");
                    return;
                } else {
                    JOptionPane.showMessageDialog(null, "Oj då, " + c.getFirstName() +
                            " har inte betalat sin årsavgift. \n Senaste betalningen : " + c.getLastPayment());
                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Personen finns inte i systemet.");
    }

    public void registerWorkout(Customer customer, boolean test) throws IOException {
        testingMode = test;
        LocalDate today;
        if (testingMode) {
            today = LocalDate.parse("2020-10-10");
        } else {
            today = LocalDate.now();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter
                (new File("" + customer.getFirstName() + customer.getLastName() + customer.getId()) + ".txt", true));
        String s = customer.printMe() + " " + today;
        writer.write(s);
        writer.newLine();
        writer.close();
    }

    public void gymStart(String fileName) throws IOException {
        parseCustomerFile(fileName);
        while (true) {
            checkCustomer(getInput(false));
        }
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public static void main(String[] args) throws IOException {
        BestGymEver gym = new BestGymEver();
        gym.gymStart("customers.txt");
    }

}
