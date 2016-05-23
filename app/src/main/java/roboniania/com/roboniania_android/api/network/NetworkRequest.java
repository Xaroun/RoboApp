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
    private HttpURLConnection conn;
    private SharedPreferenceStorage userLocalStorage;

    public NetworkRequest(String url, HttpMethod method, String body, String type) {
        this.url = url;
        this.method = method;
        this.body = body;
        this.conn = setHeaders(type);
    }

    public HttpURLConnection setHeaders(String type) {
        switch(type) {
            case "login":
                conn.setRequestProperty("Login", "");
                conn.setRequestProperty("Password","");
                return conn;
            case "robot_pair":
                conn.setRequestProperty("Pair-Key","");
                conn.setRequestProperty("Token", userLocalStorage.getAccessToken());
                return conn;
            case "robot_list":
                conn.setRequestProperty("Token", userLocalStorage.getAccessToken());
                return conn;
            default:
                return conn;
        }
    }



    public NetworkRequest(String url, HttpMethod method, String type) {
        this(url, method, null, type);
    }

    public String execute() throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(this.url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod(method.getMethod());
            conn.setDoInput(true);

            if (body != null) {
                conn.getOutputStream().write(body.getBytes());
            }

            conn.connect();
            is = conn.getInputStream();

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
