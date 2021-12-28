package objectProtocol;

import domain.Account;


public class LoginRequest implements Request {
    private Account user;

    public LoginRequest(Account user) {
        this.user = user;
    }

    public Account getUser() {
        return user;
    }
}
