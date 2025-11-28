package inventory;

import java.sql.*;
import java.util.Scanner;

public class InventoryManagement {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/inventorydb?useSSL=false&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "deva";

    static Connection conn = null;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            while (true) {
          
                System.out.println("Inventory Management System");
             
                System.out.println(" 1. Add Product");
                System.out.println(" 2. View Products");
                System.out.println(" 3. Update Product");
                System.out.println(" 4. Delete Product");
                System.out.println(" 5. Exit");
             
                System.out.print("Enter your choice: ");
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1: addProduct(); break;
                    case 2: viewProducts(); break;
                    case 3: updateProduct(); break;
                    case 4: deleteProduct(); break;
                    case 5:
                        System.out.println("Exiting...");
                        conn.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void addProduct() {
        try {
            System.out.print("Enter Product ID: ");
            int id = Integer.parseInt(sc.nextLine());
            System.out.print("Enter Product Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Quantity: ");
            int qty = Integer.parseInt(sc.nextLine());
            System.out.print("Enter Price: ");
            double price = Double.parseDouble(sc.nextLine());

            String sql = "INSERT INTO products(id, name, quantity, price) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setInt(3, qty);
            stmt.setDouble(4, price);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("✅ Product added successfully!");
            else System.out.println("❌ Failed to add product.");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("❌ Error: Product ID already exists!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void viewProducts() {
        try {
            String sql = "SELECT * FROM products";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n============================================================");
            System.out.printf("| %-5s | %-20s | %-8s | %-10s |\n", "ID", "Name", "Quantity", "Price");
            System.out.println("============================================================");

            boolean found = false;
            while (rs.next()) {
                found = true;
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int qty = rs.getInt("quantity");
                double price = rs.getDouble("price");

                System.out.printf("| %-5d | %-20s | %-8d | %-10.2f |\n", id, name, qty, price);
            }

            if (!found) {
                System.out.println("|                    No products found!                    |");
            }

            System.out.println("============================================================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void updateProduct() {
        try {
            System.out.print("Enter Product ID to update: ");
            int id = Integer.parseInt(sc.nextLine());
            System.out.print("Enter new quantity: ");
            int qty = Integer.parseInt(sc.nextLine());
            System.out.print("Enter new price: ");
            double price = Double.parseDouble(sc.nextLine());

            String sql = "UPDATE products SET quantity = ?, price = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, qty);
            stmt.setDouble(2, price);
            stmt.setInt(3, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("✅ Product updated!");
            else System.out.println("❌ Product ID not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void deleteProduct() {
        try {
            System.out.print("Enter Product ID to delete: ");
            int id = Integer.parseInt(sc.nextLine());

            String sql = "DELETE FROM products WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("✅ Product deleted!");
            else System.out.println("❌ Product ID not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
