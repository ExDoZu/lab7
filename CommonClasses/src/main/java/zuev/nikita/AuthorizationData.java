package zuev.nikita;

import java.io.Serializable;

public class AuthorizationData implements Serializable {
    private final String login;
    private final String password;
    public AuthorizationData(String login, String password){
        this.login = login;
        this.password=password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
