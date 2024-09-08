import java.io.*;
import java.time.Year;
import java.util.*;
import java.util.function.Predicate;

public class Utils {
    private static final Scanner sc = new Scanner(System.in);

    /*
    Get 'input' string then using 'test' method to check the condition (for example as the getString method it will check if String is empty if true -> input else error message)
     */
    private static String getInput(String prompt, Predicate<String> validator, String errorMessage) {
        String input;

        do {
            System.out.print(prompt);
            input = sc.nextLine().trim();

            if (!validator.test(input)) {
                System.out.println(errorMessage);
            } else {
                return input;
            }
        } while (true);
    }

    // Check if string not empty
    public static String getString(String prompt, String errorMessage) {
        return getInput(prompt, s -> !s.isEmpty(), errorMessage);
    }

    public static String updateString(String prompt, String oldValue) {
        String input = getInput(prompt, s -> true, "[!] Invalid input");
        return input.isEmpty() ? oldValue : input;
    }

    public static int getYear(String prompt, String errorMessage) {
        return Integer.parseInt(getInput(prompt, s -> isValidYear(s), errorMessage));
    }

    public static int updateYear(String prompt, int oldValue) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        if (input.isEmpty()) return oldValue;
        else {
            try {
                return (isValidYear(input)) ? Integer.parseInt(input) : oldValue;
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid input. Keeping old value: " + oldValue);
                return oldValue;
            }
        }
    }

    private static boolean isValidYear(String year) {
        try {
            return (Integer.parseInt(year) > 1900 && Integer.parseInt(year) < Year.now().getValue());
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static double getPrice(String prompt, String errorMessage) {
        return Double.parseDouble(getInput(prompt, s -> isPositiveDouble(s), errorMessage));
    }

    public static double updatePrice(String prompt, double oldValue) {
        String input;
        System.out.print(prompt);
        input = sc.nextLine().trim();

        if (input.isEmpty()) return oldValue;
        else {
            try {
                return isPositiveDouble(input) ? Double.parseDouble(input) : oldValue;
            } catch (NumberFormatException e) {
                return oldValue;
            }
        }
    }

    private static boolean isPositiveDouble(String number) {
        try {
            return Double.parseDouble(number) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    // Check brand
    // Read from file -> get valid id -> add to set?
    public static String getValidIdFromFile(String filename, String prompt, String errorMessage){

        String input;
        boolean valid = false;
        do {
            System.out.print(prompt);
            input = sc.nextLine().trim();

           if (input.isEmpty()) {
               System.out.println(errorMessage);
           } else {
                try {
                    valid = checkId(filename, input);
                } catch (IOException e) {
                    System.out.println("An error occurred while checking the ID: " + e.getMessage());
                }
           }
        } while (!valid);

        return input;
    }

    public static String updateValidId(String filename, String prompt, String oldValue) {
        String input;
        boolean valid;
        do {
            System.out.print(prompt);
            input = sc.nextLine().trim();

            if (input.isEmpty()) return oldValue;
            else {
                try {
                    valid = checkId(filename, input);
                } catch (IOException e) {
                    System.out.println("An error occurred while checking the ID: " + e.getMessage());
                    valid = false;
                }
            }
        } while (!valid);

        return input;
    }


    private static boolean checkId(String filename, String inputId) throws IOException {
        List<String> validIds = extractIds(readFile(filename));
        return validIds.contains(inputId.toUpperCase());

    }

    private static List<String> extractIds(List<String> lines) {
        List<String> ids = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",", 2);
            if (parts.length > 1) {
                ids.add(parts[0].trim());
            }
        }
        return ids;
    }

    // Generate Id
    public static String generateProductUniqueId(String filename, List<Product> products, String prefix) {
        // Get last id from list if not have then get from file
        String id;
        if (products.isEmpty()) {
            // Dont have anything in unsaved list
            List<String> list = readFile(filename); // Get id from Products.txt

            String lastProduct = null;
            for (int i = list.size() - 1; i >= 0; i--) {
                String line = list.get(i).trim();

                if (!line.isEmpty()) {
                    lastProduct = line;
                    break;
                }
            }

            if (list.isEmpty()) {
                id = prefix + "001";
            } else {
                String[] parts = lastProduct.split(",", 2);
                String lastId = parts[0];

//             Split num
                int num = Integer.parseInt(lastId.substring(1));
                num++;
                id = prefix + String.format("%03d", num);
            }
        } else {
            String lastId = products.get(products.size() - 1).getId(); // Get last
            // Split num
            int num = Integer.parseInt(lastId.substring(1));
            num++;
            id = prefix + String.format("%03d", num);
        }

        return id;
    }

    // Read file
    public static List<String> readFile(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    // Write file
    public static void writeFile(String filename, List<Product> products){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            for (Product product : products) {
                bw.write(product.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[+] File write successful.");
    }

    // Read product from file
    public static List<Product> readProductFromFile(String filename) {
        List<Product> products = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                // id, name, brandId, categoryId, year, price
                String id = parts[0].trim();
                String name = parts[1].trim();
                String brandId = parts[2].trim();
                String categoryId = parts[3].trim();
                int year;
                double price;
                try {
                    year = Integer.parseInt(parts[4].trim());
                    price = Double.parseDouble(parts[5].trim());

                    products.add(new Product(id, name, brandId, categoryId, year, price));
                } catch (NumberFormatException e) {
                    System.out.println("Skipping failure");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    public static void mainMenu() {
        System.out.println("Main Menu:");
        System.out.println("1. Create Product");
        System.out.println("2. Search Product by Name");
        System.out.println("3. Update Product Information");
        System.out.println("4. Delete Product Information");
        System.out.println("5. Save Products to File");
        System.out.println("6. Print All Products from File");
        System.out.println("0. Exit");

    }

    public static void backToMenu() {
        System.out.print("Press enter to back to menu.");
        sc.nextLine();

    }

}
