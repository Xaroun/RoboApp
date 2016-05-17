package roboniania.com.roboniania_android.activities;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import roboniania.com.roboniania_android.PairingRobot;
import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.adapter.AdapterRobotList;
import roboniania.com.roboniania_android.adapter.RecyclerItemClickListener;
import roboniania.com.roboniania_android.api.RoboService;
import roboniania.com.roboniania_android.api.model.Robot;
import roboniania.com.roboniania_android.api.model.User;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class RobotListActivity extends AppCompatActivity {

    private SharedPreferenceStorage userLocalStorage;
    private RecyclerView robotsList;
    private AdapterRobotList adapterRobotList;
    private Context context;
    private List<Robot> robots;
    private Toolbar toolbar;
//    private PairingRobot pairingRobot;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_list);
        userLocalStorage = new SharedPreferenceStorage(this);
//        pairingRobot = new PairingRobot(this);

        initializeList();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                adapterRobotList.clear();

//                robots.clear();
//                adapterRobotList.notifyDataSetChanged();
//                robots.addAll(getRobotList());
//                adapterRobotList.notifyDataSetChanged();
//                swipeRefreshLayout.setRefreshing(false);

            }
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        //SETTING UP TOOLBAR
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //SETTING UP SIDEBAR FRAGMENT
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

    }

    private void initializeList() {
        robotsList = (RecyclerView) findViewById(R.id.recyclerList);
        adapterRobotList = new AdapterRobotList(this, getRobotList());
        robotsList.setAdapter(adapterRobotList);
        robotsList.setLayoutManager(new LinearLayoutManager(this));
    }


    public List<Robot> getRobotList() {

        robots = new ArrayList<>();

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
                if (response.isSuccessful()) {
                    int statusCode = response.code();
                    User user = response.body();


                    for (Robot robot : user.getRobots()) {
                        robots.add(robot);
                        System.out.println(robot.getIp() + " || " + robot.getSn() + " || " + robot.getUuid());
                    }

                    System.out.println(statusCode);

                } else {
                    System.out.println("HIUSTON");
                    //TODO catch code error
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }

        });

        return robots;
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
                PairingRobot.showPairDialog(this, userLocalStorage);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
