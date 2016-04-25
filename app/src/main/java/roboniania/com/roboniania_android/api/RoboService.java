package roboniania.com.roboniania_android.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import roboniania.com.roboniania_android.api.model.OAuthToken;

/**
 * Created by s396348 on 2016-04-25.
 */
public interface RoboService {
    @GET
    Call<OAuthToken> getToken(@Field("login") String login,
                              @Field("password") String password);
}
