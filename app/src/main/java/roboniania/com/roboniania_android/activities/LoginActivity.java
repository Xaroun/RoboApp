package roboniania.com.roboniania_android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.api.RoboService;
import roboniania.com.roboniania_android.api.model.JwtToken;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REGISTER_REQUEST = 1;
    private SharedPreferenceStorage userLocalStorage;
    private Context context;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button loginBtn;
    private TextView signupLink;
    private EditText loginText, passwordText;
    private ViewAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();
    }

    private void initComponents() {
        context = getApplicationContext();

        //Initialize components
        loginBtn = (Button) findViewById(R.id.login_button);
        loginBtn.setOnClickListener(this);

        signupLink = (TextView) findViewById(R.id.signup_link);
        signupLink.setOnClickListener(this);

        loginText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);

        animator = (ViewAnimator) findViewById(R.id.animator);
        animator.setDisplayedChild(1);

        userLocalStorage = new SharedPreferenceStorage(this);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.login_button:
                animator.setDisplayedChild(0);
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        String login = loginText.getText().toString();
                        String password = passwordText.getText().toString();
                        login(login, password);
                    }
                }).start();
                break;
            case R.id.signup_link:
                startRegisterActivity();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REGISTER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, R.string.account_created, Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "User went back to login form.");
            }
        }
    }

    private void sendResultForMainActivity() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void startRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REGISTER_REQUEST);
    }

    private void login(String login, String password) {
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RoboService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        RoboService roboService = retrofit.create(RoboService.class);

        String authHeader = prepareAuthorizationHeader().trim();

        Call<JwtToken> call = roboService.getJwtToken("password", login, password, authHeader);

        call.enqueue(new Callback<JwtToken>() {
            @Override
            public void onResponse(Call<JwtToken> call, Response<JwtToken> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    JwtToken accessToken = response.body();

                    String authToken = "Bearer " + accessToken.getAccess_token();

                    // Save token to shared prefernces
                    userLocalStorage.storeAccessToken(authToken.trim());
                    userLocalStorage.setUserLoggedIn(true);

                    Log.d(TAG, Integer.toString(statusCode));

                    if(getCallingActivity() == null) {
                        startHomeActivity();
                    } else {
                        sendResultForMainActivity();
                    }

                } else {
                    catchErrorCode(statusCode);
                }
            }

            @Override
            public void onFailure(Call<JwtToken> call, Throwable t) {
                animator.setDisplayedChild(1);
                Toast.makeText(context, R.string.check_connection, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private String prepareAuthorizationHeader() {
        String android_key = "android:ejPdQjZJIAeww16C9GQUnqO16z49n2ms";
        byte[] data = new byte[0];
        try {
            data = android_key.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "Basic " +  Base64.encodeToString(data, Base64.DEFAULT);
    }

    private void catchErrorCode(int statusCode) {
        Log.d(TAG, Integer.toString(statusCode));
        switch(statusCode) {
            case 401:
            case 400:
                //UNAUTHORIZED
                Toast.makeText(context, R.string.wrong_credentials, Toast.LENGTH_SHORT).show();
                animator.setDisplayedChild(1);
                break;
            default:
                Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();
                animator.setDisplayedChild(1);
                break;
        }
    }


}
