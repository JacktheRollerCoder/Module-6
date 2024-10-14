import java.sql.*;
import java.util.Scanner;

public class StaffManager {
    private static final String DB_URL = "jdbc:mysql://localhost/javabook"; // Change this to your DB URL
    private static final String USER = "scott"; // Change this to your DB username
    private static final String PASS = "tiger"; // Change this to your DB password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection conn = null;

        try {
            // Establish connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to the database.");

            while (true) {
                System.out.println("\nChoose an option:");
                System.out.println("1. View Staff");
                System.out.println("2. Insert Staff");
                System.out.println("3. Update Staff");
                System.out.println("4. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter Staff ID: ");
                        String viewId = scanner.nextLine();
                        viewStaff(conn, viewId);
                        break;
                    case 2:
                        insertStaff(conn, scanner);
                        break;
                    case 3:
                        updateStaff(conn, scanner);
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            scanner.close();
        }
    }

    private static void viewStaff(Connection conn, String id) {
        String query = "SELECT * FROM Staff WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Staff Details:");
                System.out.println("ID: " + rs.getString("id"));
                System.out.println("Last Name: " + rs.getString("lastName"));
                System.out.println("First Name: " + rs.getString("firstName"));
                System.out.println("MI: " + rs.getString("mi"));
                System.out.println("Address: " + rs.getString("address"));
                System.out.println("City: " + rs.getString("city"));
                System.out.println("State: " + rs.getString("state"));
                System.out.println("Telephone: " + rs.getString("telephone"));
                System.out.println("Email: " + rs.getString("email"));
            } else {
                System.out.println("No staff found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertStaff(Connection conn, Scanner scanner) {
        String insertSQL = "INSERT INTO Staff (id, lastName, firstName, mi, address, city, state, telephone, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            System.out.print("Enter Staff ID (9 chars): ");
            String id = scanner.nextLine();
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter MI: ");
            String mi = scanner.nextLine();
            System.out.print("Enter Address: ");
            String address = scanner.nextLine();
            System.out.print("Enter City: ");
            String city = scanner.nextLine();
            System.out.print("Enter State (2 chars): ");
            String state = scanner.nextLine();
            System.out.print("Enter Telephone (10 chars): ");
            String telephone = scanner.nextLine();
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();

            pstmt.setString(1, id);
            pstmt.setString(2, lastName);
            pstmt.setString(3, firstName);
            pstmt.setString(4, mi);
            pstmt.setString(5, address);
            pstmt.setString(6, city);
            pstmt.setString(7, state);
            pstmt.setString(8, telephone);
            pstmt.setString(9, email);
            pstmt.executeUpdate();

            System.out.println("Staff inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateStaff(Connection conn, Scanner scanner) {
        String updateSQL = "UPDATE Staff SET lastName = ?, firstName = ?, mi = ?, address = ?, city = ?, state = ?, telephone = ?, email = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            System.out.print("Enter Staff ID to update: ");
            String id = scanner.nextLine();
            System.out.print("Enter New Last Name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter New First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter New MI: ");
            String mi = scanner.nextLine();
            System.out.print("Enter New Address: ");
            String address = scanner.nextLine();
            System.out.print("Enter New City: ");
            String city = scanner.nextLine();
            System.out.print("Enter New State: ");
            String state = scanner.nextLine();
            System.out.print("Enter New Telephone: ");
            String telephone = scanner.nextLine();
            System.out.print("Enter New Email: ");
            String email = scanner.nextLine();

            pstmt.setString(1, lastName);
            pstmt.setString(2, firstName);
            pstmt.setString(3, mi);
            pstmt.setString(4, address);
            pstmt.setString(5, city);
            pstmt.setString(6, state);
            pstmt.setString(7, telephone);
            pstmt.setString(8, email);
            pstmt.setString(9, id);
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Staff updated successfully.");
            } else {
                System.out.println("No staff found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
