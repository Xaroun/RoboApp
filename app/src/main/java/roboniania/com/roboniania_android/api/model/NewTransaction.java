package roboniania.com.roboniania_android.api.model;

/**
 * Created by Mateusz on 14.12.2016.
 */
public class NewTransaction {
    private String robot_id;
    private String game_id;

    public NewTransaction(String robot_id, String game_id) {
        this.robot_id = robot_id;
        this.game_id = game_id;
    }

    public String getRobot_id() {
        return robot_id;
    }

    public void setRobot_id(String robot_id) {
        this.robot_id = robot_id;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }
}
