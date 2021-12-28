package objectProtocol;


import domain.Account;
import domain.Employee;

public class LogoutRequest implements Request {
    private Account user;

    public LogoutRequest(Account user) {
        this.user = user;
    }

    public Account getUser() {
        return user;
    }
}
