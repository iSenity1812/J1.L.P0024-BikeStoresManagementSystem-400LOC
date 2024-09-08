
import java.util.Scanner;

/**
 *
 * @author isepipi
 */

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    public static final String BRANDID_VALID_FILE = "resources/BrandValid.txt";
    public static final String CATEGORYID_VALID_FILE = "resources/CategoryValid.txt";
    public static final String PRODUCTS = "resources/Products.txt";
    public static void main(String[] args) {
        ProductManager pm = new ProductManager();

        while (true) {
            Utils.mainMenu();
            int choice;
            System.out.print("Choose an option: ");
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid input. Please enter a number.");
                continue;
            }


            switch (choice) {
                case 1:
                    pm.createProduct();
                    break;
                case 2:
                    pm.searchProductByName();
                    break;
                case 3:
                    pm.updateProductInfo();
                    break;
                case 4:
                    pm.removeProduct();
                    break;
                case 5:
                    pm.saveToFile();
                    break;
                case 6:
                    pm.printFromFile();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("[!] Invalid choice.");
            }
        }
    }
}
