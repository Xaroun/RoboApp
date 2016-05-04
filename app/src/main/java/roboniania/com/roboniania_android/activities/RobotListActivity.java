package roboniania.com.roboniania_android.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.api.RoboService;
import roboniania.com.roboniania_android.api.model.Robot;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class RobotListActivity extends AppCompatActivity {
    
    private final String url = "http://192.168.2.4:8080";
    private SharedPreferenceStorage userLocalStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_list);
        userLocalStorage = new SharedPreferenceStorage(this);

        //TODO
        //-wszędzie gdzie tworzę nowy obiekt sharedpreference, powinienem chyba przekazywać ten stary stworzony raz
        //-po ponownym wejściu w listę gier/edu sidebar ma być schowany (to ma związek z singleInstance w manifeście - bez tego działało, ale pewnie więcej pamięci szło)
        //-dodać sidebar w HomeActivity
        
        getRobotList(this);
    }

    public void getRobotList(final Context context) {
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        RoboService roboService = retrofit.create(RoboService.class);


        Call<List<Robot>> call = roboService.getRobotsList(userLocalStorage.getAccessToken());

        call.enqueue(new Callback<List<Robot>>() {
            @Override
            public void onResponse(Call<List<Robot>> call, Response<List<Robot>> response) {
                if (response.isSuccessful()) {
                    int statusCode = response.code();
                    List<Robot> robots = response.body();
                    for (Robot robot: robots) {
                        System.out.println(robot.getIp() + " || " + robot.getSn() + " || " + robot.getUuid());
                    }

                    System.out.println(statusCode);
//                    Toast.makeText(context, R.string.successfully_paired, Toast.LENGTH_SHORT).show();
//                    finish();

                } else {
//                    Toast.makeText(context, R.string.wrong_match, Toast.LENGTH_SHORT).show();
                    System.out.println("HIUSTON");
                    //TODO catch code error
                }

            }

            @Override
            public void onFailure(Call<List<Robot>> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }
}
