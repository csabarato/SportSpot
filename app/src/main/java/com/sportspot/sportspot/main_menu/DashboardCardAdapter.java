package com.sportspot.sportspot.main_menu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sportspot.sportspot.R;
import com.sportspot.sportspot.constants.SportType;
import com.sportspot.sportspot.menus.activity_map.ActivitiesMapActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class DashboardCardAdapter extends RecyclerView.Adapter<DashboardCardAdapter.DashboardCardViewHolder> {

    private final ArrayList<SportType> sportTypes;
    private final Context context;

    public DashboardCardAdapter(Context context) {
        this.context = context;
        this.sportTypes = new ArrayList<>(Arrays.asList(SportType.values()));
    }

    @NonNull
    @Override
    public DashboardCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DashboardCardViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_sport_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardCardViewHolder holder, int position) {
        holder.sportTitleTextView.setText(sportTypes.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return sportTypes.size();
    }

    class DashboardCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView sportTitleTextView;

        public DashboardCardViewHolder(@NonNull View itemView) {
            super(itemView);
            sportTitleTextView = itemView.findViewById(R.id.dashboard_sport_type);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            SportType selectedSport = sportTypes.get(getAdapterPosition());

            Intent intent = new Intent(context, ActivitiesMapActivity.class);
            intent.putExtra("sportTypeFilter", selectedSport);
            context.startActivity(intent);
        }
    }
}
