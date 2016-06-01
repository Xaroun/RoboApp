package roboniania.com.roboniania_android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import roboniania.com.roboniania_android.PairingRobot;
import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.adapter.model.Game;
import roboniania.com.roboniania_android.api.network.NetworkProvider;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String GAME_EXTRA_KEY = "game";
    private Toolbar toolbar;
    private SharedPreferenceStorage userLocalStorage;
    private Handler handler;
    private Button play;
    private static final String TAG = GameActivity.class.getSimpleName();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initComponents();
    }

    private void initComponents() {
        userLocalStorage = new SharedPreferenceStorage(this);
        handler = new Handler();
        context = getApplicationContext();

        play = (Button) findViewById(R.id.play);
        play.setOnClickListener(this);

        //SETTING UP TOOLBAR
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        Game game = (Game) i.getExtras().getSerializable(GAME_EXTRA_KEY);
        showGame(game);
    }

    private void showGame(Game game) {
        TextView title = (TextView) findViewById(R.id.gameDetailTitle);
        ImageView icon = (ImageView) findViewById(R.id.gameDetailIcon);
        TextView description = (TextView) findViewById(R.id.gameDescription);

        title.setText(game.getTitle());
        icon.setImageResource(game.getIconId());
        description.setText(game.getDescriptionId());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.add:
                PairingRobot.showPairDialog(this, userLocalStorage, handler);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startPlaying();
                    }
                }).start();
                break;
        }
    }

    private void startPlaying() {
        final NetworkProvider networkProvider = new NetworkProvider(this, userLocalStorage);
        try {
            networkProvider.startPlaying(new NetworkProvider.OnResponseReceivedListener() {

                @Override
                public void onResponseReceived() {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            if (networkProvider.getRESPONSE_CODE() == 204) {
                                Log.d(TAG, "ROBOT IS PLAYING");
                                Toast.makeText(context, "Robot just started game.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "UPS");
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
}
