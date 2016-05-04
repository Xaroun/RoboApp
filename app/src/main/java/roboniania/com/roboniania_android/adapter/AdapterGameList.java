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

/**
 * Created by Mateusz on 03.05.2016.
 */
public class AdapterGameList extends RecyclerView.Adapter<AdapterGameList.MyViewHolder> {

    private LayoutInflater inflater;
    private List<Game> games = Collections.emptyList();

    public AdapterGameList(Context context, List<Game> games) {
        inflater = LayoutInflater.from(context);
        this.games = games;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.game_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Game currentGame = games.get(position);
        holder.gameTitle.setText(currentGame.getTitle());
        holder.gamePic.setImageResource(currentGame.getIconId());

    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView gameTitle;
        private ImageView gamePic;

        public MyViewHolder(View itemView) {
            super(itemView);
            gameTitle = (TextView) itemView.findViewById(R.id.gameTitle);
            gamePic = (ImageView) itemView.findViewById(R.id.gameIcon);
        }

    }

}
