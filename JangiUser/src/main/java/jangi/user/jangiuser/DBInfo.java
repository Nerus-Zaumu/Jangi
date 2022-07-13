package jangi.user.jangiuser;

public class DBInfo {
    public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/jangi_management_system";
    public static final String DATABASE_USERNAME = "root";
    public static final String DATABASE_PASSWORD = ".Kvrag7C2yFinXJ";
    public static final String REGISTER_NEW_USER = "INSERT INTO user (name, password) VALUES (?, ?)";
    public static final String GET_ALL_USERS = "SELECT * FROM user";
}
