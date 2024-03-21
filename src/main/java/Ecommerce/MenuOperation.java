package Ecommerce;

import Ecommerce.db.CreateDBConnection;
import Ecommerce.order.Order;
import Ecommerce.order.OrderManager;
import Ecommerce.product.Product;
import Ecommerce.product.ProductCatalog;
import Ecommerce.shoppingcart.ShoppingCart;
import Ecommerce.user.User;
import Ecommerce.user.UserService;

import java.util.Scanner;

public class MenuOperation {

    private CreateDBConnection dbConnection;
    private ProductCatalog productCatalog;
    private ShoppingCart shoppingCart;
    private UserService userService;
    private OrderManager orderManager;

    private User currentUser;

    public MenuOperation() {
        dbConnection = new CreateDBConnection();
        productCatalog = new ProductCatalog();
        shoppingCart = new ShoppingCart();
        userService = new UserService();
        orderManager = new OrderManager();
    }

    Scanner sc = new Scanner(System.in);
    Scanner scanner = new Scanner(System.in);

    public void showMainMenu() {
        String choice = "y";
        while (choice.equals("y")) {
            System.out.println("Main Menu:");
            if (currentUser == null) {
                System.out.println("1. Login");
                System.out.println("2. Register");
            } else {
                System.out.println("3. Product Menu");
            }
            System.out.println("4. Order History");
            System.out.println("5. Exit");

            System.out.println("Enter Your choice:");
            int menuChoice = scanner.nextInt();
            scanner.nextLine();

            switch (menuChoice) {
                case 1:
                    loginUser();
                    break;
                case 2:
                    registerUser();
                    break;
                case 3:
                    if (currentUser != null) {
                        showProductMenu();
                    } else {
                        System.out.println("Please login or register to access the Product Menu.");
                    }
                    break;
                case 4:
                    if (currentUser != null) {
                        orderManager.displayOrderHistory(currentUser.getUserId());
                    } else {
                        System.out.println("Please login to view order history.");
                    }
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice");
            }

            System.out.println("Do you want to continue? press y/n");
            choice = scanner.nextLine();
        }
    }

    private void loginUser() {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        currentUser = userService.authenticateUser(username, password);
        if (currentUser != null) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password");
        }
    }

    private void registerUser() {
        System.out.println("Enter User Name to Insert");
        String username = scanner.next();
        System.out.println("Enter Password");
        String password = scanner.next();
        System.out.println("Enter Email");
        String email = scanner.next();
        scanner.nextLine();

        User user = new User(username, password, email);
        boolean success = userService.registerUser(user);
        if (success) {
            System.out.println("User Added");

            // Automatically log in the user after registration
            currentUser = user;
        } else {
            System.out.println("Failed to add user");
        }
    }
    private void showProductMenu() {
        if (currentUser == null) {
            System.out.println("Please login or register to access the Product Menu.");
            return;
        }

        String ch = "y";
        while (ch.equals("y")) {
            System.out.println("Product Menu:");
            System.out.println("1. View Products");
            System.out.println("2. Add Product to Cart");
            System.out.println("3. Remove Item From Cart");
            System.out.println("4. Clear your Cart");
            System.out.println("5. View Cart");
            System.out.println("6. Place Order");
            System.out.println("7. Back to Main Menu");
            System.out.println("Enter Your choice:");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    productCatalog.displayProducts();
                    break;
                case 2:
                    System.out.println("Enter the name of the product to add:");
                    String productName = scanner.nextLine();
                    Product selectedProduct = productCatalog.getProductByName(productName);
                    if (selectedProduct != null) {
                        shoppingCart.addToCart(selectedProduct, currentUser.getUserId());
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;

                case 3:
                    System.out.println("Enter the name of the product to remove:");
                    String productToRemoveName = scanner.nextLine();
                    Product productToRemove = shoppingCart.getProductByName(productToRemoveName);
                    if (productToRemove != null) {
                        shoppingCart.removeItem(productToRemove, currentUser.getUserId());
                    } else {
                        System.out.println(productToRemoveName + " is not in the cart.");
                    }
                    break;
                case 4:
                    shoppingCart.clearCart(currentUser.getUserId());
                    break;
                case 5:
                    shoppingCart.displayCart(currentUser.getUserId());
                    break;
                case 6:
                    orderManager.placeOrder(currentUser, shoppingCart.getItems());
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice");
            }

            System.out.println("Do you want to continue with Product Menu? Press y/n");
            ch = sc.nextLine();
        }
    }

}
