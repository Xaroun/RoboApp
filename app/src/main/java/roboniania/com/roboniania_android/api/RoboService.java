package roboniania.com.roboniania_android.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import roboniania.com.roboniania_android.api.model.Account;
import roboniania.com.roboniania_android.api.model.JwtToken;
import roboniania.com.roboniania_android.api.model.NewAccount;
import roboniania.com.roboniania_android.api.model.Robot;
import roboniania.com.roboniania_android.api.model.User;

public interface RoboService {
    public static final String ENDPOINT = "http://192.168.2.5:8080/api/";
    public static final String ACCEPT_ROBOAPP = "Accept: application/vnd.roboapp.v1+json";
    public static final String ACCEPT_JSON = "Accept: application/json";
    public static final String CONTENT_TYPE = "Content-Type: application/vnd.roboapp.v1+json";

    @Headers({ACCEPT_ROBOAPP, CONTENT_TYPE})
    @POST("accounts/register")
    Call<Account> registerUser(@Body NewAccount newAccount);

    @Headers(ACCEPT_JSON)
    @POST("oauth/token?")
    Call<JwtToken> getJwtToken(@Query("grant_type") String grantType,
                               @Query("username") String username,
                               @Query("password") String password,
                                @Header("Authorization") String basicAuthorization);
    @Headers(ACCEPT_ROBOAPP)
    @GET("accounts/me")
    Call<Account> getMyAccount(@Header("Authorization") String oauthAuthorization);

    @GET("/robots")
    Call<Robot> getRobot(@Header("Pair-Key") String pairKey,
                         @Header("Token") String oauthToken);

    @GET("/accounts/robots")
    Call<User> getRobotsList(@Header("Token") String oauthToken);

    @GET("/accounts/me")
    Call<User> getUser(@Header("Token") String oauthToken);

    @PUT("/accounts/update_profile")
    Call<User> changePassword(@Header("oldPass") String oldPass,
                       @Header("newPass") String newPass,
                       @Header("Token") String oauthToken);

    @GET("/robots/{robotUUID}/games/{gameName}")
    Call<Void> startPlaying(@Path("robotUUID") String robotUuid,
                            @Path("gameName") String gameName);


}
