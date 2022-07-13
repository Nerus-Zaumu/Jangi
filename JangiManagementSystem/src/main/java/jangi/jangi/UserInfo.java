package jangi.jangi;

public class UserInfo {
    public int userId;
    public String name;
    public int savings;
    public int loans;
    public int contributions;

    public UserInfo(int userId, String name, int savings, int loans){
        this.userId = userId;
        this.name = name;
        this.savings = savings;
        this.loans = loans;
    }

    public UserInfo(int userId, String name, int savings, int loans, int contributions){
        this.name = name;
        this.userId = userId;
        this.savings = savings;
        this.loans = loans;
        this.contributions = contributions;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public int getSavings() {
        return savings;
    }

    public int getLoans() {
        return loans;
    }

    public int getContributions() {
        return contributions;
    }
}
