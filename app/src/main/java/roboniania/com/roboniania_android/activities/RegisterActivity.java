package roboniania.com.roboniania_android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.api.RoboService;
import roboniania.com.roboniania_android.api.model.Account;
import roboniania.com.roboniania_android.api.model.NewAccount;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private TextView loginLink;
    private Button registerBtn;
    private EditText loginText, passwordText, confirmPasswordText;
    private ViewAnimator animator;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initComponents();
    }

    private void initComponents() {
        context = getApplicationContext();

        //Initialize components
        registerBtn = (Button) findViewById(R.id.register_button);
        registerBtn.setOnClickListener(this);

        loginLink = (TextView) findViewById(R.id.login_link);
        loginLink.setOnClickListener(this);

        loginText = (EditText) findViewById(R.id.input_login_register);
        passwordText = (EditText) findViewById(R.id.input_password_register);
        confirmPasswordText = (EditText) findViewById(R.id.input_password_confirm);

        animator = (ViewAnimator) findViewById(R.id.animator);
        animator.setDisplayedChild(1);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.register_button:
                animator.setDisplayedChild(0);
                final NewAccount newAccount = createAccountObject();
                if(newAccount == null) {
                    Toast.makeText(context, R.string.pass_not_equal, Toast.LENGTH_SHORT).show();
                    clearPasswords();
                    animator.setDisplayedChild(1);
                } else {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            registerUser(newAccount);
                        }
                    }).start();
                }

                break;
            case R.id.login_link:
                sendCancelForLoginActivity();
                break;
        }
    }

    private void clearPasswords() {
        passwordText.setText("");
        confirmPasswordText.setText("");
    }

    private NewAccount createAccountObject() {
        String login = loginText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();

        if(password.equals(confirmPassword)) {
            return new NewAccount(login, password, "eee", "bbb", "aaa@koko.com");
        } else {
            return null;
        }

    }

    private void registerUser(NewAccount newAccount) {
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RoboService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        RoboService roboService = retrofit.create(RoboService.class);
        Call<Account> call = roboService.registerUser(newAccount);

        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    Account account = response.body();

                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);

                    Log.d(TAG, Integer.toString(statusCode));
                    sendOkForLoginActivity();
                    finish();

                } else {
                    catchErrorCode(statusCode);
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                animator.setDisplayedChild(1);
                Toast.makeText(context, R.string.check_connection, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void catchErrorCode(int statusCode) {
        Log.d(TAG, Integer.toString(statusCode));
        switch(statusCode) {
            case 500:
                //SERVER PROBLEM
                Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();
                animator.setDisplayedChild(1);
                break;
            case 400:
                //FILL THE FORM PROPERLY
                Toast.makeText(context, R.string.fill_form_properly, Toast.LENGTH_SHORT).show();
                animator.setDisplayedChild(1);
        }
    }

    private void sendOkForLoginActivity() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void sendCancelForLoginActivity() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

}
