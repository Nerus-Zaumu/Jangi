package jangi.jangi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static jangi.jangi.DBInfo.GET_ALL_USERS;

public class InfoList extends AdminController implements Initializable {

    @FXML
    public Label totalSavings;
    @FXML public Label totalContributions;
    @FXML public Label totalLoans;
    public static int actualSavings = 0;
    public static int actualContributions = 0;
    public static int actualLoans = 0;
    public static ArrayList<UserInfo> users = new ArrayList<>();
    @FXML
    public TableView<UserInfo> tableView;
    @FXML public TableColumn<UserInfo, Integer> UID;
    @FXML public TableColumn<UserInfo, String> UNAME;
    @FXML public TableColumn<UserInfo, Integer> USAVINGS;
    @FXML public TableColumn<UserInfo, Integer> ULOANS;

    public static ObservableList<UserInfo> allUsers = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateUsers();
    }

    public void setAllUsers(){
        allUsers.clear();
        generateUsers();
    }

    private void generateUsers() {
        try{
            Connection connection = DriverManager.getConnection(DBInfo.DATABASE_URL, DBInfo.DATABASE_USERNAME, DBInfo.DATABASE_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(DBInfo.usersQuery);

            while (resultSet.next()) {
                UserInfo currentUser = new UserInfo(0, "name", 0, 0);
                try{
                    int userID = resultSet.getInt("userID");
                    String name = resultSet.getString("name");
                    int savings = resultSet.getInt("savings");
                    int loans = resultSet.getInt("loans");
                    currentUser.userId = userID;
                    currentUser.name = name;
                    currentUser.savings = savings;
                    currentUser.loans = loans;
                    allUsers.add(currentUser);
                }
                catch(Exception exception) {
                    System.out.println(exception.getMessage());
                }
            }
            statement.close();
        }
        catch(Exception exception){
            System.out.println("An exception occurred!");
            System.out.println(exception.getMessage());
        }
        UID.setCellValueFactory(new PropertyValueFactory<UserInfo, Integer>("userId"));
        UNAME.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("name"));
        USAVINGS.setCellValueFactory(new PropertyValueFactory<UserInfo, Integer>("savings"));
        ULOANS.setCellValueFactory(new PropertyValueFactory<UserInfo, Integer>("loans"));
        tableView.setItems(allUsers);
    }

    public void setDashboardDetails() {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ALL_USERS);
            while (resultSet.next()) {
                UserInfo tempUser = new UserInfo(0, "Boss Yimnai", 0, 0, 0);
                String username = resultSet.getString("name");
                int userID = resultSet.getInt("userID");
                int savings = resultSet.getInt("savings");
                int loans = resultSet.getInt("loans");
                int contributions = resultSet.getInt("contributions");
                tempUser.name = username;
                tempUser.userId = userID;
                tempUser.contributions = contributions;
                tempUser.loans = loans;
                tempUser.savings = savings;
                users.add(tempUser);
            }
            statement.close();
        } catch (Exception exception) {
            System.out.println("An exception occurred!");
            System.out.println(exception.getMessage());
        }
        for(UserInfo user: users){
            actualContributions += user.contributions;
            actualSavings += user.savings;
            actualLoans += user.loans;
        }
        totalSavings.setText(String.valueOf(actualSavings));
        totalContributions.setText(String.valueOf(actualContributions));
        totalLoans.setText(String.valueOf(actualLoans));
        System.out.println(totalSavings);
        System.out.println(totalContributions);
        System.out.println(totalLoans);
    }

}
