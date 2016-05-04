package roboniania.com.roboniania_android.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import roboniania.com.roboniania_android.PairingRobot;
import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.api.RoboService;
import roboniania.com.roboniania_android.api.model.Robot;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

//    private Context context;
    private SharedPreferenceStorage userLocalStorage;
    private ImageView avatar, games, edu;
    private TextView hello;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        context = getApplicationContext();
        userLocalStorage = new SharedPreferenceStorage(this);

        hello = (TextView) findViewById(R.id.hello);
//        hello.setText("Hello " + getIntent().getExtras().getString("EMAIL"));

        avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setOnClickListener(this);

        games = (ImageView) findViewById(R.id.games);
        games.setOnClickListener(this);

        edu = (ImageView) findViewById(R.id.edu);
        edu.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar:
                startAccountActivity();
                break;
            case R.id.games:
                startGamesActivity();
                break;
            case R.id.edu:
                startEduActivity();
                break;
        }
    }

    private void startEduActivity() {
        Intent intent = new Intent(this, EduListActivity.class);
        startActivity(intent);
    }

    private void startGamesActivity() {
        Intent intent = new Intent(this, GameListActivity.class);
        startActivity(intent);
    }

    private void startAccountActivity() {
        System.out.println("MyAccount");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add:
                PairingRobot.showPairDialog(this, userLocalStorage);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
