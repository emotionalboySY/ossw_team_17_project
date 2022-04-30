package com.cauossw.snake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RankingViewAdapter extends RecyclerView.Adapter<RankingViewAdapter.ViewHolder> {

    private ArrayList<RankData> mData;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView score;
        TextView time;

        ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.ranking_item_name);
            score = itemView.findViewById(R.id.ranking_item_score);
            time = itemView.findViewById(R.id.ranking_item_time);
        }
    }

    RankingViewAdapter(ArrayList<RankData> list) {
        mData = list;
    }

    @NonNull
    @Override
    public RankingViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.ranking_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = mData.get(position).getName();
        String score = mData.get(position).getScore();
        String time = mData.get(position).getTime();

        holder.title.setText(name);
        holder.score.setText(score);
        holder.time.setText(time);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
