package jangi.jangi;

public class DBInfo {
    public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/jangi_management_system";
    public static final String DATABASE_USERNAME = "root";
    public static final String GET_ALL_USERS = "SELECT * FROM user";
    public static final String DATABASE_PASSWORD = ".Kvrag7C2yFinXJ";
    public static final String insertInterestRateQuery = "INSERT INTO interest_rate (rate, loan_range) VALUES (?, ?)";
    public static final String usersQuery = "SELECT userID, name, savings, loans FROM user";
}
