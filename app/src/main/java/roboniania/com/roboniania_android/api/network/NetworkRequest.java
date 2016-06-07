package roboniania.com.roboniania_android.api.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class NetworkRequest {

    private final String url;
    private final HttpMethod method;
    private final String body;
    private SharedPreferenceStorage userLocalStorage;
    private String login, password, pairKey, type, oldPass, newPass;
    private int RESPONSE_CODE;
    private static final String TAG = NetworkRequest.class.getSimpleName();

    public NetworkRequest(String url, HttpMethod method, String body, SharedPreferenceStorage userLocalStorage, String type) {
        this.url = url;
        this.method = method;
        this.body = body;
        this.userLocalStorage = userLocalStorage;
        this.type = type;
    }

    public NetworkRequest(String url, HttpMethod method, String body, String oldPass, String newPass, SharedPreferenceStorage userLocalStorage, String type) {
        this.url = url;
        this.method = method;
        this.body = body;
        this.oldPass = oldPass;
        this.newPass = newPass;
        this.userLocalStorage = userLocalStorage;
        this.type = type;
    }

    public NetworkRequest(String url, HttpMethod method, String body,  String type) {
        this.url = url;
        this.method = method;
        this.body = body;
        this.type = type;
    }

    public NetworkRequest(String url, HttpMethod method, String body, String login, String password, String type) {
        this.url = url;
        this.method = method;
        this.body = body;
        this.login = login;
        this.password = password;
        this.type = type;
    }

    public NetworkRequest(String url, HttpMethod method, String body, SharedPreferenceStorage userLocalStorage, String pairKey, String type) {
        this.url = url;
        this.method = method;
        this.body = body;
        this.pairKey = pairKey;
        this.userLocalStorage = userLocalStorage;
        this.type = type;
    }

    public int getRESPONSE_CODE() {
        return RESPONSE_CODE;
    }

    private void setHeaders(String type, HttpURLConnection conn) {
        switch(type) {
            case "login_code":
                conn.setRequestProperty("Login", login);
                conn.setRequestProperty("Password",password);
                break;
            case "robot_pair":
                conn.setRequestProperty("Pair-Key",pairKey);
                conn.setRequestProperty("Token", userLocalStorage.getAccessToken());
                break;
            case "token":
                conn.setRequestProperty("Token", userLocalStorage.getAccessToken());
                break;
            case "change_code":
                conn.setRequestProperty("oldPass", oldPass);
                conn.setRequestProperty("newPass", newPass);
                conn.setRequestProperty("Token", userLocalStorage.getAccessToken());
            default:
                break;

        }
    }

    public String execute() throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(method.getMethod());
            conn.setDoInput(true);
            conn.setRequestProperty("Accept","application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            setHeaders(type, conn);
            if (body != null) {
                conn.getOutputStream().write(body.getBytes());
            }
            conn.connect();
            RESPONSE_CODE = conn.getResponseCode();
            Log.d(TAG, "RESPONSE CODE IN REQUEST: " + RESPONSE_CODE);
            if (RESPONSE_CODE == 202 || RESPONSE_CODE == 200) {
                is = conn.getInputStream();
            } else {
                return null;
            }

            return readStream(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private String readStream(InputStream stream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }
}
