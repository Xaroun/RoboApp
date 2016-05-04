package roboniania.com.roboniania_android.adapter.model;

import java.io.Serializable;

/**
 * Created by Mateusz on 04.05.2016.
 */
public class Edu implements Serializable {
    private int iconId;
    private String title;

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
