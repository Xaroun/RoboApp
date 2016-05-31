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


import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String TAG = RobotListActivity.class.getSimpleName();
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_list);

        initComponents();
        getRobotList();
    }

    private void initComponents() {
        userLocalStorage = new SharedPreferenceStorage(this);
        handler = new Handler();
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
            networkProvider.getRobotList(new NetworkProvider.OnResponseReceivedListener() {
                @Override
                public void onResponseReceived() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapterRobotList.swap(networkProvider.getRobots());
                        }
                    });

                }
            });
            robotsDownloaded.addAll(networkProvider.getRobots());
        } catch (IOException e) {
            Log.d(TAG, "IO Exception.");
        } catch (JSONException e) {
            Log.d(TAG, "Problems with JSON.");
        }
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
                PairingRobot.showPairDialog(this, userLocalStorage, handler);
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
