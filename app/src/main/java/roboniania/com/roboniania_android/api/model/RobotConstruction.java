package roboniania.com.roboniania_android.api.model;

import java.io.Serializable;

import roboniania.com.roboniania_android.api.enums.Constructions;

/**
 * Created by Mateusz on 30.11.2016.
 */
public class RobotConstruction implements Serializable{

    private String id;
    private Constructions robot_model;
    private String name;
    private String info;
    private Config config;

    public RobotConstruction(String id, Constructions robot_model, String name, String info, Config config) {
        this.id = id;
        this.robot_model = robot_model;
        this.name = name;
        this.info = info;
        this.config = config;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Constructions getRobot_model() {
        return robot_model;
    }

    public void setRobot_model(Constructions robot_model) {
        this.robot_model = robot_model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}

