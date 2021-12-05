package com.c.retrofit_post;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    JSONArray jsonArray;


    public MainAdapter(JSONArray jsonArray) {

        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            JSONObject object= jsonArray.getJSONObject(position);
            holder.tv_Name.setText(object.getString("name"));
            holder.tv_Trip.setText(String.format("%s Trips", object.getString("trips")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_Name, tv_Trip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Name = itemView.findViewById(R.id.tv_Name);
            tv_Trip = itemView.findViewById(R.id.tv_Trip);
        }
    }
}
