package roboniania.com.roboniania_android.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import roboniania.com.roboniania_android.PairingRobot;
import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.api.model.User;
import roboniania.com.roboniania_android.api.network.NetworkProvider;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferenceStorage userLocalStorage;
    private ImageView avatar;
    private TextView hello;
    private Toolbar toolbar;
    private Handler handler;
    private Button games, edu;
    private static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initComponents();
        getUser();
    }

    private void initComponents() {
        userLocalStorage = new SharedPreferenceStorage(this);
        handler = new Handler();


        hello = (TextView) findViewById(R.id.hello);

        avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setOnClickListener(this);

        games = (Button) findViewById(R.id.games);
        games.setOnClickListener(this);

        edu = (Button) findViewById(R.id.edu);
        edu.setOnClickListener(this);

        //SETTING UP TOOLBAR
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //SETTING UP SIDEBAR FRAGMENT
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
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

    private void getUser() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                downloadUser();
            }
        }).start();
    }

    private void downloadUser() {
        final NetworkProvider networkProvider = new NetworkProvider(this, userLocalStorage);
        try {
            networkProvider.downloadUser(new NetworkProvider.OnResponseReceivedListener() {

                @Override
                public void onResponseReceived() {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            if (networkProvider.getRESPONSE_CODE() == 200 || networkProvider.getRESPONSE_CODE() == 202) {
                                User user = networkProvider.getUser();
                                hello.setText("Hi " + user.getLogin() + "!");
                            } else {
                                Log.d(TAG, "ups");
                            }
                        }
                    });
                }
            });

        } catch (IOException e) {
            Log.d(TAG, "IO Exception.");
        } catch (JSONException e) {
            Log.d(TAG, "Problems with JSON.");
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
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add:
                PairingRobot.showPairDialog(this, userLocalStorage, handler);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
