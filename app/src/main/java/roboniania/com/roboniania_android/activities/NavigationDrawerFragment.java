package roboniania.com.roboniania_android.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.adapter.AdapterSideList;
import roboniania.com.roboniania_android.adapter.RecyclerItemClickListener;
import roboniania.com.roboniania_android.adapter.model.SideElement;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class NavigationDrawerFragment extends Fragment {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private RecyclerView recyclerView;
    private AdapterSideList adapterSideList;
    private List<SideElement> elements;
    private Context context;
    private ImageView logout;
    private SharedPreferenceStorage userLocalStorage;

    public NavigationDrawerFragment() {
        //nop
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        context = getContext();
        userLocalStorage = new SharedPreferenceStorage(context);

        logout = (ImageView) layout.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

        recyclerView = (RecyclerView) layout.findViewById(R.id.sideBarList);
        adapterSideList = new AdapterSideList(getActivity(), getElements());
        recyclerView.setAdapter(adapterSideList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                        SideElement element = elements.get(position);
                        navigateTo(position);
                    }
                })
        );
        return layout;
    }

    private void navigateTo(int position) {
        Intent i;
        switch(position) {
            case 0:
                i = new Intent(context, HomeActivity.class);
                startActivity(i);
                break;
            case 1:
                i = new Intent(context, AccountActivity.class);
                startActivity(i);
                break;
            case 2:
                i = new Intent(context, RobotListActivity.class);
                startActivity(i);
                break;
            case 3:
                System.out.println("Settings");
                break;
        }
    }


    public List<SideElement> getElements() {
        elements = new ArrayList<>();
        int[] icons = {
                R.drawable.home,
                R.drawable.account,
                R.drawable.robot,
                R.drawable.settings

        };

        String[] titles = {
                "Home",
                "Account",
                "Robots list",
                "Settings"
        };

        for (int i = 0; i < icons.length && i <titles.length; i++) {
            SideElement currentElement = new SideElement();
            currentElement.setIconId(icons[i]);
            currentElement.setTitle(titles[i]);
            elements.add(currentElement);
        }

        return elements;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(slideOffset < 0.6) {
                    toolbar.setAlpha(1-slideOffset);
                }

            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    private void showLogoutDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage("Are you sure you want to logout?");
        alert.setTitle("Signing out..");

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                userLocalStorage.clearUserData();
                Toast.makeText(context, R.string.successfully_logged_out, Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
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
