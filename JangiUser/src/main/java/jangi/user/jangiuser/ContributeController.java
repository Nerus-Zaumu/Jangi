package jangi.user.jangiuser;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.lang.reflect.Array;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static javax.swing.JOptionPane.showMessageDialog;

public class ContributeController extends MemberController implements Initializable {
    @FXML public ComboBox<String> type;
    @FXML public ComboBox<Integer> amount;
   String[] contTypes = {"Death of member", "Death of member Spouse", "Illness Contribution", "Parents In Laws"};
   Integer[] amountTypes = {2000, 5000, 10000, 25000};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        type.getItems().clear();
        amount.getItems().clear();
        type.getItems().addAll(contTypes);
        amount.getItems().addAll(amountTypes);
    }

    public void insertContribution() throws SQLException {
        Statement statement = null;
        Connection connection = null;
        Statement statement1 = null;
        Connection connection1 = null;
        int tempAmount = amount.getValue();
        try{
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            connection1 = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            statement = connection.createStatement();
            statement1 = connection1.createStatement();

            String typeVal = "--".concat(type.getValue());
            System.out.println(typeVal);

            String queryString = "UPDATE user SET contributions=contributions +'"  + tempAmount + "'" + "WHERE name='" + trackName + "'";
            String queryStringStatus = "UPDATE user SET con_status=CONCAT(COALESCE(con_status , '')," + type.getValue() + ")" + "WHERE name='" + trackName + "'";
            int rowsAffected = statement.executeUpdate(queryString);
            int otherRowsAffected = statement1.executeUpdate(queryStringStatus);
            System.out.println("Rows affected: " + rowsAffected);
            System.out.println("Rows affected: " + otherRowsAffected);

            System.out.println("Update Completed");
            showMessageDialog(null, "Successful deposit of XAF" + amount.getValue());
        }catch(Exception e){
//            e.printStackTrace();
            System.out.println(e.getMessage());
        }finally {
            connectionCloser(statement, connection, statement1, connection1);
        }
    }

    static void connectionCloser(Statement statement, Connection connection, Statement statement1, Connection connection1) throws SQLException {
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
    }
}
