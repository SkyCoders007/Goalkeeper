package com.mxi.goalkeeper.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.fragment.*;
import com.mxi.goalkeeper.fragment.MyCredit;
import com.mxi.goalkeeper.network.AppController;
import com.mxi.goalkeeper.network.CommanClass;
import com.mxi.goalkeeper.network.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressDialog pDialog;
    CommanClass cc;
    private Toolbar toolbar;

    Button btn_games, btn_my_games, btn_post, btn_credits, btn_more;
    TextView tvCount;
    LinearLayout ll_linear;
    FrameLayout recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            cc = new CommanClass(this);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            pDialog = new ProgressDialog(MainActivity.this);
            recycler_view = (FrameLayout) findViewById(R.id.recycler_view);

            tvCount = (TextView) findViewById(R.id.tvCount);
            ll_linear = (LinearLayout) findViewById(R.id.ll_linear);
            btn_games = (Button) findViewById(R.id.btn_games);
            btn_my_games = (Button) findViewById(R.id.btn_my_games);
            btn_post = (Button) findViewById(R.id.btn_post);
            btn_credits = (Button) findViewById(R.id.btn_credits);
            btn_more = (Button) findViewById(R.id.btn_more);

            btn_games.setOnClickListener(this);
            btn_my_games.setOnClickListener(this);
            btn_post.setOnClickListener(this);
            btn_credits.setOnClickListener(this);
            btn_more.setOnClickListener(this);

            tvCount.setBackgroundResource(R.mipmap.ic_noti_white);
            tvCount.setTextColor(Color.parseColor("#DA3C3A"));
            btn_games.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_games.setTextColor(Color.parseColor("#DA3C3A"));
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.recycler_view, new Games("games"));
            fragmentTransaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

       /* if (Integer.parseInt(cc.loadPrefString("user_type_count")) >= 1 && !cc.loadPrefString("isManager").equals("Team_Manager")) {
            btn_games.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_games.setTextColor(Color.parseColor("#DA3C3A"));
            btn_post.setVisibility(View.GONE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.recycler_view, new Games("games"));
            fragmentTransaction.commit();
        } else if (Integer.parseInt(cc.loadPrefString("user_type_count")) > 1 && cc.loadPrefString("isManager").equals("Team_Manager")) {

            btn_games.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_games.setTextColor(Color.parseColor("#DA3C3A"));
            btn_my_games.setBackgroundColor(Color.parseColor("#DA3C3A"));
            btn_my_games.setTextColor(Color.parseColor("#FFFFFF"));
            btn_post.setBackgroundColor(Color.parseColor("#DA3C3A"));
            btn_post.setTextColor(Color.parseColor("#FFFFFF"));
            btn_more.setBackgroundColor(Color.parseColor("#DA3C3A"));
            btn_more.setTextColor(Color.parseColor("#FFFFFF"));
            btn_credits.setBackgroundColor(Color.parseColor("#DA3C3A"));
            btn_credits.setTextColor(Color.parseColor("#FFFFFF"));

            Fragment fragment1 = new Games("games");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recycler_view, fragment1, fragment1.getClass().getSimpleName()).commit();
        } else if (Integer.parseInt(cc.loadPrefString("user_type_count")) == 1 && cc.loadPrefString("isManager").equals("Team_Manager")) {
            btn_games.setVisibility(View.GONE);
            btn_games.setBackgroundColor(Color.parseColor("#DA3C3A"));
            btn_games.setTextColor(Color.parseColor("#FFFFFF"));
            btn_my_games.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_my_games.setTextColor(Color.parseColor("#DA3C3A"));

            btn_post.setBackgroundColor(Color.parseColor("#DA3C3A"));
            btn_post.setTextColor(Color.parseColor("#FFFFFF"));
            btn_more.setBackgroundColor(Color.parseColor("#DA3C3A"));
            btn_more.setTextColor(Color.parseColor("#FFFFFF"));
            btn_credits.setBackgroundColor(Color.parseColor("#DA3C3A"));
            btn_credits.setTextColor(Color.parseColor("#FFFFFF"));

            Fragment fragment1 = new MyGames("my games");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recycler_view, fragment1, fragment1.getClass().getSimpleName()).commit();
        }*/

    }

    public void onResume() {
        super.onResume();
        try {
            BudgetCount();
            if (cc.loadPrefString("active").equals("post") || cc.loadPrefString("active").equals("by credit")) {
                btn_games.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_games.setTextColor(Color.parseColor("#DA3C3A"));
                btn_my_games.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_my_games.setTextColor(Color.parseColor("#FFFFFF"));
                btn_credits.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_credits.setTextColor(Color.parseColor("#FFFFFF"));
                btn_post.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_post.setTextColor(Color.parseColor("#FFFFFF"));
                btn_more.setTextColor(Color.parseColor("#FFFFFF"));
                btn_more.setBackgroundColor(Color.parseColor("#DA3C3A"));
                cc.savePrefString("active", "");
                Fragment fragment = new Games("games");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recycler_view, fragment, fragment.getClass().getSimpleName()).commit();

            }
           /* if (cc.loadPrefString("Team").equals("bothPlayer") && cc.loadPrefString("active").equals("post")) {
                btn_games.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_games.setTextColor(Color.parseColor("#DA3C3A"));
                btn_my_games.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_my_games.setTextColor(Color.parseColor("#FFFFFF"));
                btn_credits.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_credits.setTextColor(Color.parseColor("#FFFFFF"));
                btn_post.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_post.setTextColor(Color.parseColor("#FFFFFF"));
                btn_more.setTextColor(Color.parseColor("#FFFFFF"));
                btn_more.setBackgroundColor(Color.parseColor("#DA3C3A"));
                cc.savePrefString("Team", "");
                cc.savePrefString("active", "");
                Fragment fragment = new Games("games");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recycler_view, fragment, fragment.getClass().getSimpleName()).commit();
            } else if (cc.loadPrefString("Team").equals("onlyManager") && cc.loadPrefString("active").equals("post")) {
                  btn_games.setVisibility(View.GONE);
                btn_games.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_games.setTextColor(Color.parseColor("#FFFFFF"));
                btn_my_games.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_my_games.setTextColor(Color.parseColor("#DA3C3A"));
                btn_post.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_post.setTextColor(Color.parseColor("#FFFFFF"));
                btn_more.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_more.setTextColor(Color.parseColor("#FFFFFF"));
                btn_credits.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_credits.setTextColor(Color.parseColor("#FFFFFF"));
                cc.savePrefString("Team", "");
                cc.savePrefString("active", "");
                Fragment fragment1 = new MyGames("my games");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recycler_view, fragment1, fragment1.getClass().getSimpleName()).commit();

            }*/
            // Log.e("onResume","MainActivity");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        //Fragment fragment = null;
        switch (v.getId()) {
            case R.id.btn_games:
                tvCount.setBackgroundResource(R.mipmap.ic_noti_white);
                tvCount.setTextColor(Color.parseColor("#DA3C3A"));
                btn_games.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_games.setTextColor(Color.parseColor("#DA3C3A"));
                btn_my_games.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_my_games.setTextColor(Color.parseColor("#FFFFFF"));
                btn_credits.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_credits.setTextColor(Color.parseColor("#FFFFFF"));
                btn_post.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_post.setTextColor(Color.parseColor("#FFFFFF"));
                btn_more.setTextColor(Color.parseColor("#FFFFFF"));
                btn_more.setBackgroundColor(Color.parseColor("#DA3C3A"));
                cc.savePrefString("active", "game");
                Fragment fragment = new Games("games");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recycler_view, fragment, fragment.getClass().getSimpleName()).commit();

                break;
            case R.id.btn_my_games:
                tvCount.setBackgroundResource(R.mipmap.ic_noti);
                tvCount.setTextColor(Color.parseColor("#FFFFFF"));
                btn_games.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_games.setTextColor(Color.parseColor("#FFFFFF"));
                btn_my_games.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_my_games.setTextColor(Color.parseColor("#DA3C3A"));

                btn_post.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_post.setTextColor(Color.parseColor("#FFFFFF"));
                btn_more.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_more.setTextColor(Color.parseColor("#FFFFFF"));
                btn_credits.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_credits.setTextColor(Color.parseColor("#FFFFFF"));

                cc.savePrefString("active", "my game");
                Fragment fragment1 = new MyGames("my games");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recycler_view, fragment1, fragment1.getClass().getSimpleName()).commit();


                break;
            case R.id.btn_post:
                tvCount.setBackgroundResource(R.mipmap.ic_noti_white);
                tvCount.setTextColor(Color.parseColor("#DA3C3A"));
                btn_games.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_games.setTextColor(Color.parseColor("#FFFFFF"));
                btn_my_games.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_my_games.setTextColor(Color.parseColor("#FFFFFF"));
                btn_post.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_post.setTextColor(Color.parseColor("#DA3C3A"));

                btn_more.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_more.setTextColor(Color.parseColor("#FFFFFF"));
                btn_credits.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_credits.setTextColor(Color.parseColor("#FFFFFF"));
                cc.savePrefString("active", "post");
                /*cc.savePrefString("active", "post");
                if (Integer.parseInt(cc.loadPrefString("user_type_count")) > 1 && cc.loadPrefString("isManager").equals("Team_Manager")) {
                    cc.savePrefString("Team", "bothPlayer");
                } else if (Integer.parseInt(cc.loadPrefString("user_type_count")) == 1 && cc.loadPrefString("isManager").equals("Team_Manager")) {
                    cc.savePrefString("Team", "onlyManager");
                }*/
                startActivity(new Intent(MainActivity.this, PostGames.class));
                break;
            case R.id.btn_credits:
                tvCount.setBackgroundResource(R.mipmap.ic_noti_white);
                tvCount.setTextColor(Color.parseColor("#DA3C3A"));
                btn_games.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_games.setTextColor(Color.parseColor("#FFFFFF"));
                btn_my_games.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_my_games.setTextColor(Color.parseColor("#FFFFFF"));
                btn_post.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_post.setTextColor(Color.parseColor("#FFFFFF"));
                btn_more.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_more.setTextColor(Color.parseColor("#FFFFFF"));
                btn_credits.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_credits.setTextColor(Color.parseColor("#DA3C3A"));
               /* Fragment fragment3 = new MyCredit();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recycler_view, fragment3, fragment3.getClass().getSimpleName()).commit();
*/
                startActivity(new Intent(MainActivity.this, MyCredit.class));
                cc.savePrefString("active", "by credit");
                break;
            case R.id.btn_more:
                tvCount.setBackgroundResource(R.mipmap.ic_noti_white);
                tvCount.setTextColor(Color.parseColor("#DA3C3A"));
                btn_games.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_games.setTextColor(Color.parseColor("#FFFFFF"));
                btn_my_games.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_my_games.setTextColor(Color.parseColor("#FFFFFF"));
                btn_credits.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_credits.setTextColor(Color.parseColor("#FFFFFF"));
                btn_post.setBackgroundColor(Color.parseColor("#DA3C3A"));
                btn_post.setTextColor(Color.parseColor("#FFFFFF"));
                btn_more.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_more.setTextColor(Color.parseColor("#DA3C3A"));

                cc.savePrefString("active", "more");
                Fragment fragment5 = new MyGames("more");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recycler_view, fragment5, fragment5.getClass().getSimpleName()).commit();
                break;


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
                logout();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Please Wait");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_logout,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:login", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                                cc.logoutapp();
                                startActivity(new Intent(MainActivity.this, Login.class));
                                finish();
                            } else {
                                pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showToast(error.toString() + "");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                Log.e("request logout", params.toString());
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

    private void BudgetCount() {
        AppController.getInstance().getRequestQueue().getCache().clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_buzzer,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:Url_buzzer", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                JSONObject Buzzer = jsonObject.getJSONObject("Buzzer");

                                tvCount.setText((Integer.parseInt(Buzzer.getString("manager_available_count")) + Integer.parseInt(Buzzer.getString("survey_count")) + Integer.parseInt(Buzzer.getString("player_available_count")) + ""));

                                if (tvCount.getText().toString().trim().equals("0") || tvCount.getText().toString().trim().equals("")) {
                                    tvCount.setVisibility(View.GONE);
                                } else {
                                    tvCount.setVisibility(View.VISIBLE);
                                }
                                Log.e("budget", tvCount.getText().toString().trim());
                            } else {
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // cc.showToast(error.toString() + "");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));

                Log.e("request buzzer", params.toString());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                // Log.i("request header", headers.toString());
                return headers;
            }


        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }

}
