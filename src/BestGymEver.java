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


    public String getInput(boolean test) {
        String input;

        if (test) {
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
        LocalDate today;

        if (test) {
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

    public String checkCustomer(String input, boolean test) throws IOException {
        input = input.trim();
        input = input.replace("-", "");
        Scanner scanner = new Scanner(input);
        if(Character.isDigit(input.charAt(0))){
            return checkCustomerById(scanner, test);
        } else {
            return checkCustomerByName(scanner, test);
        }

    }

    public String checkCustomerById(Scanner scanner, boolean test) throws IOException {
        String id = scanner.next();
        if (scanner.hasNext()) {
            return "Mata endast in för- och efternamn, eller personnummer.";
        }
        if (id.length() != 10){
            return "Personnummret måste innehålla 10 siffror.";
        }
        for (Customer c : getCustomers()){
            if(c.getId().equals(id)){
                if (checkMembership(c, test)) {
                    registerWorkout(c, test);
                    return "Välkommen " + c.getFirstName() + ". Besök registrerat.";
                } else {
                    return "Oj då, " + c.getFirstName() +
                            " har inte betalat sin årsavgift. \n Senaste betalningen : " + c.getLastPayment();
                }
            }
        }
        return "Personen finns inte i systemet.";
    }

    public String checkCustomerByName(Scanner scanner, boolean test) throws IOException {
        String name1;
        String name2;
        try {
            name1 = scanner.next();
            name2 = scanner.next();
        } catch (NoSuchElementException e) {
            return "Felaktig inmatning, måste innehålla för- och efternamn, eller personnummer";
        }

        if (scanner.hasNext()) {
            return "Mata endast in för- och efternamn, eller personnummer";
        }

        for (Customer c : getCustomers()) {
            if ((name1.equalsIgnoreCase(c.getFirstName()) && name2.equalsIgnoreCase(c.getLastName())) ||
                    (name2.equalsIgnoreCase(c.getFirstName()) && name1.equalsIgnoreCase(c.getLastName()))) {

                if (checkMembership(c, test)) {
                    registerWorkout(c, test);
                    return "Välkommen " + c.getFirstName() + ". Besök registrerat.";
                } else {
                    return "Oj då, " + c.getFirstName() +
                            " har inte betalat sin årsavgift. \n Senaste betalningen : " + c.getLastPayment();
                }
            }
        }
        return "Personen finns inte i systemet.";
    }

    public void registerWorkout(Customer customer, boolean test) throws IOException {
        LocalDate today;
        if (test) {
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
        String out;
        while (true) {
            out = checkCustomer(getInput(false), false);
            JOptionPane.showMessageDialog(null, out);
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
