package Ecommerce.product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductCatalog {
    private List<Product> products;

    public ProductCatalog() {
        this.products = new ArrayList<>();
        readProducts();
    }

    public Product getProductByName(String productName) {
        for (Product product : products) {
            if (product.getName().trim().equalsIgnoreCase(productName.trim())) {
                return product;
            }
        }
        return null;
    }

    private static final String CSV_FILE_PATH = "C:\\java-workspace\\E-CommerceApp\\src\\main\\resources\\Product.csv";

    public void readProducts() {
        String line;
        String csvSplitBy = ",";
        boolean skipHeader = true;

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] data = line.split(csvSplitBy);
                int productId = Integer.parseInt(data[0]);
                String name = data[1];
                String description = data[2];
                double price = Double.parseDouble(data[3]);
                int quantity = Integer.parseInt(data[4]);
                String brand = data[6];
                Product product = new Product(productId, name, description, price, quantity, brand);
                addProduct(product);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }


    public void displayProducts() {
        if (products.isEmpty()) {
            readProducts();
        }

        System.out.println("Product Catalog:");
        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.printf("%-20s %-30s %-10s %-10s %-20s\n", "Name", "Description", "Price", "Quantity", "Brand");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (Product product : products) {
            System.out.printf("%-20s %-30s $%-10.2f %-10d %-20s\n",
                    product.getName(), product.getDescription(), product.getPrice(), product.getQuantity(), product.getBrand());
        }
        System.out.println("---------------------------------------------------------------------------------------------");
    }


    public void addProduct(Product product) {
        products.add(product);
    }

}
