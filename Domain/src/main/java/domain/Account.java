package domain;

import java.io.Serializable;

public class Account extends Entity<String> implements Serializable {
    private String password;
    private String name;

    public Account(String username, String password, String name) {
        super(username);
        this.name=name;
        this.password = password;
    }
    public Account(String username, String password) {
        super(username);
        this.name="";
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername(){
        return super.getId();
    }

    @Override
    public String toString() {
        return getUsername();
    }
}
