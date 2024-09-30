import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FinalProject2 extends Application {
    private List<FriendBirthday> friends = new ArrayList<>();
    private TextField nameField;
    private TextField birthdayField;
    private TextArea summaryArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Birthday Manager");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Input fields
        nameField = new TextField();
        birthdayField = new TextField();
        summaryArea = new TextArea();
        summaryArea.setEditable(false);

        // Labels
        grid.add(new Label("Friend's Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Birthday (yyyy-mm-dd):"), 0, 1);
        grid.add(birthdayField, 1, 1);

        // Buttons
        Button addButton = new Button("Add Birthday");
        Button showButton = new Button("Show Birthdays");

        grid.add(addButton, 0, 2);
        grid.add(showButton, 1, 2);
        grid.add(new Label("Summary:"), 0, 3);
        grid.add(summaryArea, 0, 4, 2, 1); // span 2 columns

        // Button actions
        addButton.setOnAction(e -> addBirthday());
        showButton.setOnAction(e -> showBirthdays());

        // Set up the scene
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addBirthday() {
        String friendName = nameField.getText();
        String birthdayInput = birthdayField.getText();
        LocalDate birthday;

        try {
            birthday = LocalDate.parse(birthdayInput, DateTimeFormatter.ISO_LOCAL_DATE);
            friends.add(new FriendBirthday(friendName, birthday));
            showAlert("Success", "Birthday added for " + friendName);
            nameField.clear();
            birthdayField.clear();
        } catch (DateTimeParseException e) {
            showAlert("Error", "Invalid date format. Use yyyy-mm-dd.");
        }
    }

    private void showBirthdays() {
        StringBuilder summary = new StringBuilder();
        LocalDate today = LocalDate.now();

        summary.append("--- Summary of Friends' Birthdays ---\n");
        for (FriendBirthday friend : friends) {
            LocalDate nextBirthday = friend.getBirthday().withYear(today.getYear());

            if (nextBirthday.isEqual(today)){
                summary.append("Happy Birthday to ").append(friend.getFriendName()).append("! Go celebrate!\n");
            }
            if (nextBirthday.isBefore(today)) {
                nextBirthday = nextBirthday.plusYears(1);
            }

            long daysUntilBirthday = java.time.temporal.ChronoUnit.DAYS.between(today, nextBirthday);
            summary.append(friend.getFriendName()).append("'s birthday is in ")
                   .append(daysUntilBirthday).append(" days on ")
                   .append(nextBirthday).append(".\n");
        }

        summaryArea.setText(summary.toString());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

class FriendBirthday {
    private String friendName;
    private LocalDate birthday;

    public FriendBirthday(String friendName, LocalDate birthday) {
        this.friendName = friendName;
        this.birthday = birthday;
    }

    public String getFriendName() {
        return friendName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }
}
