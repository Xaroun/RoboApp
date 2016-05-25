package roboniania.com.roboniania_android.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

import roboniania.com.roboniania_android.api.model.User;

/**
 * Created by s396348 on 2016-04-25.
 */
public interface RoboService {

    public static final String ENDPOINT = "http://192.168.2.3:8080";

    public static final String OAUTH = ENDPOINT + "/oauth2/token";
    public static final String ROBOTS_PAIR = ENDPOINT + "/robots";
    public static final String ROBOTS_LIST = ENDPOINT + "/accounts/robots";

    //TODO
    // list of robots + cardview pobrać z master brancha


    @GET("/accounts/robots")
    Call<User> getRobotsList(@Header("Token") String oauthToken);
}
