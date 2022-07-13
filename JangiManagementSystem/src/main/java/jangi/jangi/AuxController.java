package jangi.jangi;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
public class AuxController extends AdminController implements Initializable{
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/jangi_management_system";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = ".Kvrag7C2yFinXJ";
    private static final String insertInterestRateQuery = "INSERT INTO interest_rate (rate, loan_range) VALUES (?, ?)";
    @FXML
    public ComboBox<Integer> loanRange;
     @FXML public ComboBox<Double> interestRate;
    @FXML public TableView<UserInfo> tableView;
    private final Integer[] loans = {10000, 20000, 30000, 40000, 50000};
    private final Double[] rates = {0.5, 1.0, 1.5, 2.0, 2.2};
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loanRange.getItems().clear();
        interestRate.getItems().clear();
        loanRange.getItems().addAll(loans);
        interestRate.getItems().addAll(rates);
    }

    //Insert interest rate into database
    public void insertInterestRate() throws SQLException {
        System.out.println(loanRange.getSelectionModel().getSelectedItem());
        System.out.println(interestRate.getSelectionModel().getSelectedItem());
        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(insertInterestRateQuery)) {
            preparedStatement.setDouble(1, interestRate.getValue());
            preparedStatement.setInt(2, loanRange.getValue());

            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
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

}

