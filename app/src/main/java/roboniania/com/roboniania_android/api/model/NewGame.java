package roboniania.com.roboniania_android.api.model;

import java.io.Serializable;

/**
 * Created by Mateusz on 14.12.2016.
 */
public class NewGame implements Serializable {
    private String id;
    private String name;
    private String description;
    private String author;
    private String version;
    private String robot_model;
    private String robot_system;
    private String updated;
    private String bin_size;
    private GameRequirements game_requirements;
    private int iconId = 0;
    private int photoId = 0;

    public NewGame(String id, String name, String description, String author, String version, String robot_model, String robot_system, String updated, String bin_size, GameRequirements game_requirements) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.author = author;
        this.version = version;
        this.robot_model = robot_model;
        this.robot_system = robot_system;
        this.updated = updated;
        this.bin_size = bin_size;
        this.game_requirements = game_requirements;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRobot_model() {
        return robot_model;
    }

    public void setRobot_model(String robot_model) {
        this.robot_model = robot_model;
    }

    public String getRobot_system() {
        return robot_system;
    }

    public void setRobot_system(String robot_system) {
        this.robot_system = robot_system;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getBin_size() {
        return bin_size;
    }

    public void setBin_size(String bin_size) {
        this.bin_size = bin_size;
    }

    public GameRequirements getGame_requirements() {
        return game_requirements;
    }

    public void setGame_requirements(GameRequirements game_requirements) {
        this.game_requirements = game_requirements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
}
