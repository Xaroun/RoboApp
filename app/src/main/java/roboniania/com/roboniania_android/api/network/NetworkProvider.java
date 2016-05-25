package roboniania.com.roboniania_android.api.network;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
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

    private static final String login_code = "login_code";
    private static final String robot_pair = "robot_pair";
    private static final String robot_list = "robot_list";
    private int RESPONSE_CODE;


    public NetworkProvider(Context context, SharedPreferenceStorage userLocalStorage) {
        this.context = context;
        this.userLocalStorage = userLocalStorage;
    }

    public int getRESPONSE_CODE() {
        return RESPONSE_CODE;
    }

    public interface OnResponseReceivedListener {
        void onResponseReceived();
    }

    public void showPairDialog(final Context context, final SharedPreferenceStorage userLocalStorage, final OnResponseReceivedListener listener) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final EditText pairKey = new EditText(context);
        pairKey.setTextColor(Color.RED);
        pairKey.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setMessage("Enter robot's pair-key:");
        alert.setTitle("Connecting..");
        alert.setView(pairKey);


        alert.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    if (isOnline()) {
                        String s = checkPairKey(userLocalStorage, pairKey.getText().toString());
                        if (s == null) {
                            // INVALID PAIRKEY
                        } else {
                            parseRobot(s);
                        }

                    } else {
                        // INVALID PAIRKEY
                    }
                    listener.onResponseReceived();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    private void parseRobot(String robotString) throws JSONException {
        JSONObject recipeObject = new JSONObject(robotString);

        Robot robot = new Robot(recipeObject.getString("ip"),
                    recipeObject.getString("sn"),
                    recipeObject.getString("uuid"));

            robots.add(robot);

    }

    public void pair(SharedPreferenceStorage userLocalStorage, String pairKey, OnResponseReceivedListener listener) throws IOException, JSONException {
        if (isOnline()) {
            String s = checkPairKey(userLocalStorage, pairKey);
            if (s == null) {
                // INVALID PAIRKEY
            } else {
                parseRobot(s);
            }

        } else {
            // NO INTERNET
        }
        listener.onResponseReceived();
    }

    private String checkPairKey(SharedPreferenceStorage userLocalStorage, String pairKey) throws IOException {
        NetworkRequest request = new NetworkRequest(RoboService.ROBOTS_PAIR, HttpMethod.GET, null, userLocalStorage, pairKey, robot_pair);
        String response = request.execute();
        RESPONSE_CODE = request.getRESPONSE_CODE();
        System.out.println("RESPONSE CODE IN PROVIDER : " + RESPONSE_CODE);

        if (RESPONSE_CODE == 200 || RESPONSE_CODE == 202) {
            // SUCCESFULLY PAIRED
            return response;
        }
        else {
            // INVALID PAIRKEY
            return null;
            //TODO catch code error
        }
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void login(String login, String password, OnResponseReceivedListener listener) throws IOException, JSONException {
        NetworkRequest request = new NetworkRequest(RoboService.OAUTH, HttpMethod.GET, null, login, password, login_code);
        String response = request.execute();
        RESPONSE_CODE = request.getRESPONSE_CODE();

        if (RESPONSE_CODE == 200 || RESPONSE_CODE == 202) {
            OAuthToken accessToken = parseToken(response);

            // Save token to shared prefernces
            userLocalStorage.storeAccessToken(accessToken.getAccess_token());
            userLocalStorage.setUserLoggedIn(true);
            System.out.println("TOKEN: " + accessToken.getAccess_token());
        }
        listener.onResponseReceived();
    }

    private OAuthToken parseToken(String response) throws JSONException{
        JSONObject responseObject = new JSONObject(response);
        OAuthToken token = new OAuthToken();
        token.setAccess_token(responseObject.getString("access_token"));
        return token;
    }
}
