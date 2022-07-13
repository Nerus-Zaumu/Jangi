package jangi.user.jangiuser;

import static javax.swing.JOptionPane.showMessageDialog;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static jangi.user.jangiuser.DBInfo.*;

public class AuxController extends MemberController implements Initializable {

    @FXML
    public Scene scene;
    @FXML
    public static int toto;
    @FXML
    public TextField loanAmount;
    @FXML
    ComboBox<String> userCombo;
    public static String borrowerName;
    public ArrayList<UserStructure> users = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ALL_USERS);
            while (resultSet.next()) {
                UserStructure tempUser = new UserStructure("BossMan", 0);
                String username = resultSet.getString("name");
                int userID = resultSet.getInt("userID");
                tempUser.name = username;
                tempUser.userID = userID;
                users.add(tempUser);
            }
            statement.close();
        } catch (Exception exception) {
            System.out.println("An exception occurred!");
            System.out.println(exception.getMessage());
        }
        userCombo.getItems().clear();
        for (UserStructure user : users) {
            userCombo.getItems().add(user.name);
        }
    }

    public void takeLoan(ActionEvent event) throws SQLException, IOException {
       try{
           Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
           Connection sureteeConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
           Connection totalSavings = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

           Statement sureteeStatement = sureteeConnection.createStatement();
           Statement statement = connection.createStatement();
           Statement totalStmt = totalSavings.createStatement();
           String myQuery = "SELECT savings FROM user";
           String queryString = "SELECT * FROM user WHERE name='" + trackName + "'";
           String sureteeString = "SELECT * FROM user WHERE name='" + userCombo.getValue() + "'";
           ResultSet resultSet = statement.executeQuery(queryString);
           ResultSet sureSet = sureteeStatement.executeQuery(sureteeString);
           ResultSet savingsSet = totalStmt.executeQuery(myQuery);
           while (resultSet.next() && sureSet.next() && savingsSet.next()) {
               int s = savingsSet.getInt("savings");
               toto += s;
               borrowerName = resultSet.getString("name");
               int adminSavings = resultSet.getInt("savings");
               int sureteeSavings = sureSet.getInt("savings");
               if (0.75*Integer.parseInt(loanAmount.getText()) > (adminSavings + sureteeSavings)) {
                   showMessageDialog(null, "Not eligible to take loan");
               } else {
                   toto -= Integer.parseInt(loanAmount.getText());
                   refresh();

               }
           }
           statement.close();
       }
        catch (Exception exception) {
            System.out.println("An exception occurred!");
            System.out.println(exception.getMessage());

        }
    }

    public void refresh() throws SQLException {
        Statement updateBorrowerStatement = null;
        Connection updateBorrowerConnection = null;
        Statement updateBorrowerLoanStatement = null;
        Connection updateBorrowerLoanConnection = null;
        try{
            updateBorrowerConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            updateBorrowerStatement = updateBorrowerConnection.createStatement();
            updateBorrowerLoanConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            updateBorrowerLoanStatement = updateBorrowerLoanConnection.createStatement();

            String borrowerSavingsUpdateString = "UPDATE user SET savings=savings +'" + Integer.parseInt(loanAmount.getText()) + "'" + "WHERE name='" + borrowerName + "'";
            String borrowerLoansUpdateString = "UPDATE user SET loans=loans +'" + Integer.parseInt(loanAmount.getText()) + "'" + "WHERE name='" + borrowerName + "'";
            int borrowerRowsAffected = updateBorrowerLoanStatement.executeUpdate(borrowerSavingsUpdateString);
            int borrowerLoansAffected = updateBorrowerStatement.executeUpdate(borrowerLoansUpdateString);
            System.out.println("Rows affected: " + borrowerRowsAffected);
            System.out.println("Rows affected: " + borrowerLoansAffected);
            System.out.println("Update Completed");
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            ContributeController.connectionCloser(updateBorrowerLoanStatement, updateBorrowerLoanConnection, updateBorrowerStatement, updateBorrowerConnection);
        }
    }
}
