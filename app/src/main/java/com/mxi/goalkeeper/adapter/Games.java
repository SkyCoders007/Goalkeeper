package com.mxi.goalkeeper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.model.game_type;

import java.util.ArrayList;

/**
 * Created by sonali on 10/3/17.
 */
public class Games extends RecyclerView.Adapter<Games.CustomViewHolder> {

    private Context context;
    private ArrayList<game_type> arrayList;

    public Games(Context context, ArrayList<game_type> list) {
        this.context = context;
        arrayList = ((ArrayList) list);

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.row_games, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {

        final game_type list = arrayList.get(i);
        holder.tv_title.setText(list.getTeam_name());
        holder.tv_date.setText(list.getGame_date() + " " + " " + " " + list.getGame_start_time());
        holder.tv_time.setText(list.getDuration() + " " + "MIN");
        holder.tv_surface.setText("Surface" + " : " + list.getGame_type());
        holder.tv_team_size.setText("Team size" + " : " + list.getGround_size());
        holder.tv_gender.setText("Gender" + " : " + list.getGame_gender());
        holder.tv_team_category.setText("Game Caliber" + " : " + list.getLevel());
        holder.tv_location.setText(list.getAddress());
        holder.tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double latitude = Double.parseDouble(list.getAddress_lat());
                double longitude = Double.parseDouble(list.getAddress_long());
                try {
                    Intent directioIntent = new Intent(
                            android.content.Intent.ACTION_VIEW, Uri
                            .parse("http://maps.google.com/maps?daddr="
                                    + latitude + "," + longitude));
                    directioIntent.setClassName("com.google.android.apps.maps",
                            "com.google.android.maps.MapsActivity");
                    context.startActivity(directioIntent);
                } catch (Exception e) {
                    Log.e("MAP Error", e.getMessage());
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_title, tv_date, tv_time, tv_surface, tv_team_size, tv_gender, tv_team_category, tv_location;
        protected LinearLayout ll_row;

        public CustomViewHolder(View convertView) {
            super(convertView);

            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            tv_surface = (TextView) convertView.findViewById(R.id.tv_surface);
            tv_team_size = (TextView) convertView.findViewById(R.id.tv_team_size);
            tv_gender = (TextView) convertView.findViewById(R.id.tv_gender);
            tv_team_category = (TextView) convertView.findViewById(R.id.tv_team_category);
            tv_location = (TextView) convertView.findViewById(R.id.tv_location);
            ll_row = (LinearLayout) convertView.findViewById(R.id.ll_row);

        }
    }

}
