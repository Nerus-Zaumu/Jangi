package jangi.user.jangiuser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static javax.swing.JOptionPane.showMessageDialog;

public class MemberController extends DBInfo implements Initializable {
    @FXML
    public Button registerSwitch;
    @FXML
    public Button loanBack;
    @FXML
    public Button loginSwitch;
    @FXML Button contributeBtn;
    @FXML
    public Button moneySwitch;
    @FXML
    public Button dashboardSwitch;
    @FXML
    public Button loadSwitch;
    @FXML
    public Button sendMoney;
    @FXML
    public Button takeLoan;
    @FXML
    public Button addMoney;
    @FXML
    public Label nameTracker;
    @FXML
    public Scene scene;
    @FXML
    public Stage stage;
    @FXML
    public TextField username;
    public PasswordField userPassword;
    @FXML
    TextField savedAmount;
    public static String trackName;
    @FXML public static Text totalSavings;
    @FXML public static Text totalContributions;
    @FXML public static Text totalLoans;
    public static ArrayList<UserStructure> users = new ArrayList<>();


    public MemberController() {
    }

    public void registerNewUser(ActionEvent event) throws SQLException {
        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(REGISTER_NEW_USER)) {
            preparedStatement.setString(1, username.getText());
            preparedStatement.setString(2, userPassword.getText());

            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            preparedStatement.executeUpdate();
            updateUserOnLogin();
            //Switch to login
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    public void updateUserOnLogin() throws SQLException, IOException{
        Statement statement = null;
        Statement statement1 = null;
        Statement statement2 = null;
        Statement statement3 = null;
        Connection connection = null;
        Connection connection1 = null;
        Connection connection2 = null;
        Connection connection3 = null;
        try{
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            connection1 = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            connection2 = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            connection3 = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            statement = connection.createStatement();
            statement1 = connection.createStatement();
            statement2 = connection.createStatement();
            statement3 = connection.createStatement();
            String noConYet = "No contributions yet";
            String queryString = "UPDATE user SET contributions=0 +'" +"'"  + "WHERE contributions is NULL";
            String queryStringOne = "UPDATE user SET savings=0 +'" +"'" + "WHERE savings is NULL";
            String queryStringTwo = "UPDATE user SET loans=0 +'" +"'"  + "WHERE loans is NULL";
            String queryStringThree = "UPDATE user SET con_status=+'" + noConYet +"'" + "WHERE con_status is NULL";
            int rowsAffected = statement.executeUpdate(queryString);
            int rowsAffectedOne = statement1.executeUpdate(queryStringOne);
            int rowsAffectedTwo = statement2.executeUpdate(queryStringTwo);
            int rowsAffectedThree = statement3.executeUpdate(queryStringThree);
            System.out.println("Rows affected: " + rowsAffected);
            System.out.println("Rows affected: " + rowsAffectedOne);
            System.out.println("Rows affected: " + rowsAffectedTwo);
            System.out.println("Rows affected: " + rowsAffectedThree);
            System.out.println("Update Completed");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally {
            connectionCloser(statement, connection, statement1, connection1, statement2, connection2, statement3, connection3);
        }
    }

    static void connectionCloser(Statement statement, Connection connection, Statement statement1, Connection connection1, Statement statement2, Connection connection2, Statement statement3, Connection connection3) throws SQLException {
        if(statement != null){
            statement.close();
        }
        if(connection != null){
            connection.close();
        }
        if(statement1 != null){
            statement1.close();
        }
        if(connection1 != null){
            connection1.close();
        }
        if(statement2 != null){
            statement2.close();
        }
        if(connection2 != null){
            connection2.close();
        }
        if(statement3 != null){
            statement3.close();
        }
        if(connection3 != null){
            connection3.close();
        }
    }

    public void loginRegisteredUser(ActionEvent event) throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            Statement statement = connection.createStatement();
            String queryString = "SELECT * FROM user WHERE name='" + username.getText() + "'";
            ResultSet resultSet = statement.executeQuery(queryString);
            while (resultSet.next()) {
                String adminUsername = resultSet.getString("name");
                String adminPassword = resultSet.getString("password");
                if (Objects.equals(username.getText(), adminUsername) && Objects.equals(userPassword.getText(), adminPassword)) {
                    trackName = username.getText();
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dashboard.fxml")));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    System.out.println("Successfully logged in");
                } else {
                    return;
                }
            }
            statement.close();
        } catch (Exception exception) {
            System.out.println("An exception occurred!");
            System.out.println(exception.getMessage());
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;
        if (event.getSource() == dashboardSwitch) {
            stage = (Stage) dashboardSwitch.getScene().getWindow();
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        } else if (event.getSource() == loginSwitch) {
            stage = (Stage) loginSwitch.getScene().getWindow();
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("register.fxml")));
        } else if (event.getSource() == registerSwitch) {
            stage = (Stage) registerSwitch.getScene().getWindow();
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        } else if (event.getSource() == takeLoan) {
            stage = (Stage) takeLoan.getScene().getWindow();
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("take-loan.fxml")));
        } else if (event.getSource() == sendMoney) {
            stage = (Stage) sendMoney.getScene().getWindow();
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("send-receive-cash.fxml")));
        } else if (event.getSource() == addMoney) {
            stage = (Stage) addMoney.getScene().getWindow();
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("add-money.fxml")));
        } else if (event.getSource() == loanBack) {
            stage = (Stage) loanBack.getScene().getWindow();
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dashboard.fxml")));
        }
        else if (event.getSource() == contributeBtn) {
            stage = (Stage) contributeBtn.getScene().getWindow();
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("contribute.fxml")));
        }
        else {
            stage = (Stage) dashboardSwitch.getScene().getWindow();
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dashboard.fxml")));
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToDashboard(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dashboard.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void addMoney() throws SQLException {
        Statement statement = null;
        Connection connection = null;
        int tempAmount = Integer.parseInt(savedAmount.getText());
        try{
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            statement = connection.createStatement();
            System.out.println(trackName);
            String queryString = "UPDATE user SET savings=savings +'"  +tempAmount + "'" + "WHERE name='" + trackName + "'";
            int rowsAffected = statement.executeUpdate(queryString);
            System.out.println("Rows affected: " + rowsAffected);
            System.out.println("Update Completed");
            showMessageDialog(null, "Successful deposit of XAF" + savedAmount.getText());
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(statement != null){
                statement.close();
            }if(connection != null){
                connection.close();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
