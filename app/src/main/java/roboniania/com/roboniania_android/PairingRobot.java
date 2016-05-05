package roboniania.com.roboniania_android;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import roboniania.com.roboniania_android.activities.RobotListActivity;
import roboniania.com.roboniania_android.api.RoboService;
import roboniania.com.roboniania_android.api.model.Robot;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

/**
 * Created by Mateusz on 04.05.2016.
 */
public class PairingRobot {

    private static RobotListActivity robotListActivity;

    public PairingRobot(RobotListActivity robotListActivity) {
        //NEED TO PASS IT JUST FOR REFRESHING ACTIVITY AFTER PAIRING
        this.robotListActivity = robotListActivity;
    }

    public static void showPairDialog(final Context context, final SharedPreferenceStorage userLocalStorage) {

        robotListActivity.getApplicationContext();

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final EditText pairKey = new EditText(context);
        pairKey.setTextColor(Color.RED);
        pairKey.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setMessage("Enter robot's pair-key:");
        alert.setTitle("Connecting..");
        alert.setView(pairKey);


        alert.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                checkPairKey(pairKey.getText().toString(), userLocalStorage, context);
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    public static void checkPairKey(String pairKey, SharedPreferenceStorage userLocalStorage, final Context context) {
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RoboService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        RoboService roboService = retrofit.create(RoboService.class);


        Call<Robot> call = roboService.getRobot(pairKey, userLocalStorage.getAccessToken());

        call.enqueue(new Callback<Robot>() {
            @Override
            public void onResponse(Call<Robot> call, Response<Robot> response) {
                if (response.isSuccessful()) {
                    int statusCode = response.code();
                    Robot robot = response.body();

//                    System.out.println(statusCode);
//                    System.out.println(robot.getIp());
//                    System.out.println(robot.getSn());
//                    System.out.println(robot.getUuid());
                    Toast.makeText(context, R.string.successfully_paired, Toast.LENGTH_SHORT).show();
                    robotListActivity.finish();
                    robotListActivity.startActivity(robotListActivity.getIntent());
//                    finish();

                } else {
                    Toast.makeText(context, R.string.wrong_match, Toast.LENGTH_SHORT).show();
                    System.out.println("pair-key and token doesn't match");
                    //TODO catch code error
                }

            }

            @Override
            public void onFailure(Call<Robot> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
