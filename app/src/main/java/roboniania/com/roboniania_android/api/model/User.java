package roboniania.com.roboniania_android.api.model;

import java.util.List;

/**
 * Created by Mateusz on 05.05.2016.
 */
public class User {

    private String userId, login, password, token, createed;
    private List<Robot> robots;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreateed() {
        return createed;
    }

    public void setCreateed(String createed) {
        this.createed = createed;
    }

    public List<Robot> getRobots() {
        return robots;
    }

    public void setRobots(List<Robot> robots) {
        this.robots = robots;
    }
}
