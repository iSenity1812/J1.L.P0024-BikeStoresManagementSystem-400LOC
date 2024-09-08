public class ProductFactory implements IProductFactory{
    @Override
    public Product createProduct(String id, String name, String brandId, String categoryId, int modelYear, double price) {
        return new Product(id, name, brandId, categoryId, modelYear, price);
    }
}
