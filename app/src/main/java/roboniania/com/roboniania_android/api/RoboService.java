package roboniania.com.roboniania_android.api;

public interface RoboService {
    public static final String ENDPOINT = "http://192.168.2.4:8080";
    public static final String OAUTH = ENDPOINT + "/oauth2/token";
    public static final String ROBOTS_PAIR = ENDPOINT + "/robots";
    public static final String ROBOTS_LIST = ENDPOINT + "/accounts/robots";
}
