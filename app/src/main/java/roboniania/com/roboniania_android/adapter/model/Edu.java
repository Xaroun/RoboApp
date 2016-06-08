package roboniania.com.roboniania_android.adapter.model;

import java.io.Serializable;

/**
 * Created by Mateusz on 04.05.2016.
 */
public class Edu implements Serializable {
    private int iconId;
    private int titleId;

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
