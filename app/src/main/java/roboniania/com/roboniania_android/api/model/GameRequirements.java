package roboniania.com.roboniania_android.api.model;

import java.io.Serializable;

/**
 * Created by Mateusz on 14.12.2016.
 */
public class GameRequirements implements Serializable {
    private String lego_construction;
    private String config_file;

    public GameRequirements() {
    }

    public GameRequirements(String config_file) {
        this.config_file = config_file;
    }

    public GameRequirements(String lego_construction, String config_file) {
        this.lego_construction = lego_construction;
        this.config_file = config_file;
    }

    public String getLego_construction() {
        return lego_construction;
    }

    public void setLego_construction(String lego_construction) {
        this.lego_construction = lego_construction;
    }

    public String getConfig_file() {
        return config_file;
    }

    public void setConfig_file(String config_file) {
        this.config_file = config_file;
    }
}
