package roboniania.com.roboniania_android.api.model;

import java.io.Serializable;

/**
 * Created by Mateusz on 30.11.2016.
 */
public class Config implements Serializable{

    private String id;
    private String requirements;
    private String config;

    public Config(String id, String requirements, String config) {
        this.id = id;
        this.requirements = requirements;
        this.config = config;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
