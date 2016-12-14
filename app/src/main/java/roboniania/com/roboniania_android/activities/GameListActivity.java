package roboniania.com.roboniania_android.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import roboniania.com.roboniania_android.PairingRobot;
import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.adapter.AdapterGameList;
import roboniania.com.roboniania_android.adapter.RecyclerItemClickListener;
import roboniania.com.roboniania_android.api.RoboService;
import roboniania.com.roboniania_android.api.model.NewGame;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class GameListActivity extends AppCompatActivity {

    private RecyclerView gamesList;
    private AdapterGameList adapterGameList;
    private Context context;
    private Toolbar toolbar;
    private SharedPreferenceStorage userLocalStorage;
    private static final String TAG = GameListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initComponents();
    }

    private void initComponents() {
        context = getApplicationContext();
        userLocalStorage = new SharedPreferenceStorage(this);

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
        getGameList();
    }

    private void startGameActivity(NewGame game) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra(GameActivity.GAME_EXTRA_KEY, game);
        startActivity(i);
    }

    public void getGameList() {
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RoboService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RoboService roboService = retrofit.create(RoboService.class);

        Call<List<NewGame>> call = roboService.getGamesList();

        call.enqueue(new Callback<List<NewGame>>() {
            @Override
            public void onResponse(Call<List<NewGame>> call, Response<List<NewGame>> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    final List<NewGame> listOfGames = response.body();

                    int[] icons = {
                            R.drawable.tictactoe,
                            R.drawable.smile,
                            R.drawable.moving,
                            R.drawable.follower
                    };

                    int[] photos = {
                            R.drawable.tictac,
                            R.drawable.tag,
                            R.drawable.move,
                            R.drawable.linefollower
                    };

                    int i = 0;
                    for(NewGame game : listOfGames) {
                        if(i < 4) {
                            game.setIconId(icons[i]);
                            game.setPhotoId(photos[i]);
                            i++;
                        } else {
                            i = 0;
                            game.setIconId(icons[i]);
                            game.setPhotoId(photos[i]);
                            i++;
                        }

                    }

                    Log.d(TAG, Integer.toString(statusCode));

                    adapterGameList = new AdapterGameList(context, listOfGames);
                    gamesList.setAdapter(adapterGameList);
                    gamesList.setLayoutManager(new LinearLayoutManager(context));
                    gamesList.addOnItemTouchListener(
                            new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    NewGame game = listOfGames.get(position);
                                    startGameActivity(game);
                                }
                            })
                    );

                } else {
                    Log.d(TAG, Integer.toString(statusCode));
                }
            }

            @Override
            public void onFailure(Call<List<NewGame>> call, Throwable t) {
                Toast.makeText(context, R.string.check_connection, Toast.LENGTH_SHORT).show();
            }

        });
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
                PairingRobot.showPairDialog(this, userLocalStorage);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
