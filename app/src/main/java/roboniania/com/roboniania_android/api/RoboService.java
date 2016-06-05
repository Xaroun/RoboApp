package roboniania.com.roboniania_android.api;

public interface RoboService {
    public static final String ENDPOINT = "http://192.168.2.6:8080";
    public static final String OAUTH = ENDPOINT + "/oauth2/token";
    public static final String ROBOTS_PAIR = ENDPOINT + "/robots";
    public static final String ROBOTS_LIST = ENDPOINT + "/accounts/robots";
    public static final String MY_PROFILE = ENDPOINT + "/accounts/me";
    public static final String EDIT_PROFILE = ENDPOINT + "/accounts/update_profile";
}
