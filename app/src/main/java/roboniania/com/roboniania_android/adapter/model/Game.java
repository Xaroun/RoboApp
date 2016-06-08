package roboniania.com.roboniania_android.adapter.model;

import java.io.Serializable;

/**
 * Created by Mateusz on 03.05.2016.
 */
public class Game implements Serializable {
    private int iconId;
    private int titleId;
    private int descriptionId;
    private int photoId;

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public int getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(int descriptionId) {
        this.descriptionId = descriptionId;
    }

    public int getIconId() {
        return iconId;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }
}
