package roboniania.com.roboniania_android.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.adapter.AdapterGameList;
import roboniania.com.roboniania_android.adapter.RecyclerItemClickListener;
import roboniania.com.roboniania_android.adapter.model.Game;

public class GameListActivity extends AppCompatActivity {

    private RecyclerView gamesList;
    private AdapterGameList adapterGameList;
    private Context context;
    private List<Game> games;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        context = getApplicationContext();
        initializeList();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

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
                R.drawable.game_icon1,
                R.drawable.game_icon2,
                R.drawable.game_icon3
        };

        String[] titles = {
                "TIC-TAC-TOE",
                "SOLITAIRE",
                "CHESS"
        };

        for (int i = 0; i < icons.length && i <titles.length; i++) {
            Game currentGame = new Game();
            currentGame.setIconId(icons[i]);
            currentGame.setTitle(titles[i]);
            games.add(currentGame);
        }

        return games;
    }

}
