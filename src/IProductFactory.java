
/**
 *
 * @author isepipi
 */

public interface IProductFactory {
    Product createProduct(String id, String name, String brandId, String categoryId, int modelYear, double price);
}
