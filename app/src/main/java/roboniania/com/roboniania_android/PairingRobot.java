package roboniania.com.roboniania_android;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import roboniania.com.roboniania_android.api.RoboService;
import roboniania.com.roboniania_android.api.model.Robot;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

/**
 * Created by Mateusz on 04.05.2016.
 */
public class PairingRobot {

    private static final String TAG = PairingRobot.class.getSimpleName();

    public static void showPairDialog(final Context context, final SharedPreferenceStorage userLocalStorage) {

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
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    Robot robot = response.body();

                    Log.d(TAG, Integer.toString(statusCode));

                    Log.d(TAG,robot.getIp() + " || " + robot.getSn() + " || " + robot.getUuid());
                    Toast.makeText(context, R.string.successfully_paired, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, R.string.wrong_match, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, Integer.toString(statusCode));
                    Log.d(TAG, "Pair-key and token doesn't match");
                }

            }

            @Override
            public void onFailure(Call<Robot> call, Throwable t) {
                Toast.makeText(context, R.string.check_connection, Toast.LENGTH_SHORT).show();
            }
        });

    }


}
