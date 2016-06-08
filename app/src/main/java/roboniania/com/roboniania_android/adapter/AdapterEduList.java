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
import roboniania.com.roboniania_android.adapter.model.Edu;

/**
 * Created by Mateusz on 04.05.2016.
 */
public class AdapterEduList extends RecyclerView.Adapter<AdapterEduList.MyViewHolder> {

    private LayoutInflater inflater;
    private List<Edu> edus = Collections.emptyList();

    public AdapterEduList(Context context, List<Edu> edus) {
        inflater = LayoutInflater.from(context);
        this.edus = edus;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.edu_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Edu currentEdu = edus.get(position);
        holder.eduTitle.setText(currentEdu.getTitleId());
        holder.eduPic.setImageResource(currentEdu.getIconId());
    }

    @Override
    public int getItemCount() {
        return edus.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView eduTitle;
        private ImageView eduPic;

        public MyViewHolder(View itemView) {
            super(itemView);
            eduTitle = (TextView) itemView.findViewById(R.id.eduTitle);
            eduPic = (ImageView) itemView.findViewById(R.id.eduIcon);
        }

    }

}