package roboniania.com.roboniania_android.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import roboniania.com.roboniania_android.api.model.NewRobot;
import roboniania.com.roboniania_android.api.model.Robot;
import roboniania.com.roboniania_android.api.model.User;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class RobotListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SharedPreferenceStorage userLocalStorage;
    private RecyclerView robotsList;
    private AdapterRobotList adapterRobotList;
    private List<NewRobot> robotsDownloaded = new ArrayList<>();
    private Toolbar toolbar;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String TAG = RobotListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_list);

        initComponents();
        getRobotList();
    }

    private void initComponents() {
        userLocalStorage = new SharedPreferenceStorage(this);
        context = getApplicationContext();

        //SETTING UP SWIPE REFRESH LAYOUT
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright);

        //SETTING UP TOOLBAR
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //SETTING UP SIDEBAR FRAGMENT
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        //SETTING UP RECYCLER VIEW
        robotsList = (RecyclerView) findViewById(R.id.recyclerList);
        adapterRobotList = new AdapterRobotList(context, robotsDownloaded);
        robotsList.setAdapter(adapterRobotList);
        robotsList.setLayoutManager(new LinearLayoutManager(context));
        robotsList.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        NewRobot robot = robotsDownloaded.get(position);
                        startRobotActivity(robot);
                    }
                })
        );
    }

    private void startRobotActivity(NewRobot robot) {
        Intent i = new Intent(this, RobotActivity.class);
        i.putExtra(RobotActivity.ROBOT_EXTRA_KEY, robot);
        startActivity(i);
    }


    public void getRobotList() {
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
                    List<NewRobot> listOfRobots = response.body();

                    adapterRobotList.swap(listOfRobots);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    @Override
    public void onRefresh() {
        robotsDownloaded.clear();
        getRobotList();
        swipeRefreshLayout.setRefreshing(false);
    }

}
