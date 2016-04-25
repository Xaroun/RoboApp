package roboniania.com.roboniania_android.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import roboniania.com.roboniania_android.api.model.OAuthToken;

/**
 * Created by s396348 on 2016-04-25.
 */
public interface RoboService {

    @GET("oauth2/token")
    Call<OAuthToken> getToken(@Header("Login") String login,
                              @Header("Password") String password);
}
