package com.mxi.goalkeeper.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.model.game_type;
import com.mxi.goalkeeper.network.AppController;
import com.mxi.goalkeeper.network.CommanClass;
import com.mxi.goalkeeper.network.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Games extends Fragment {

    RecyclerView recycler_view;
    String games;
    CommanClass cc;
    ProgressDialog pDialog;
    public static ArrayList<game_type> currentlist;
    Boolean checked = false;
    ImageView iv_no_image;

    public Games(String games) {
        this.games = games;
    }

    @SuppressLint("ValidFragment")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_games, container, false);
        cc = new CommanClass(getActivity());

        recycler_view = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        iv_no_image = (ImageView) rootView.findViewById(R.id.iv_no_image);

        pDialog = new ProgressDialog(getActivity());

        if (games.equals("games")) {
            if (cc.isConnectingToInternet()) {
                pDialog.setMessage("Please wait...");
                pDialog.show();
                pDialog.setCancelable(false);
                makeJsonCurrentGames("Current Games");
            } else {
                cc.showSnackbar(recycler_view, getString(R.string.no_internet));
            }
        } /*else if (games.equals("Past")) {
            if (cc.isConnectingToInternet()) {
                pDialog.setMessage("Please wait...");
                pDialog.show();
                pDialog.setCancelable(false);
                makeJsonCurrentGames("Past Games");
            } else {
                cc.showSnackbar(recycler_view, getString(R.string.no_internet));
            }
        }*/

        return rootView;
    }

    private void makeJsonCurrentGames(final String games) {
        AppController.getInstance().getRequestQueue().getCache().clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_view_player_games,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:player_games", response);
                        jsonParseLogin(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showSnackbar(recycler_view, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("type", games);

                Log.i("request player_games", params.toString());

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header", headers.toString());
                return headers;
            }


        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParseLogin(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            currentlist = new ArrayList<game_type>();
            if (jsonObject.getString("status").equals("200")) {

                JSONArray player_data = jsonObject.getJSONArray("player_data");

                for (int i = 0; i < player_data.length(); i++) {
                    JSONObject jsonObject1 = player_data.getJSONObject(i);

                    game_type gt = new game_type();
                    gt.setId(jsonObject1.getString("id"));
                    gt.setTeam_name(jsonObject1.getString("team_name"));
                    gt.setGame_date(cc.dateConvert(jsonObject1.getString("game_date")));
                    gt.setGame_start_time(jsonObject1.getString("game_start_time"));
                    gt.setDuration(jsonObject1.getString("duration"));
                    gt.setGame_type(jsonObject1.getString("game_type"));
                    gt.setGround_size(jsonObject1.getString("ground_size"));
                    gt.setLevel(jsonObject1.getString("level"));
                    gt.setP_id(jsonObject1.getString("p_id"));
                    gt.setRequest_id(jsonObject1.getString("request_id"));
                    gt.setApplicant_status(jsonObject1.getString("applicant_status"));
                    gt.setRequestor_status(jsonObject1.getString("requestor_status"));
                    gt.setFull_name(jsonObject1.getString("full_name"));
                    gt.setUser_id(jsonObject1.getString("user_id"));
                    gt.setGame_gender(jsonObject1.getString("game_gender"));
                    gt.setAddress(jsonObject1.getString("address"));
                    gt.setAddress_lat(jsonObject1.getString("address_lat"));
                    gt.setAddress_long(jsonObject1.getString("address_long"));
                    gt.setIs_status(jsonObject1.getString("is_apply"));

                    currentlist.add(gt);
                }
                try {
                    if (!currentlist.isEmpty()) {
                        iv_no_image.setVisibility(View.GONE);
                        recycler_view.setAdapter(new GamesAdapter(getActivity(), currentlist));
                        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    } else {
                        iv_no_image.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                iv_no_image.setVisibility(View.VISIBLE);
                //cc.showSnackbar(recycler_view, jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            pDialog.dismiss();
            Log.e("Error view_player_games", e.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //========================================Apply Players=======================================================================================

    private void makeJsonApplyPlayer(final String requested_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_apply_player,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:apply_player", response);
                        jsonParseDetails(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showSnackbar(recycler_view, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("request_id", requested_id);

                Log.i("request apply_player", params.toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header", headers.toString());
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParseDetails(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                cc.showSnackbar(recycler_view, jsonObject.getString("message"));
            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            pDialog.dismiss();
            Log.e("Error apply_player", e.toString());
        }
    }

    public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.CustomViewHolder> {

        private Context context;
        private ArrayList<game_type> arrayList;

        public GamesAdapter(Context context, ArrayList<game_type> list) {
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
            holder.ll_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (games.equals("games") && list.getIs_status().equals("No")) {
                        showDialogDetails(list.getRequest_id(), i);
                    }
                }
            });
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
                        startActivity(directioIntent);
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
            LinearLayout ll_row;

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

        private void showDialogDetails(final String requested_id, final int position) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.cureent_details_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Button btn_reject = (Button) dialog.findViewById(R.id.btn_reject);
            Button btn_accept = (Button) dialog.findViewById(R.id.btn_accept);
            ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
            final EditText et_price = (EditText) dialog.findViewById(R.id.et_price);
            final EditText et_amount = (EditText) dialog.findViewById(R.id.et_amount);
            final CheckBox cb_free = (CheckBox) dialog.findViewById(R.id.cb_free);

            btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    com.mxi.goalkeeper.fragment.Games.currentlist.remove(position);
                    if (!com.mxi.goalkeeper.fragment.Games.currentlist.isEmpty()) {

                        notifyDataSetChanged();
                   /* recycler_view.setAdapter(new com.mxi.goalkeeper.adapter.Games(getActivity(), currentlist, "games"));
                    recycler_view.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false))*/
                        ;
                    }
                    dialog.dismiss();
                }
            });

            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });
            cb_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    cb_free.setChecked(isChecked);
                    checked = isChecked;
                }
            });
            btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String price = et_price.getText().toString().trim();
                    String amount = et_amount.getText().toString().trim();

                    if (!cc.isConnectingToInternet()) {
                        cc.showToast(context.getString(R.string.no_internet));
                    } else if (price.equals("") && !checked && amount.equals("")) {
                        cc.showToast("Please enter amount for play/Play for free/Pay to play");
                    } else if (!price.equals("") || checked || !amount.equals("")) {

                        pDialog.show();
                        pDialog.setCancelable(false);
                        makeJsonApplyPlayer(requested_id);
                        if (games.equals("games")) {
                            if (cc.isConnectingToInternet()) {
                                pDialog.setMessage("Please wait...");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                makeJsonCurrentGames("Current Games");
                            } else {
                                cc.showToast(context.getString(R.string.no_internet));
                            }
                        }
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        }
    }

}
