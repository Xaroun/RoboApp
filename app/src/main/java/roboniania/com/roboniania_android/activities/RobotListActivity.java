package roboniania.com.roboniania_android.activities;

import android.content.Context;
import android.os.Handler;
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
import android.widget.ViewAnimator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.IOException;
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
import roboniania.com.roboniania_android.api.network.NetworkProvider;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class RobotListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SharedPreferenceStorage userLocalStorage;
    private RecyclerView robotsList;
    private AdapterRobotList adapterRobotList;
    private List<Robot> robotsDownloaded = new ArrayList<>();
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_list);
        userLocalStorage = new SharedPreferenceStorage(this);
        handler = new Handler();

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

        initializeList();
    }

    private void initializeList() {
        robotsList = (RecyclerView) findViewById(R.id.recyclerList);
        getRobotList();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        adapterRobotList = new AdapterRobotList(this, robotsDownloaded);
        robotsList.setAdapter(adapterRobotList);
        robotsList.setLayoutManager(new LinearLayoutManager(this));
    }


    public void getRobotList() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                downloadRobotList();
            }
        }).start();
    }

    private void downloadRobotList() {
        final NetworkProvider networkProvider = new NetworkProvider(this, userLocalStorage);
        try {
            robotsDownloaded = networkProvider.getRobotList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                PairingRobot.showPairDialog(this, userLocalStorage, handler);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        robotsDownloaded.clear();
        initializeList();
        swipeRefreshLayout.setRefreshing(false);
    }

}
