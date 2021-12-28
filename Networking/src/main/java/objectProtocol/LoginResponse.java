package objectProtocol;

import domain.Account;

public class LoginResponse implements Response{
    private Account user;

    public LoginResponse(Account user) {
        this.user = user;
    }

    public Account getUser() {
        return user;
    }
}
