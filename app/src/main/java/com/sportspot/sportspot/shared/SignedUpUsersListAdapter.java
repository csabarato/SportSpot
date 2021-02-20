package com.sportspot.sportspot.shared;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sportspot.sportspot.R;
import com.sportspot.sportspot.model.UserDataModel;

import java.util.List;

public class SignedUpUsersListAdapter extends RecyclerView.Adapter<SignedUpUsersListAdapter.UserViewHolder> {

    private final List<UserDataModel> userList;
    private LayoutInflater layoutInflater;

    public SignedUpUsersListAdapter(Context context, List<UserDataModel> userList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.userList = userList;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        public final TextView userItemView;
        final SignedUpUsersListAdapter adapter;

        public UserViewHolder(@NonNull View itemView, SignedUpUsersListAdapter adapter) {
            super(itemView);
            this.userItemView = itemView.findViewById(R.id.signed_up_user);
            this.adapter = adapter;
        }
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = layoutInflater.inflate(R.layout.signed_up_users_list_item, parent, false);
        return new UserViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.userItemView.setText(userList.get(position).getDisplayName());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
