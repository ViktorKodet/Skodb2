package skodb2.menu;

import skodb2.db.Beställning;
import skodb2.db.Kund;
import skodb2.db.Repository;
import skodb2.db.Sko;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Scanner;

public class Meny {

    Scanner scan = new Scanner(System.in);
    Kund k;

    Meny(){
        loginMenu();
    }

    public void loginMenu(){
        List<Kund> kundList = Repository.getAllCustomers();

        String användarnamn;
        String lösenord;

        while(true){
            boolean found = false;
            System.out.println("Skriv in användarnamn:");
            användarnamn = scan.nextLine();
            System.out.println("Skriv in lösenord:");
            lösenord = scan.nextLine();
            for(Kund kund : kundList){
                if (kund.getAnvändarnamn().equals(användarnamn) && kund.getLösenord().equals(lösenord)){
                    k = kund;
                    found = true;
                    mainMenu();
                    break;
                }
            }
            if (!found) {
                System.out.println("Användarnamn eller lösenord är fel, försök igen");
            }
        }
    }

    public void mainMenu(){
        System.out.println("Välkommen " + k.getNamn());
        String input;
        Boolean inloggad = true;
        while(inloggad){
            System.out.println("Mata in en siffra i consolen för att välja alternativ:" +
                    "\n 1: Sko-menyn" +
                    "\n 2: Se din beställning" +
                    "\n 3: Slutför beställning" +
                    "\n 0: Logga ut");
            input = scan.nextLine();
            switch(input) {
                case "1": shoeMenu();
                    break;
                case "2": Repository.getActiveOrder(k).printAllShoes();
                    break;
                case "3": Repository.finalizeOrder(Repository.getActiveOrderId(k));
                    break;
                case "0": inloggad = false;
                    break;
                default:
                    System.out.println("Felaktig inmatning, försök igen.");
                    break;

            }
        }
    }

    public void shoeMenu(){
        List<Sko> skoList = Repository.getAllActiveShoes();
        for(Sko sko : skoList){
            System.out.println(sko.toStringForCustomer());
        }
        String input;
        while(true) {
            System.out.println("Mata in en siffra i consolen för att välja alternativ:" +
                    "\n 1: Beställ en sko" +
                    "\n 2: Betygsätt en sko" +
                    "\n 0: Tillbaka till huvudmenyn");
            input = scan.nextLine();
            switch (input) {
                case "1": shoeOrderMenu();
                    break;
                case "2": shoeRatingMenu();
                    break;
                case "0": return;
                default:
                    System.out.println("Felaktig inmatning, försök igen.");
                    break;

            }
        }
    }

    public void shoeOrderMenu(){
        System.out.println("Mata in skons namn:");
        String name = scan.nextLine();
        System.out.println("Mata in skons färg:");
        String color = scan.nextLine();
        System.out.println("Mata in skons storlek:");
        int size = scan.nextInt();

        try{
            Repository.addToCart(k.getId(), Repository.getActiveOrderId(k), Repository.getShoeId(name, color, size));
            System.out.println("Sko tillagd.");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void shoeRatingMenu(){
        System.out.println("Mata in skons namn:");
        String name = scan.nextLine();
        System.out.println("Mata in skons färg:");
        String color = scan.nextLine();
        System.out.println("Mata in skons storlek:");
        int size = scan.nextInt();
        System.out.println("Mata in ditt betyg (1-10):");
        int betyg = scan.nextInt();
        System.out.println("Mata in din kommentar:");
        String kommentar = scan.nextLine();
        try{
            Repository.rateShoe(Repository.getShoeId(name, color, size), k.getId(), betyg, kommentar);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Meny();
    }

}
