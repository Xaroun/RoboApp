package roboniania.com.roboniania_android.adapter.model;

import java.io.Serializable;

/**
 * Created by Mateusz on 03.05.2016.
 */
public class Game implements Serializable {
    private int iconId;
    private String title;
    private int descriptionId;



    public int getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(int descriptionId) {
        this.descriptionId = descriptionId;
    }

    public int getIconId() {
        return iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
