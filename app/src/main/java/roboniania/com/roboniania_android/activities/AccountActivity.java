package roboniania.com.roboniania_android.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import roboniania.com.roboniania_android.api.model.User;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private SharedPreferenceStorage userLocalStorage;
    private static final String TAG = AccountActivity.class.getSimpleName();
    private TextView login, password, created;
    private CardView changePassword;
    private ViewAnimator animator;
    private Button dismiss, confirmChange;
    private EditText oldPass, newPass;
    private ImageView logoutButton;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initComponents();
        getUserData();
    }

    private void initComponents() {
        userLocalStorage = new SharedPreferenceStorage(this);
        context = getApplicationContext();

        //INITIALIZE COMPONENTS
        login = (TextView) findViewById(R.id.login);
        password = (TextView) findViewById(R.id.password);
        created = (TextView) findViewById(R.id.created);
        changePassword = (CardView) findViewById(R.id.edit_button);
        changePassword.setOnClickListener(this);
        dismiss = (Button) findViewById(R.id.dismiss);
        dismiss.setOnClickListener(this);
        confirmChange = (Button) findViewById(R.id.change_button);
        confirmChange.setOnClickListener(this);
        oldPass = (EditText) findViewById(R.id.input_old_password);
        newPass = (EditText) findViewById(R.id.input_new_password);
        logoutButton = (ImageView) findViewById(R.id.logout);
        logoutButton.setOnClickListener(this);

        //SETTING UP TOOLBAR
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        animator = (ViewAnimator) findViewById(R.id.animator);
        animator.setDisplayedChild(0);
    }

    private void getUserData() {
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RoboService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RoboService roboService = retrofit.create(RoboService.class);

        Call<User> call = roboService.getUser(userLocalStorage.getAccessToken());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    User user = response.body();

                    login.setText(user.getLogin());
                    password.setText(computeStars(user.getPassword()));
                    created.setText(user.getCreateed());

                    Log.d(TAG, Integer.toString(statusCode));

                } else {
                    Log.d(TAG, Integer.toString(statusCode));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, R.string.check_connection, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private String computeStars(String password) {
        String stars = "*";
        int number = password.length();
        for (int i = 1; i < number; i++) {
            stars = "*" + stars;
        }
        return stars;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_button:
                animator.setDisplayedChild(1);
                changePassword.setVisibility(View.INVISIBLE);
                break;
            case R.id.dismiss:
                animator.setDisplayedChild(0);
                clearEditTexts();
                break;
            case R.id.change_button:
                if(validatePassword(oldPass) && validatePassword(newPass)) {
                    startChanging(oldPass.getText().toString(), newPass.getText().toString());
                } else {
                    //PASSWORD EMPTY
                }
                break;
            case R.id.logout:
                showLogoutDialog();
                break;
        }
    }

    private void showLogoutDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Are you sure you want to logout?");
        alert.setTitle("Signing out..");

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                userLocalStorage.clearUserData();
                Toast.makeText(context, R.string.successfully_logged_out, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.create();
        alert.show();
    }

    private void startChanging(String oldPassword, String newPassword) {
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RoboService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RoboService roboService = retrofit.create(RoboService.class);

        Call<User> call = roboService.changePassword(oldPassword, newPassword, userLocalStorage.getAccessToken());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    User user = response.body();
                    Toast.makeText(context, R.string.successfully_changed, Toast.LENGTH_SHORT).show();
                    animator.setDisplayedChild(0);
                    password.setText(computeStars(user.getPassword()));
                    clearEditTexts();
                    hideKeyboard();
                    Log.d(TAG, Integer.toString(statusCode));

                } else {
                    Log.d(TAG, Integer.toString(statusCode));
                    animator.setDisplayedChild(1);
                    Log.d(TAG, "Wrong password.");
                    Toast.makeText(context, R.string.wrong_pass, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, R.string.check_connection, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void clearEditTexts() {
        changePassword.setVisibility(View.VISIBLE);
        oldPass.setText("");
        newPass.setText("");
    }

    private boolean validatePassword(EditText pass) {
        if (pass.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, R.string.empty_pass, Toast.LENGTH_SHORT).show();
            requestFocus(pass);
            return false;
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
