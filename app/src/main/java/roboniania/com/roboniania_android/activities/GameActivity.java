package roboniania.com.roboniania_android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.adapter.model.Game;

public class GameActivity extends AppCompatActivity {

    public static final String GAME_EXTRA_KEY = "game";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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
}
