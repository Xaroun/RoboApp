package roboniania.com.roboniania_android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.adapter.AdapterEduList;
import roboniania.com.roboniania_android.adapter.RecyclerItemClickListener;
import roboniania.com.roboniania_android.adapter.model.Edu;

/**
 * Created by Mateusz on 04.05.2016.
 */
public class EduListActivity extends AppCompatActivity {

    private RecyclerView eduList;
    private AdapterEduList adapterEduList;
    private Context context;
    private List<Edu> edus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        context = getApplicationContext();
        initializeList();
    }

    private void initializeList() {
        eduList = (RecyclerView) findViewById(R.id.recyclerList);
        adapterEduList = new AdapterEduList(this, getEdus());
        eduList.setAdapter(adapterEduList);
        eduList.setLayoutManager(new LinearLayoutManager(this));
        eduList.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Edu edu = edus.get(position);
                        startEduActivity(edu);
                    }
                })
        );
    }

    private void startEduActivity(Edu edu) {
        Intent i = new Intent(this, EduActivity.class);
        i.putExtra(EduActivity.EDU_EXTRA_KEY, edu);
        startActivity(i);
    }

    public List<Edu> getEdus() {
        edus = new ArrayList<>();
        int[] icons = {
                R.drawable.edu_icon1,
                R.drawable.edu_icon2,
                R.drawable.edu_icon3
        };

        String[] titles = {
                "LETTERS",
                "ANIMALS",
                "ENGLISH"
        };

        for (int i = 0; i < icons.length && i <titles.length; i++) {
            Edu currentEdu = new Edu();
            currentEdu.setIconId(icons[i]);
            currentEdu.setTitle(titles[i]);
            edus.add(currentEdu);
        }

        return edus;
    }

}
