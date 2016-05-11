package roboniania.com.roboniania_android.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import roboniania.com.roboniania_android.api.model.OAuthToken;
import roboniania.com.roboniania_android.api.model.Robot;
import roboniania.com.roboniania_android.api.model.User;

/**
 * Created by s396348 on 2016-04-25.
 */
public interface RoboService {

    public static final String ENDPOINT = "http://192.168.2.5:8080";

    @GET("/oauth2/token")
    Call<OAuthToken> getToken(@Header("Login") String login,
                              @Header("Password") String password);

    @GET("/robots")
    Call<Robot> getRobot(@Header("Pair-Key") String pairKey,
                              @Header("Token") String oauthToken);

    @GET("/accounts/robots")
    Call<User> getRobotsList(@Header("Token") String oauthToken);
}
