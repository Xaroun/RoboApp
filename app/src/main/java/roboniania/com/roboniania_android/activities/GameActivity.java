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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import roboniania.com.roboniania_android.PairingRobot;
import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.api.RoboService;
import roboniania.com.roboniania_android.api.model.NewGame;
import roboniania.com.roboniania_android.api.model.NewRobot;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String GAME_EXTRA_KEY = "game";
    private Toolbar toolbar;
    private SharedPreferenceStorage userLocalStorage;
    private Button start, play, stop;
    private static final String TAG = GameActivity.class.getSimpleName();
    private Context context;
    private NewGame game;
    private String uuid = null;
    private List<NewRobot> robotsList;
    private ProgressDialog progress;
    private String robotIp, gameCode;
    private int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initComponents();
    }

    private void initComponents() {
        userLocalStorage = new SharedPreferenceStorage(this);
        context = getApplicationContext();

        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);

        play = (Button) findViewById(R.id.play);
        play.setOnClickListener(this);

        stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(this);

        //SETTING UP TOOLBAR
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        game = (NewGame) i.getExtras().getSerializable(GAME_EXTRA_KEY);
        showGame(game);
    }

    public void downloadRobotList(final String gameCode) {

        this.gameCode = gameCode;
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RoboService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RoboService roboService = retrofit.create(RoboService.class);

        Call<List<NewRobot>> call = roboService.getRobotsList(userLocalStorage.getAccessToken());

        call.enqueue(new Callback<List<NewRobot>>() {
            @Override
            public void onResponse(Call<List<NewRobot>> call, Response<List<NewRobot>> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    robotsList = response.body();
                    showRobotsPopupList(gameCode);

                    Log.d(TAG, Integer.toString(statusCode));

                } else {
                    Log.d(TAG, Integer.toString(statusCode));
                }
            }

            @Override
            public void onFailure(Call<List<NewRobot>> call, Throwable t) {
                Toast.makeText(context, R.string.check_connection, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void showGame(NewGame game) {
        TextView title = (TextView) findViewById(R.id.gameDetailTitle);
        ImageView icon = (ImageView) findViewById(R.id.gameDetailIcon);
        TextView description = (TextView) findViewById(R.id.gameDescription);
        ImageView gamePic = (ImageView) findViewById(R.id.game_pic);

        title.setText(game.getName());
        icon.setImageResource(game.getIconId());
        description.setText(game.getDescription());
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
            case R.id.start:
//                switch (game.getName()) {
                    downloadRobotList(game.getName());
//                    case R.string.label_tictac:
//                        downloadRobotList("TIC_TAC_TOE");
//                        break;
//                    case R.string.label_tag:
//                        downloadRobotList("TAG");
//                        break;
//                    case R.string.label_moving:
//                        break;
//                    case R.string.label_follower:
//                        downloadRobotList("LINE_FOLLOWER");
//                        break;
//                }
//                break;

            case R.id.play:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startPlaying();
                    }
                }).start();
                break;
            case R.id.stop:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        stopPlaying();
                    }
                }).start();
                break;

        }

    }

    private void stopPlaying() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showStopDialog();
                }
            });

            Socket client = new Socket();
            client.connect(new InetSocketAddress(robotIp, port), 10000);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("STOP");
            DataInputStream in = new DataInputStream(client.getInputStream());
            String response = in.readUTF();
            System.out.println(response);
            client.close();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    play.setVisibility(View.VISIBLE);
                    play.setEnabled(true);
                    stop.setVisibility(View.GONE);
                    stop.setEnabled(false);
                    Toast.makeText(context, R.string.stopped_game, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            });
        } catch(Exception e) {
            e.printStackTrace();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, R.string.cannot_connect, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showStopDialog() {
        progress = new ProgressDialog(GameActivity.this, R.style.AppTheme_Dialog);
        progress.setTitle(R.string.stopping);
        progress.setMessage("Stopping game");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setProgressNumberFormat(null);
        progress.setProgressPercentFormat(null);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }

    private void showRobotsPopupList(final String gameCode) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(GameActivity.this);
        builderSingle.setIcon(R.drawable.robot);
        builderSingle.setTitle(R.string.choose_robot);
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(GameActivity.this, android.R.layout.select_dialog_singlechoice);

        for(NewRobot robot : robotsList) {
            arrayAdapter.add(robot.getRobot_ip());
        }

        builderSingle.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        if(arrayAdapter.isEmpty()) {
            builderSingle.setMessage("You need to pair any robot with your account first.");
        }

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedRobotIp = arrayAdapter.getItem(which);
                        robotIp = selectedRobotIp;
                        launchProgressDialog(robotIp, gameCode);
                    }
                });
        builderSingle.show();
    }

    private void launchProgressDialog(final String robotIp, final String gameCode) {
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
                    port = 3456;
                    startPolling(robotIp, port, gameCode);

                } catch (Exception e) {
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(progress.isShowing()) {
                                Toast.makeText(context, R.string.cannot_connect, Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }

                        }
                    });
                }
            }
        }).start();
    }

    private void startPolling(String robotIp, int port, String gameCode) throws IOException, InterruptedException {
        boolean flag = true;
        while(flag) {
            Socket client = new Socket();
            client.connect(new InetSocketAddress(robotIp, port), 10000);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF(gameCode);

            DataInputStream in = new DataInputStream(client.getInputStream());
            String response = in.readUTF();
            switch (response) {
                case "DOWNLOADING":
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.setTitle(R.string.download);
                            progress.setMessage("Downloading game.. ");
                        }
                    });

                    break;
                case "READY":
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                            Toast.makeText(context, R.string.game_is_ready, Toast.LENGTH_SHORT).show();
                            start.setVisibility(View.GONE);
                            start.setEnabled(false);
                            play.setVisibility(View.VISIBLE);
                            play.setEnabled(true);
                        }
                    });
                    flag = false;
                    break;
            }

            client.close();
        }

    }

    private void startPlaying() {
        try {
            Socket client = new Socket();
            client.connect(new InetSocketAddress(robotIp, port), 10000);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF(gameCode);
            DataInputStream in = new DataInputStream(client.getInputStream());
            String response = in.readUTF();
            System.out.println(response);
            client.close();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    play.setVisibility(View.GONE);
                    play.setEnabled(false);
                    stop.setVisibility(View.VISIBLE);
                    stop.setEnabled(true);
                    Toast.makeText(context, R.string.started_game, Toast.LENGTH_SHORT).show();
                }
            });
        } catch(Exception e) {
            e.printStackTrace();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, R.string.cannot_connect, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}

