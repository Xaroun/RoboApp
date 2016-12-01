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
import roboniania.com.roboniania_android.api.model.NewRobot;
import roboniania.com.roboniania_android.api.model.Robot;
import roboniania.com.roboniania_android.api.model.User;

/**
 * Created by Mateusz on 05.05.2016.
 */
public class AdapterRobotList extends RecyclerView.Adapter<AdapterRobotList.MyViewHolder> {

    private LayoutInflater inflater;
    private List<NewRobot> robots;

    public AdapterRobotList(Context context, List<NewRobot> robots) {
        inflater = LayoutInflater.from(context);
        this.robots = robots;
    }

    public void swap(List<NewRobot> robots) {
        this.robots.clear();
        this.robots.addAll(robots);
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
        NewRobot currentRobot = robots.get(position);
        holder.robotIp.setText(currentRobot.getRobot_ip());
        holder.robotModel.setText(currentRobot.getRobot_model().toString());
        holder.robotSystem.setText(currentRobot.getCurrent_system().toString());

    }

    @Override
    public int getItemCount() {
        return robots.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView robotIp, robotModel, robotSystem;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            robotIp = (TextView) itemView.findViewById(R.id.robotIp);
            robotModel = (TextView) itemView.findViewById(R.id.robotModel);
            robotSystem = (TextView) itemView.findViewById(R.id.robotSystem);
        }

    }

}
