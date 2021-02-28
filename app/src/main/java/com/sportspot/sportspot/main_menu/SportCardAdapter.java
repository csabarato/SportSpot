package com.sportspot.sportspot.main_menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sportspot.sportspot.R;

import java.util.ArrayList;
import java.util.Arrays;

public class SportCardAdapter extends RecyclerView.Adapter<SportCardAdapter.SportCardViewHolder> {

    private ArrayList<String> sports;
    private Context context;

    public SportCardAdapter(Context context) {
        this.context = context;
        this.sports = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.sport_type_items_array)));
    }

    @NonNull
    @Override
    public SportCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SportCardViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_sport_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SportCardViewHolder holder, int position) {
        holder.sportTitleTextView.setText(sports.get(position));
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

    class SportCardViewHolder extends RecyclerView.ViewHolder {

        TextView sportTitleTextView;

        public SportCardViewHolder(@NonNull View itemView) {
            super(itemView);
            sportTitleTextView = itemView.findViewById(R.id.dashboard_sport_type);
        }
    }
}
