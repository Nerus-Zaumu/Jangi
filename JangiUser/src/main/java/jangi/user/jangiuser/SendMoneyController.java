package jangi.user.jangiuser;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javax.swing.JOptionPane.showMessageDialog;

public class SendMoneyController extends MemberController implements Initializable {
    @FXML public ComboBox<String> userCombo;
    @FXML public TextField transferAmount;
    public static ArrayList<UserStructure> users = new ArrayList<>();
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

    public void makeTransfer() throws SQLException, IOException {
        try{
            Connection senderConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            Connection receiverConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            Statement receiverConnectionStatement = receiverConnection.createStatement();
            Statement senderConnectionStatement = senderConnection.createStatement();
            String senderQueryString = "SELECT * FROM user WHERE name='" + trackName + "'";
            String receiverQueryString = "SELECT * FROM user WHERE name='" + userCombo.getValue() + "'";
            ResultSet senderResultSet = senderConnectionStatement.executeQuery(senderQueryString);
            ResultSet receiverResultSet = receiverConnectionStatement.executeQuery(receiverQueryString);
            while (senderResultSet.next() && receiverResultSet.next()) {
                int senderTotalSavings = senderResultSet.getInt("savings");
                int receiverTotalSavings = receiverResultSet.getInt("savings");
                if (senderTotalSavings < Integer.parseInt(transferAmount.getText())) {
                    showMessageDialog(null, "Oops!!! You do not have enough funds to make this transfer");
                } else {
                    refresh();
                    showMessageDialog(null, "Successfully transferred " + transferAmount.getText() + " to " + userCombo.getValue() + "!");

                }
            }
            senderConnectionStatement.close();
        }
        catch (Exception exception) {
            System.out.println("An exception occurred!");
            System.out.println(exception.getMessage());

        }
    }

    public void refresh() throws SQLException {
        Statement updateReceiverAccountStatement = null;
        Statement updateSenderAccountStatement = null;
        Connection updateReceiverAccountConnection = null;
        Connection updateSenderAccountConnection = null;
        try{
            updateReceiverAccountConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            updateSenderAccountConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            updateReceiverAccountStatement = updateReceiverAccountConnection.createStatement();
            updateSenderAccountStatement = updateSenderAccountConnection.createStatement();
            String updatedReceiverQueryString = "UPDATE user SET savings=savings + '" + Integer.parseInt(transferAmount.getText()) + "'" + "WHERE name='" + userCombo.getValue() + "'";
            String updatedSenderQueryString = "UPDATE user SET savings=savings - '" + Integer.parseInt(transferAmount.getText()) + "'" + "WHERE name='" + trackName + "'";
            int receiverRowsAffected = updateReceiverAccountStatement.executeUpdate(updatedReceiverQueryString);
            int senderRowsAffected = updateSenderAccountStatement.executeUpdate(updatedSenderQueryString);
            System.out.println("Rows affected: " + receiverRowsAffected);
            System.out.println("Rows affected: " + senderRowsAffected);
            System.out.println("Update Completed");
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            ContributeController.connectionCloser(updateReceiverAccountStatement, updateReceiverAccountConnection, updateSenderAccountStatement, updateSenderAccountConnection);
        }
    }
}
