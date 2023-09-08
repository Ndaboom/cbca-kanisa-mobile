package com.cyberclick.cbcakanisa;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cyberclick.cbcakanisa.adapter.CustomAdapter;
import com.cyberclick.cbcakanisa.models.Articles;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeActivity extends AppCompatActivity {
    public ImageSlider imageSlider;

    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    //private Progress

    String url = "https://www.cbca-kanisa.org/api/articles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        createNotificationChannel();
        imageSlider = findViewById(R.id.image_slider);

        ArrayList<SlideModel> images = new ArrayList<>();
        images.add(new SlideModel(R.drawable.slide1,"Developpement et promotion humaine", null)); // https://www.cbca-kanisa.org/department/3
        //images.add(new SlideModel(R.drawable.slide2,"Slide 2", null));
        images.add(new SlideModel(R.drawable.slide3,"Evangelisation qualitative et quantitative", null)); // https://www.cbca-kanisa.org/department/2
        images.add(new SlideModel(R.drawable.slide4,"Formation integrale pour tous", null)); // https://www.cbca-kanisa.org/department/5
        images.add(new SlideModel(R.drawable.slide5,"Mobilisation et Gestion des ressources", null)); // https://www.cbca-kanisa.org/department/9
        images.add(new SlideModel(R.drawable.slide6,"Protection de l'environement", null)); // https://www.cbca-kanisa.org/service/2
        images.add(new SlideModel(R.drawable.slide7,"Promotion du genre et des droits humains", null)); // https://www.cbca-kanisa.org/service/2

        imageSlider.setImageList(images, ScaleTypes.CENTER_CROP);
        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void doubleClick(int i) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                if(i == 0){
                intent.putExtra("page_url","https://www.cbca-kanisa.org/department/3");
                }else if(i == 1){
                intent.putExtra("page_url","https://www.cbca-kanisa.org/department/2");
                }else if(i == 2){
                intent.putExtra("page_url","https://www.cbca-kanisa.org/department/5");
                }else if(i == 3){
                intent.putExtra("page_url","https://www.cbca-kanisa.org/department/9");
                }else if(i == 4){
                intent.putExtra("page_url","https://www.cbca-kanisa.org/service/2");
                }else if(i == 5){
                intent.putExtra("page_url","https://www.cbca-kanisa.org/service/2");
                }

                HomeActivity.this.startActivity(intent);
            }

            @Override
            public void onItemSelected(int i) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                if(i == 0){
                    intent.putExtra("page_url","https://www.cbca-kanisa.org/department/3");
                }else if(i == 1){
                    intent.putExtra("page_url","https://www.cbca-kanisa.org/department/2");
                }else if(i == 2){
                    intent.putExtra("page_url","https://www.cbca-kanisa.org/department/5");
                }else if(i == 3){
                    intent.putExtra("page_url","https://www.cbca-kanisa.org/department/9");
                }else if(i == 4){
                    intent.putExtra("page_url","https://www.cbca-kanisa.org/service/2");
                }else if(i == 5){
                    intent.putExtra("page_url","https://www.cbca-kanisa.org/service/2");
                }

                HomeActivity.this.startActivity(intent);
            }
        });

        /* Fetch articles */

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                Constants.FETCH_ARTICLES,
                response -> {
                    //pDialog.setContentText(response+" User id "+DataStore.getInstance(HomeActivity.this).getCurrentUsername()+"/"+date1_recup+"/"+date2_recup);
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.has("data") && !obj.isNull("data")) {
                            JSONArray articlesArr = obj.getJSONArray("data");
                            // Convert JSONArray to list object
                            Type listType = new TypeToken<List<Articles>>() {}.getType();
                            List<Articles> articlesList = new Gson().fromJson(articlesArr.toString(), listType);
                            generateDataList(articlesList);
                        } else {
                            Log.w("Message", "Aucune actualite ...");
                        }
                    } catch (JSONException e) {
                        Log.e("Error ", e.getMessage());
                        new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Petite erreur!")
                                .setContentText("Impossible de charger les recentes articles!\n Merci de verifier votre internet")
                                .show();
                    }
                },
                error -> {
                    new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Petite erreur!")
                            .setContentText("Impossible de charger les recentes articles!\n Merci de verifier votre internet")
                            .show();
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                return headers;
            }
        };

        RequestHandler.getInstance(HomeActivity.this).addToRequestQueue(stringRequest);
    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<Articles> articlesList) {
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new CustomAdapter(this,articlesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CBCA Kanisa";
            String description = "Recherche des nouvelles actualites";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("updates_channel",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        switch (item.getItemId()){
            case R.id.history_menu:
                intent.putExtra("page_url","https://www.cbca-kanisa.org/history");
                HomeActivity.this.startActivity(intent);
                break;
            case R.id.chriffres_menu:
                intent.putExtra("page_url","https://www.cbca-kanisa.org/statistics");
                HomeActivity.this.startActivity(intent);
                break;
            case R.id.departements_menu:
                intent.putExtra("page_url","https://www.cbca-kanisa.org/departements");
                HomeActivity.this.startActivity(intent);
                break;
            case R.id.vision_menu:
                intent.putExtra("page_url","https://www.cbca-kanisa.org/vision-et-mission");
                HomeActivity.this.startActivity(intent);
                break;
            case R.id.mandataires_menu:
                intent.putExtra("page_url","https://www.cbca-kanisa.org/dirigeants");
                HomeActivity.this.startActivity(intent);
                break;
        }
        return true;
    }
    
}