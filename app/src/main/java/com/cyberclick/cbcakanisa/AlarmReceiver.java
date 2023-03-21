package com.cyberclick.cbcakanisa;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import com.cyberclick.cbcakanisa.Constants;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // Request for the latest public post ID

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,Constants.ROOT_URL,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        int id = dataArray.getJSONObject(0).getInt("id");
                        String title = dataArray.getJSONObject(0).getString("name");

                        // Check if current post differ from incoming
                        if(id != DataStore.getInstance(context).getLastPostId()){
                            // Save ID in shared preferences
                            DataStore.getInstance(context).setLastPostId(id);
                            // Display Notification with post name
                            Intent i = new Intent(context,DestinationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,0);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"updates_channel")
                                    .setSmallIcon(R.drawable.ic_launcher_background)
                                    .setContentTitle("CBCA Kanisa")
                                    .setContentText(""+title)
                                    .setAutoCancel(true)
                                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setContentIntent(pendingIntent);

                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                            notificationManagerCompat.notify(123,builder.build());
                            // Display Notification with post name

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                context,
                                error.getMessage()+"Something went wrong...please try again",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }
}
