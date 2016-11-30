package roboniania.com.roboniania_android.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import roboniania.com.roboniania_android.PairingRobot;
import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.adapter.model.Game;
import roboniania.com.roboniania_android.api.RoboService;
import roboniania.com.roboniania_android.api.model.Robot;
import roboniania.com.roboniania_android.api.model.User;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String GAME_EXTRA_KEY = "game";
    private Toolbar toolbar;
    private SharedPreferenceStorage userLocalStorage;
    private Button play;
    private static final String TAG = GameActivity.class.getSimpleName();
    private Context context;
    private Game game;
    private String uuid = null;
    private LinkedList<Robot> robotsList;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initComponents();
    }

    private void initComponents() {
        userLocalStorage = new SharedPreferenceStorage(this);
        context = getApplicationContext();

        play = (Button) findViewById(R.id.play);
        play.setOnClickListener(this);

        //SETTING UP TOOLBAR
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        game = (Game) i.getExtras().getSerializable(GAME_EXTRA_KEY);
        showGame(game);

        //getRobotUuid();
        downloadRobotsList();
    }

    private void downloadRobotsList() {
        //THERE SHOULD BE ROBOTS LIST DOWNLOAD MECHANISM

        robotsList = new LinkedList<>();

        robotsList.add(new Robot("189.123.11.47", "EV3POZ", "f75e6a52-f84f-47b0-b1fa-c361652fd1a4"));
        robotsList.add(new Robot("212.45.193.03", "EV3WRO", "cd96052b-261e-4e34-9237-9c5316280f83"));
        robotsList.add(new Robot("95.112.09.178", "EV3WAR", "29c362e3-e4b8-40f8-9009-72bac396c82e"));
    }

    private void showGame(Game game) {
        TextView title = (TextView) findViewById(R.id.gameDetailTitle);
        ImageView icon = (ImageView) findViewById(R.id.gameDetailIcon);
        TextView description = (TextView) findViewById(R.id.gameDescription);
        ImageView gamePic = (ImageView) findViewById(R.id.game_pic);

        title.setText(game.getTitleId());
        icon.setImageResource(game.getIconId());
        description.setText(game.getDescriptionId());
        gamePic.setImageResource(game.getPhotoId());
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
                PairingRobot.showPairDialog(this, userLocalStorage);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                switch (game.getTitleId()) {
                    case R.string.label_tictac:
//                        startPlaying("TIC_TAC_TOE");
                        showRobotsPopupList();
                        break;
                    case R.string.label_tag:
//                        startPlaying("TAG");
                        break;
                    case R.string.label_moving:
                        break;
                    case R.string.label_follower:
//                        startPlaying("LINE_FOLLOWER");
                        break;
                }
                break;
        }

    }

    private void showRobotsPopupList() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(GameActivity.this);
        builderSingle.setIcon(R.drawable.robot);
        builderSingle.setTitle(R.string.choose_robot);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(GameActivity.this, android.R.layout.select_dialog_singlechoice);

        for(Robot robot : robotsList) {
            arrayAdapter.add(robot.getIp());
        }

        builderSingle.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedRobotIp = arrayAdapter.getItem(which);
                        launchProgressDialog(selectedRobotIp);

//                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
//                                GameActivity.this);

//                        builderInner.setMessage(selectedRobotIp);
//                        builderInner.setTitle("Your Selected Item is");
//                        builderInner.setPositiveButton("Ok",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                        builderInner.show();
                    }
                });
        builderSingle.show();
    }

    private void launchProgressDialog(String robotIp) {
        progress = new ProgressDialog(GameActivity.this, R.style.AppTheme_Dialog);

        progress.setTitle(R.string.creating_connection);
        progress.setMessage("Trying to connect with " + robotIp);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setProgressNumberFormat(null);
        progress.setProgressPercentFormat(null);
        progress.setIndeterminate(true);
        progress.setCancelable(true);
        progress.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // CONNECTING TO ROBOT
                    Thread.sleep(10000);
                } catch (Exception e) {

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progress.isShowing()) {
                            Toast.makeText(context, R.string.cannot_connect, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                progress.dismiss();
            }
        }).start();

    }


    private void getRobotUuid() {
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RoboService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RoboService roboService = retrofit.create(RoboService.class);

        Call<User> call = roboService.getRobotsList(userLocalStorage.getAccessToken());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    User user = response.body();

                    if(user.getRobots().isEmpty())
                        uuid = null;
                    else
                        uuid = user.getRobots().get(0).getUuid();

                    Log.d(TAG, Integer.toString(statusCode));

                } else {
                    uuid = null;
                    Log.d(TAG, Integer.toString(statusCode));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, R.string.check_connection, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void startPlaying(String game) {
//        System.out.println(uuid);

        if(uuid != null) {
            Gson gson = new GsonBuilder().create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RoboService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            RoboService roboService = retrofit.create(RoboService.class);

            Call<Void> call = roboService.startPlaying(uuid, game);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    int statusCode = response.code();
                    if (response.isSuccessful()) {

                        Log.d(TAG, "ROBOT IS PLAYING");
                        Toast.makeText(context, "Robot just started game.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, Integer.toString(statusCode));

                    } else {
                        Log.d(TAG, Integer.toString(statusCode));
                        Log.d(TAG, "ERROR?");
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, R.string.check_connection, Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            Toast.makeText(context, R.string.no_robots, Toast.LENGTH_SHORT).show();
        }

    }

}

