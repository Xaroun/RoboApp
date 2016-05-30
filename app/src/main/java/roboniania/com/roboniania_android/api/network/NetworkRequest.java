package roboniania.com.roboniania_android.api.network;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class NetworkRequest {

    private String url;
    private final HttpMethod method;
    private final String body;
    private SharedPreferenceStorage userLocalStorage;
    private String pairKey, type;
    private int RESPONSE_CODE;

    public NetworkRequest(String url, HttpMethod method, String body, SharedPreferenceStorage userLocalStorage, String type) {
        this.url = url;
        this.method = method;
        this.body = body;
        this.userLocalStorage = userLocalStorage;
        this.type = type;
    }

    public NetworkRequest(String url, HttpMethod method, String body, String type) {
        this.url = url;
        this.method = method;
        this.body = body;
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
                String app_id = "resource-server:";
                byte[] data = new byte[0];
                try {
                    data = app_id.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String base64 = "Basic " +  Base64.encodeToString(data, Base64.DEFAULT);
                conn.setRequestProperty("Authorization", base64);
                conn.setDoOutput(true);

                break;
            case "robot_pair":
                conn.setRequestProperty("Pair-Key",pairKey);
                conn.setRequestProperty("Token", userLocalStorage.getAccessToken());
                break;
            case "robot_list":
                conn.setRequestProperty("Token", userLocalStorage.getAccessToken());
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
            conn.setRequestProperty("Content-Type","application/json");
            setHeaders(type, conn);
            if (body != null) {
                conn.getOutputStream().write(body.getBytes());
            }
            conn.connect();
            RESPONSE_CODE = conn.getResponseCode();
            System.out.println("RESPONSE CODE IN REQUEST: " + RESPONSE_CODE);
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
