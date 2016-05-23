package roboniania.com.roboniania_android.api.network;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
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

    public void showPairDialog(final Context context, final SharedPreferenceStorage userLocalStorage) {
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
                        parseRobot(s);
                    } else {
                        Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                    }

//                    checkPairKey(pairKey.getText().toString(), userLocalStorage);
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
        JSONObject recipesObject = new JSONObject(robotString);
        JSONArray recipesArray = recipesObject.getJSONArray("robots");

        for (int i = 0; i < recipesArray.length(); ++i) {
            JSONObject recipeObject = recipesArray.getJSONObject(i);

            Robot robot = new Robot(recipeObject.getString("ip"),
                    recipeObject.getString("sn"),
                    recipeObject.getString("uuid"));

            robots.add(robot);
        }
    }

    private String checkPairKey(SharedPreferenceStorage userLocalStorage, String pairKey) throws IOException {
        NetworkRequest request = new NetworkRequest(RoboService.ROBOTS_PAIR, HttpMethod.GET, null, userLocalStorage, pairKey, robot_pair);
        String response = request.execute();
        if (request.getRESPONSE_CODE() == 200) {
            Toast.makeText(context, R.string.successfully_paired, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, R.string.wrong_match, Toast.LENGTH_SHORT).show();
            System.out.println("pair-key and token doesn't match");
            //TODO catch code error
        }

        return response;
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void login(String login, String password) throws IOException, JSONException {
        NetworkRequest request = new NetworkRequest(RoboService.OAUTH, HttpMethod.GET, null, login, password, login_code);
        String response = request.execute();
        RESPONSE_CODE = request.getRESPONSE_CODE();

        if (request.getRESPONSE_CODE() == 200) {
            OAuthToken accessToken = parseResponse(response);

            // Save token to shared prefernces
            userLocalStorage.storeAccessToken(accessToken.getAccess_token());
            userLocalStorage.setUserLoggedIn(true);
            System.out.println("TOKEN :" + accessToken.getAccess_token());
        }
        else {
            Toast.makeText(context, R.string.wrong_credentials, Toast.LENGTH_SHORT).show();
            System.out.println("invalid email or password");
            //TODO catch code error
        }

    }

    private OAuthToken parseResponse(String response) throws JSONException{
        JSONObject responseObject = new JSONObject(response);
        OAuthToken token = new OAuthToken();
        token.setAccess_token(responseObject.getString("access_token"));
        return token;
    }
}
