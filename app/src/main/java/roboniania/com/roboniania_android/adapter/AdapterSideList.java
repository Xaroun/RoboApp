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
import roboniania.com.roboniania_android.adapter.model.SideElement;

/**
 * Created by Mateusz on 04.05.2016.
 */
public class AdapterSideList extends RecyclerView.Adapter<AdapterSideList.MyViewHolder> {

    private LayoutInflater inflater;
    private List<SideElement> elements = Collections.emptyList();

    public AdapterSideList(Context context, List<SideElement> elements) {
        inflater = LayoutInflater.from(context);
        this.elements = elements;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.side_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SideElement currentSide = elements.get(position);
        holder.sideTitle.setText(currentSide.getTitle());
        holder.sidePic.setImageResource(currentSide.getIconId());
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView sideTitle;
        private ImageView sidePic;

        public MyViewHolder(View itemView) {
            super(itemView);
            sideTitle = (TextView) itemView.findViewById(R.id.sideTitle);
            sidePic = (ImageView) itemView.findViewById(R.id.sideIcon);
        }

    }

}
