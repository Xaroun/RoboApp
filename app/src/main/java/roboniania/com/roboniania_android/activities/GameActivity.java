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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import roboniania.com.roboniania_android.PairingRobot;
import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.adapter.model.Game;
import roboniania.com.roboniania_android.api.RoboService;
import roboniania.com.roboniania_android.api.model.NewRobot;
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
    private List<NewRobot> robotsList;
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

        downloadRobotList();
    }

    public void downloadRobotList() {
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
                        showRobotsPopupList();
                        break;
                    case R.string.label_moving:
                        break;
                    case R.string.label_follower:
//                        startPlaying("LINE_FOLLOWER");
                        showRobotsPopupList();
                        break;
                }
                break;
        }

    }

    private void showRobotsPopupList() {
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
                    String url = "192.168.2.4";
                    int port = 3456;
//                    connectWithRobot(url);
                    connectWithSockets(url, port);

//                    Thread.sleep(10000);
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

    private void connectWithSockets(String url, int port) {

        try {
            String sentence;
            String modifiedSentence;
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

            Socket clientSocket = new Socket(url, port);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            sentence = inFromUser.readLine();
            outToServer.writeBytes(sentence + '\n');
            modifiedSentence = inFromServer.readLine();
            System.out.println(modifiedSentence);
            clientSocket.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void connectWithRobot(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");

            String body = "{\n" +
                    "\"game\": \"TIC_TAC_TOE\",\n" +
                    "\"id\": \"1\",\n" +
                    "\"command\": \"PREPARE\"\n" +
                    "}";

            //send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(body);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            System.out.println("Post parameters : \n" + body);
            System.out.println("Response Code : " + responseCode);


            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response.toString());



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getRobotUuid() {
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
                    List<NewRobot> robotsList = response.body();

                    if(robotsList.isEmpty())
                        uuid = null;
                    else
                        uuid = robotsList.get(0).getSerial_number();

                    Log.d(TAG, Integer.toString(statusCode));

                } else {
                    uuid = null;
                    Log.d(TAG, Integer.toString(statusCode));
                }
            }

            @Override
            public void onFailure(Call<List<NewRobot>> call, Throwable t) {
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

