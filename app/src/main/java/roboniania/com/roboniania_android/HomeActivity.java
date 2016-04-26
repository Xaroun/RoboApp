package roboniania.com.roboniania_android;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import roboniania.com.roboniania_android.api.RoboService;
import roboniania.com.roboniania_android.api.model.Robot;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class HomeActivity extends AppCompatActivity {

    private Context context;
    private SharedPreferenceStorage userLocalStorage;
    private final String url = "http://192.168.2.4:8080";
    private boolean paired = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = getApplicationContext();
        userLocalStorage = new SharedPreferenceStorage(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add:
                showPairDialog();
                if(paired)
                    item.setVisible(false);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showPairDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText pairKey = new EditText(context);
        pairKey.setTextColor(Color.RED);
        alert.setMessage("Enter robot's pair-key:");
        alert.setTitle("Connecting..");
        alert.setView(pairKey);


        alert.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                checkPairKey(pairKey.getText().toString());
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                paired = false;
            }
        });

        alert.show();
    }

    public void checkPairKey(String pairKey) {
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
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

                    System.out.println(statusCode);
                    System.out.println(robot.getIp());
                    System.out.println(robot.getSn());
                    System.out.println(robot.getUuid());
                    Toast.makeText(context, R.string.successfully_paired, Toast.LENGTH_SHORT).show();
                    paired = true;
//                    finish();

                } else {
                    Toast.makeText(context, R.string.wrong_match, Toast.LENGTH_SHORT).show();
                    paired = false;
                    System.out.println("pair-key and token doesn't match");
                    //TODO catch code error
                }

            }

            @Override
            public void onFailure(Call<Robot> call, Throwable t) {
                paired = false;
                t.printStackTrace();
            }

        });

        }
    }
