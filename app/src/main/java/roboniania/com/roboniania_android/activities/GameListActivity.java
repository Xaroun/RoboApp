package roboniania.com.roboniania_android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import roboniania.com.roboniania_android.PairingRobot;
import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.adapter.AdapterGameList;
import roboniania.com.roboniania_android.adapter.RecyclerItemClickListener;
import roboniania.com.roboniania_android.adapter.model.Game;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class GameListActivity extends AppCompatActivity {

    private RecyclerView gamesList;
    private AdapterGameList adapterGameList;
    private Context context;
    private List<Game> games;
    private Toolbar toolbar;
    private SharedPreferenceStorage userLocalStorage;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initComponents();
    }

    private void initComponents() {
        context = getApplicationContext();
        userLocalStorage = new SharedPreferenceStorage(this);
        handler = new Handler();

        initializeList();

        //SETTING UP TOOLBAR
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //SETTING UP SIDEBAR FRAGMENT
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout)findViewById(R.id.drawer_layout), toolbar);
    }

    private void initializeList() {
        gamesList = (RecyclerView) findViewById(R.id.recyclerList);
        adapterGameList = new AdapterGameList(this, getGames());
        gamesList.setAdapter(adapterGameList);
        gamesList.setLayoutManager(new LinearLayoutManager(this));
        gamesList.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Game game = games.get(position);
                        startGameActivity(game);
                    }
                })
        );
    }

    private void startGameActivity(Game game) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra(GameActivity.GAME_EXTRA_KEY, game);
        startActivity(i);
    }

    public List<Game> getGames() {
        games = new ArrayList<>();
        int[] icons = {
                R.drawable.tictactoe,
                R.drawable.smile,
                R.drawable.moving,
                R.drawable.follower
        };

        int[] titles = {
                R.string.label_tictac,
                R.string.label_tag,
                R.string.label_moving,
                R.string.label_follower
        };

        int[] descriptions = {
                R.string.game_tictac,
                R.string.game_tag,
                R.string.game_moving,
                R.string.game_follower
        };

        int[] photos = {
                R.drawable.tictac,
                R.drawable.tag,
                R.drawable.move,
                R.drawable.linefollower
        };

        for (int i = 0; i < icons.length && i <titles.length; i++) {
            Game currentGame = new Game();
            currentGame.setIconId(icons[i]);
            currentGame.setTitleId(titles[i]);
            currentGame.setDescriptionId(descriptions[i]);
            currentGame.setPhotoId(photos[i]);
            games.add(currentGame);
        }

        return games;
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
