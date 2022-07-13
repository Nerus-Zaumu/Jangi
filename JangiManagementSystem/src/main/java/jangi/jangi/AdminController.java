package jangi.jangi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;


import static jangi.jangi.DBInfo.GET_ALL_USERS;
import static javax.swing.JOptionPane.showMessageDialog;

public class AdminController {

    public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/jangi_management_system";
    public static final String DATABASE_USERNAME = "root";
    public static final String DATABASE_PASSWORD = ".Kvrag7C2yFinXJ";
    private static final String adminLoginQuery = "SELECT * FROM Admin";
    @FXML
    public Label error_message;
    @FXML
    public TextField adminName;
    @FXML
    public PasswordField loginPassword;
    @FXML
    public Scene scene;
    @FXML
    public Stage stage;
    @FXML public Button interestBtn;
    @FXML public Button toInterestBtn;
    @FXML public Button toInfoBtn;

    //Configuring interest rates imports

    public void login(ActionEvent event) throws SQLException{
        try{
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(adminLoginQuery);

            while (resultSet.next()) {
                String adminUsername = resultSet.getString("username");
                String adminPassword = resultSet.getString("password");
                System.out.println(adminUsername);
                System.out.println(adminPassword);
                if(Objects.equals(adminName.getText(), adminUsername) && Objects.equals(loginPassword.getText(), adminPassword)){
                    showMessageDialog(null,"Hello " + adminUsername + ". " + "You have successfully logged in!");
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dashboard.fxml")));
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    System.out.println("Successfully Logged in");
                }
                else {
                    error_message.setText("Wrong credentials. Check again!");
                    showMessageDialog(null, "Wrong Credentials. Try again");
                }
            }
            statement.close();
        }
        catch(Exception exception){
            System.out.println("An exception occurred!");
            System.out.println(exception.getMessage());
        }
    }

    @FXML
    public void handleButtonAction (ActionEvent event) throws Exception {
        Stage stage;
        Parent root;

        if(event.getSource()==toInterestBtn){
            stage = (Stage) toInterestBtn.getScene().getWindow();
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("configure-interest.fxml")));
        }
        else if(event.getSource()==toInfoBtn){
            stage = (Stage) toInfoBtn.getScene().getWindow();
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user-info.fxml")));
        }
        else{
            stage = (Stage) interestBtn.getScene().getWindow();
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dashboard.fxml")));
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToDashboard(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dashboard.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

