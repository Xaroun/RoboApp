package roboniania.com.roboniania_android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.api.RoboService;
import roboniania.com.roboniania_android.api.model.OAuthToken;
import roboniania.com.roboniania_android.api.network.NetworkProvider;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REGISTER_REQUEST = 1;
    private SharedPreferenceStorage userLocalStorage;
    private Context context;
    private Handler handler;
    private static final String TAG = LoginActivity.class.getSimpleName();


    private Button loginBtn;
    private TextView signupLink;
    private EditText emailText, passwordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        handler = new Handler();

        //Initialize components
        loginBtn = (Button) findViewById(R.id.login_button);
        loginBtn.setOnClickListener(this);

        signupLink = (TextView) findViewById(R.id.signup_link);
        signupLink.setOnClickListener(this);

        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);

        userLocalStorage = new SharedPreferenceStorage(this);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.login_button:
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        String email = emailText.getText().toString();
                        String password = passwordText.getText().toString();
                        login(email, password);
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
                Log.e("ERR", "User went back to login form.");
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

    private void login(String email, String password) {
        final NetworkProvider networkProvider = new NetworkProvider(this, userLocalStorage);
        try {
            networkProvider.login(email, password, new NetworkProvider.OnResponseReceivedListener() {

                @Override
                public void onResponseReceived() {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
//                            Log.d(TAG, "RESPONSE CODE:  " + networkProvider.getRESPONSE_CODE());
                            if (networkProvider.getRESPONSE_CODE() == 200 || networkProvider.getRESPONSE_CODE() == 202) {
                                sendResultForMainActivity();
                            } else {
                                Log.d(TAG, "Wrong credentials.");
                                Toast.makeText(context, R.string.wrong_credentials, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
