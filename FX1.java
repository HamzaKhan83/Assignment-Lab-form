import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class FX1 extends Application {

    private ArrayList<User> userList;
    private File file;
    private Stage primaryStage;

    public FX1() {
        userList = new ArrayList<>();
        file = new File("UserData.txt");
        loadUsersFromFile();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        Image logo = new Image("logo.jfif");
        ImageView imageView = new ImageView(logo);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        StackPane imagePane = new StackPane(imageView);

        Label userNameLabel = new Label("Username:");
        TextField userNameField = new TextField();

        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField();

        Button loginButton = new Button("Login");
        Label resultLabel = new Label();

        loginButton.setOnAction(e -> {
            String username = userNameField.getText();
            String password = passwordField.getText();
            User user = findUser(username, password);

            if (user != null) {
                showWelcomeScene(user.getUserName());
            } else {
                resultLabel.setText("Invalid username or password.");
            }
        });

        VBox layout = new VBox(10, imagePane, userNameLabel, userNameField, passwordLabel, passwordField, loginButton, resultLabel);
        Scene loginScene = new Scene(layout, 400, 350);

        primaryStage.setTitle("Login Form");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void loadUsersFromFile() {
        try {
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 2) {
                        userList.add(new User(data[0], data[1]));
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            System.out.println("Error loading users from file.");
        }

        addPredefinedUsers();
    }

    private void addPredefinedUsers() {
        if (!userExists("hamza")) {
            userList.add(new User("hamza", "123"));
        }
        if (!userExists("haider")) {
            userList.add(new User("haider", "1234"));
        }
        if (!userExists("ali")) {
            userList.add(new User("ali", "4567"));
        }

        saveUsersToFile();
    }

    private boolean userExists(String username) {
        for (User user : userList) {
            if (user.getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private User findUser(String username, String password) {
        for (User user : userList) {
            if (user.getUserName().equals(username) && user.getUserPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private void saveUsersToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (User user : userList) {
                writer.write(user.getUserName() + "," + user.getUserPassword());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving users to file.");
        }
    }

    private void showWelcomeScene(String username) {
        Label welcomeLabel = new Label("Welcome, " + username + "!");
        VBox welcomeLayout = new VBox(20, welcomeLabel);
        Scene welcomeScene = new Scene(welcomeLayout, 400, 200);

        primaryStage.setScene(welcomeScene);
        primaryStage.setTitle("Welcome Page");
    }

    public static class User {
        private String userName;
        private String userPassword;

        public User(String userName, String userPassword) {
            this.userName = userName;
            this.userPassword = userPassword;
        }

        public String getUserName() {
            return userName;
        }

        public String getUserPassword() {
            return userPassword;
        }
    }
}
