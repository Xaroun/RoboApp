package roboniania.com.roboniania_android.api.network;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.api.RoboService;
import roboniania.com.roboniania_android.api.model.OAuthToken;
import roboniania.com.roboniania_android.api.model.Robot;
import roboniania.com.roboniania_android.api.model.User;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class NetworkProvider {

    private final Context context;
    private SharedPreferenceStorage userLocalStorage;

    private final List<User> users = new ArrayList<>();
    private final List<Robot> robots = new ArrayList<>();
    private final List<OAuthToken> oauth = new ArrayList<>();

    private static final String login = "login";
    private static final String robot_pair = "robot_pair";

    public interface OnRecipesDownloadedListener {
        void onRecipesDownloaded();
    }

    public NetworkProvider(Context context, SharedPreferenceStorage userLocalStorage) {
        this.context = context;
        this.userLocalStorage = userLocalStorage;
    }


    public void pairing(OnRecipesDownloadedListener listener, SharedPreferenceStorage userLocalStorage) throws IOException, JSONException {
        if (isOnline()) {
            String s = getPairKey(userLocalStorage);
            JSONObject recipesObject = new JSONObject(s);
            JSONArray recipesArray = recipesObject.getJSONArray("recipes");

            for (int i = 0; i < recipesArray.length(); ++i) {
                JSONObject recipeObject = recipesArray.getJSONObject(i);

                Robot robot = new Robot(recipeObject.getString("ip"),
                        recipeObject.getString("sn"),
                        recipeObject.getString("uuid"));

                robots.add(robot);
            }

            listener.onRecipesDownloaded();
        } else {
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

//    public int getRecipesNumber() {
//        return recipes.size();
//    }
//
//    public List<Recipe> getAllRecipes() {
//        return recipes;
//    }

    private String getPairKey() throws IOException {
        return new NetworkRequest(RoboService.ROBOTS_PAIR, HttpMethod.GET, robot_pair).execute();
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
