package jangi.user.jangiuser;

public class UserStructure {
    String name;
    int userID;
    int savings;
    int loans;
    int contribution;


    public UserStructure(String name, int userID){
        this.name = name;
        this.userID = userID;
    }

    public UserStructure(int userID, String name, int savings, int loans, int contributions){
        this.name = name;
        this.userID = userID;
        this.savings = savings;
        this.loans = loans;
        this.contribution = contributions;
    }

}
