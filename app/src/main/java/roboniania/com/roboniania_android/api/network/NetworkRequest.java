package roboniania.com.roboniania_android.api.network;

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
//    private HttpURLConnection conn;
    private SharedPreferenceStorage userLocalStorage;
    private String login, password, pairKey, type;
    private int RESPONSE_CODE;

    public NetworkRequest(String url, HttpMethod method, String body, SharedPreferenceStorage userLocalStorage, String type) {
        this.url = url;
        this.method = method;
        this.body = body;
        this.userLocalStorage = userLocalStorage;
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

//    public void setHeaders(String type) {
//        switch(type) {
//            case "login_code":
//                conn.setRequestProperty("Login", login);
//                conn.setRequestProperty("Password",password);
//                break;
////                return conn;
//            case "robot_pair":
//                conn.setRequestProperty("Pair-Key",pairKey);
//                conn.setRequestProperty("Token", userLocalStorage.getAccessToken());
//                break;
////                return conn;
//            case "robot_list":
//                conn.setRequestProperty("Token", userLocalStorage.getAccessToken());
//                break;
////                return conn;
////            default:
////                return conn;
//        }
//    }

    public String execute() throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod(method.getMethod());
            conn.setDoInput(true);
//            setHeaders(type);
            conn.setRequestProperty("Login", login);
            conn.setRequestProperty("Password", password);
            if (body != null) {
                conn.getOutputStream().write(body.getBytes());
            }
            conn.connect();
            is = conn.getInputStream();
            RESPONSE_CODE = conn.getResponseCode();

            return readStream(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readStream(InputStream stream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }
}
