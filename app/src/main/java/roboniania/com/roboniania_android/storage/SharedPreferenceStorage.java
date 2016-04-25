package roboniania.com.roboniania_android.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceStorage {
    public static final String SP_NAME = "userAuthentication";
    SharedPreferences userLocalDatabase;

    public SharedPreferenceStorage(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }

    public void storeAccessToken(String accessToken) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("accessToken", accessToken);
        spEditor.commit();
    }

    public String getAccessToken() {
        String accessToken = userLocalDatabase.getString("accessToken", "");
        return  accessToken;
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn() {
        if (userLocalDatabase.getBoolean("loggedIn", false)) {
            return true;
        } else {
            return false;
        }
    }
    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

}
