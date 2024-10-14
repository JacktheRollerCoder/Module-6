import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Random;

public class BatchUpdatePerformance extends Application {
    private TextField tfURL = new TextField();
    private TextField tfDriver = new TextField();
    private TextField tfUsername = new TextField();
    private PasswordField pfPassword = new PasswordField();
    private Button btConnect = new Button("Connect to Database");
    private Button btInsertBatch = new Button("Insert with Batch");
    private Button btInsertSingle = new Button("Insert Single");
    private Label lblStatus = new Label();

    private Connection connection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(new Label("Database URL:"), 0, 0);
        grid.add(tfURL, 1, 0);
        grid.add(new Label("JDBC Driver:"), 0, 1);
        grid.add(tfDriver, 1, 1);
        grid.add(new Label("Username:"), 0, 2);
        grid.add(tfUsername, 1, 2);
        grid.add(new Label("Password:"), 0, 3);
        grid.add(pfPassword, 1, 3);
        grid.add(btConnect, 1, 4);
        grid.add(btInsertBatch, 1, 5);
        grid.add(btInsertSingle, 1, 6);
        grid.add(lblStatus, 1, 7);

        btConnect.setOnAction(e -> connectToDatabase());
        btInsertBatch.setOnAction(e -> insertWithBatch());
        btInsertSingle.setOnAction(e -> insertSingle());

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Batch Update Performance");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void connectToDatabase() {
        try {
            Class.forName(tfDriver.getText().trim());
            connection = DriverManager.getConnection(tfURL.getText().trim(), tfUsername.getText().trim(), pfPassword.getText().trim());
            lblStatus.setText("Connected to the database.");
        } catch (Exception ex) {
            lblStatus.setText("Connection failed: " + ex.getMessage());
        }
    }

    private void insertWithBatch() {
        if (connection == null) {
            lblStatus.setText("Connect to the database first.");
            return;
        }

        long startTime = System.currentTimeMillis();
        try {
            String sqlInsert = "INSERT INTO Temp (num1, num2, num3) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);

            for (int i = 0; i < 1000; i++) {
                preparedStatement.setDouble(1, Math.random());
                preparedStatement.setDouble(2, Math.random());
                preparedStatement.setDouble(3, Math.random());
                preparedStatement.addBatch();

                if (i % 100 == 0) { // Execute every 100 records
                    preparedStatement.executeBatch();
                }
            }
            preparedStatement.executeBatch(); // Execute remaining records
            preparedStatement.close();
        } catch (SQLException e) {
            lblStatus.setText("Batch Insert Error: " + e.getMessage());
            return;
        }
        long endTime = System.currentTimeMillis();
        lblStatus.setText("Inserted 1000 records with batch in " + (endTime - startTime) + " ms.");
    }

    private void insertSingle() {
        if (connection == null) {
            lblStatus.setText("Connect to the database first.");
            return;
        }

        long startTime = System.currentTimeMillis();
        try {
            String sqlInsert = "INSERT INTO Temp (num1, num2, num3) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);

            for (int i = 0; i < 1000; i++) {
                preparedStatement.setDouble(1, Math.random());
                preparedStatement.setDouble(2, Math.random());
                preparedStatement.setDouble(3, Math.random());
                preparedStatement.executeUpdate(); // Execute each insert
            }
            preparedStatement.close();
        } catch (SQLException e) {
            lblStatus.setText("Single Insert Error: " + e.getMessage());
            return;
        }
        long endTime = System.currentTimeMillis();
        lblStatus.setText("Inserted 1000 records without batch in " + (endTime - startTime) + " ms.");
    }
}
