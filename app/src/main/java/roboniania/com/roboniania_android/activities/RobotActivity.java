package roboniania.com.roboniania_android.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.api.RoboService;
import roboniania.com.roboniania_android.api.model.NewRobot;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class RobotActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ROBOT_EXTRA_KEY = "robot";
    private static final String TAG = RobotActivity.class.getSimpleName();
    private SharedPreferenceStorage userLocalStorage;
    private Context context;
    private Toolbar toolbar;
    private TextView robotId, robotIp, robotModel, robotConfig, robotLastSeen;
    private Button unpairButton;
    private NewRobot newRobot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot);

        initComponents();
        loadRobotData();
    }

    private void initComponents() {
        userLocalStorage = new SharedPreferenceStorage(this);
        context = getApplicationContext();

        robotId = (TextView) findViewById(R.id.robot_id);
        robotIp = (TextView) findViewById(R.id.robot_ip);
        robotModel = (TextView) findViewById(R.id.robot_model);
        robotConfig = (TextView) findViewById(R.id.robot_config);
        robotLastSeen = (TextView) findViewById(R.id.robot_last_seen);
        unpairButton = (Button) findViewById(R.id.unpair_button);
        unpairButton.setOnClickListener(this);

        //SETTING UP TOOLBAR
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadRobotData() {
        Intent intent = getIntent();
        newRobot = (NewRobot) intent.getSerializableExtra(ROBOT_EXTRA_KEY);
        if(newRobot != null) {
            setRobotData(newRobot);
        }
    }

    private void setRobotData(NewRobot robot) {
        robotId.setText(robot.getRobot_id());
        robotIp.setText(robot.getRobot_ip());
        robotModel.setText(robot.getRobot_model().toString());
        robotConfig.setText(robot.getCurrent_lego_construction().getName());
        robotLastSeen.setText(robot.getLast_seen());
    }


    @Override
    public void onClick(View v) {
        showConfirmationDialog();
    }

    private void unpairRobot() {
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RoboService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RoboService roboService = retrofit.create(RoboService.class);

        Call<Void> call = roboService.unpairRobot(userLocalStorage.getAccessToken(), newRobot.getRobot_id());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    Log.d(TAG, Integer.toString(statusCode));
                    Toast.makeText(context, R.string.successfully_unpaired, Toast.LENGTH_SHORT).show();
                    finish();
                    Intent i = new Intent(context, RobotListActivity.class);
                    startActivity(i);

                } else {
                    Log.d(TAG, Integer.toString(statusCode));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, R.string.check_connection, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(R.string.confirm_unpair);
        alert.setTitle(R.string.unpair_title);

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                unpairRobot();
            }
        });

        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.create();
        alert.show();
    }

}
