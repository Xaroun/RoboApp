package roboniania.com.roboniania_android.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import roboniania.com.roboniania_android.PairingRobot;
import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.adapter.model.Edu;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

public class EduActivity extends AppCompatActivity {

    public static final String EDU_EXTRA_KEY = "edu";
    private Toolbar toolbar;
    private SharedPreferenceStorage userLocalStorage;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu);

        initComponents();
    }

    private void initComponents() {
        userLocalStorage = new SharedPreferenceStorage(this);
        handler = new Handler();

        //SETTING UP TOOLBAR
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        Edu edu = (Edu) i.getExtras().getSerializable(EDU_EXTRA_KEY);
        showEdu(edu);
    }

    private void showEdu(Edu edu) {
        TextView title = (TextView) findViewById(R.id.eduDetailTitle);
        ImageView photo = (ImageView) findViewById(R.id.eduDetailIcon);

        title.setText(edu.getTitle());
        photo.setImageResource(edu.getIconId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.add:
                PairingRobot.showPairDialog(this, userLocalStorage, handler);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
