package roboniania.com.roboniania_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import roboniania.com.roboniania_android.R;
import roboniania.com.roboniania_android.adapter.model.Game;
import roboniania.com.roboniania_android.api.model.Robot;
import roboniania.com.roboniania_android.api.model.User;

/**
 * Created by Mateusz on 05.05.2016.
 */
public class AdapterRobotList extends RecyclerView.Adapter<AdapterRobotList.MyViewHolder> {

    private LayoutInflater inflater;
    private List<Robot> robots = Collections.emptyList();

    public AdapterRobotList(Context context, List<Robot> robots) {
        inflater = LayoutInflater.from(context);
        this.robots = robots;
    }

    // Clean all elements of the recycler
    public void clear() {
        robots.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Robot> list) {
        robots.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.robot_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Robot currentRobot = robots.get(position);
        holder.robotIp.setText(currentRobot.getIp());
        holder.robotSn.setText(currentRobot.getSn());

//        holder.robotUuid.setText(currentRobot.getUuid());

    }

    @Override
    public int getItemCount() {
        return robots.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView robotIp, robotSn, robotUuid;

        public MyViewHolder(View itemView) {
            super(itemView);
            robotIp = (TextView) itemView.findViewById(R.id.robotIp);
            robotSn = (TextView) itemView.findViewById(R.id.robotSn);
//            robotUuid = (TextView) itemView.findViewById(R.id.robotUuid);
        }

    }

}
