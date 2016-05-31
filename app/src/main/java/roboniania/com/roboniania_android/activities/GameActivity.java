package roboniania.com.roboniania_android.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import roboniania.com.roboniania_android.PairingRobot;
import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.adapter.model.Game;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class GameActivity extends AppCompatActivity {

    public static final String GAME_EXTRA_KEY = "game";
    private Toolbar toolbar;
    private SharedPreferenceStorage userLocalStorage;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initComponents();
    }

    private void initComponents() {
        userLocalStorage = new SharedPreferenceStorage(this);
        handler = new Handler();

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
        ImageView photo = (ImageView) findViewById(R.id.gameDetailIcon);

        title.setText(game.getTitle());
        photo.setImageResource(game.getIconId());
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
}
