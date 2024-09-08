import java.io.File;
import java.io.IOException;
import java.util.*;

public class ProductManager {
    private final List<Product> productList = new ArrayList<>();
    private final IProductFactory productFactory = new ProductFactory();
//    private static final Scanner sc = new Scanner(System.in);

    public static final String BRANDID_VALID_FILE = "resources/BrandValid.txt";
    public static final String CATEGORYID_VALID_FILE = "resources/CategoryValid.txt";
    public static final String PRODUCTS = "resources/Products.txt";

    // Create a product
    public void createProduct() {

        if (isExisted(PRODUCTS)) {
            String id, name, brandId, categoryId;
            int modelYear;
            double price;

            id = Utils.generateProductUniqueId(PRODUCTS, productList, "P");
            System.out.println("[NOTICE] Nothing can be changed if data is saved to a file");
            System.out.println("[+] Id generated: " + id);
            name = Utils.getString("[+] Product's name: ", "[!] Please not enter empty string!");
            brandId = Utils.getValidIdFromFile(BRANDID_VALID_FILE, "[+] Brand's Id (Please enter valid id (B001 or B002)): ", "[!] Please not enter empty string!");
            categoryId = Utils.getValidIdFromFile(CATEGORYID_VALID_FILE, "[+] Category's Id (C001 or C002): ", "[!] Please not enter empty string!");
            modelYear = Utils.getYear("[+] Model year: ", "[!] Please enter valid year (>1900");
            price = Utils.getPrice("[+] Enter price: ", "[!] Valid number please");

            Product product = productFactory.createProduct(id, name, brandId, categoryId, modelYear, price);
            productList.add(product);
            System.out.println("[+] PRODUCT CREATED SUCCESSFULLY");

            Utils.backToMenu();
        } else {
            System.out.println("[-] Can not create a file");
        }
    }

    // Function 2: Search Product information by name
    /*
        return a list of products
        if null -> "Have no any product"
        else print by Model Year ascending
     */

    private List<Product> getFullProducts() {
        List<Product> savedProducts = Utils.readProductFromFile(PRODUCTS);
        List<Product> totalProducts = new ArrayList<>();
        totalProducts.addAll(savedProducts);
        totalProducts.addAll(productList);

        return totalProducts;
    }

    public void searchProductByName() {
        String nameInput;
        nameInput = Utils.getString("[+] Enter name to search: ", "[!] Please not enter empty string!");
        String searchString = nameInput.toLowerCase();

        List<Product> totalProducts = getFullProducts();
        if (totalProducts.isEmpty()) {
            System.out.println("Empty");
            return;
        }

        List<Product> results = new ArrayList<>();
        for (Product product : totalProducts) {
            if (product.getName().toLowerCase().contains(searchString)) {
                results.add(product);
            }
        }
        results.sort(Comparator.comparingInt(Product::getModelYear));

        if (results.isEmpty()) System.out.println("[!] Have no any product such name: " + nameInput);
        else results.forEach(System.out::println);
        Utils.backToMenu();
    }

    // Function 3: Update Product information
    public void updateProductInfo() {
        int index = -1;
//        List<Product> totalProducts = getFullProducts();
        String id = Utils.getString("[+] Enter product id: ", "[!] Please not enter empty string.");

        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getId().equalsIgnoreCase(id)) {
                index = i;
                updateProductInfo_update(i, productList);
                System.out.println("[+] Update successfully!");
                break;
            }
        }
        if (index == -1) {
            System.out.println("[!] Product with Id " + id + " not found");
            System.out.println("[-] Update failed.");
        }
        Utils.backToMenu();
    }

    private void updateProductInfo_update(int index, List<Product> products) {
        // Get old value
        Product product = products.get(index);
        String newName = Utils.updateString("[+] Enter name to update: ", product.getName());
        product.setName(newName);

        String newBrandId = Utils.updateValidId(BRANDID_VALID_FILE, "[+] Enter brand's id to update: ", product.getBrandId());
        String newCategoryId = Utils.updateValidId(CATEGORYID_VALID_FILE, "[+] Enter category's id to update: ", product.getCategoryId());
        int newModelYear = Utils.updateYear("[+] Enter new model year: ", product.getModelYear());
        double price = Utils.updatePrice("[+] Enter price to update: ", product.getPrice());

        product.setName(newBrandId);
        product.setCategoryId(newCategoryId);
        product.setModelYear(newModelYear);
        product.setPrice(price);

        Utils.backToMenu();
    }

    // Function 4: Delete Product information
    public void removeProduct() {
        String id = Utils.getString("[+] Enter product id: ", "[!] Please not enter empty string.");
        // Just remove from unsaved list

        // Check in current list
        List<Product> toRemove = new ArrayList<>();

        // Can not remove during loop so we will add to another list and remove when finish loop
        for (Product product : productList) {
            if (product.getId().equalsIgnoreCase(id)) {
                toRemove.add(product);
            }
        }

        if (!toRemove.isEmpty()) {
            productList.removeAll(toRemove);
            System.out.println("[+] Product removed successfully.");
        } else {
            System.out.println("[!] Product with Id " + id + " not found\nRemove failed.");
        }
    }

    // Function 5: Save to file
    public void saveToFile() {

        // Check if the file exists
        if (isExisted(PRODUCTS)) {
            Utils.writeFile(PRODUCTS, productList);
        } else {
            System.out.println("[!] Can not create file.");
            return;
        }

        // Clear list for next save
        productList.clear();
        Utils.backToMenu();
    }

    // Check if file exist
    public boolean isExisted(String filename) {
        File file = new File(filename);

        if (file.exists()) {
            return true;
        } else {
            System.out.println("[-] The file does not exist. Creating a new file.");
            try {
                file.createNewFile();
                return true;
            } catch (IOException e) {
                System.out.println("An error occurred while creating the file");
                e.printStackTrace();
            }
        }
        return false;
    }

    // Function 6: Print all lists from file
    public void printFromFile() {
        List<Product> products = Utils.readProductFromFile(PRODUCTS);
        // Sort products by list price descending, then by name ascending
        products.sort(Comparator.comparingDouble(Product::getPrice).reversed().thenComparing(Product::getName));
        // Display products
        System.out.println("Type: Price descending");
        System.out.println("================================================================");
        System.out.println("ID, Name, Brand Id, Category Id, Model Year, List Price");
        for (Product product : products) {
            System.out.println(product);
        }

        Utils.backToMenu();
    }
}
