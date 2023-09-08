package com.cyberclick.cbcakanisa.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cyberclick.cbcakanisa.Detailed;
import com.cyberclick.cbcakanisa.R;
import com.cyberclick.cbcakanisa.models.Articles;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private List<Articles> dataList;
    private Context context;

    public CustomAdapter(Context context,List<Articles> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView txtTitle, txtDate;
        private ImageView coverImage;
        private CardView cardView;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtTitle = mView.findViewById(R.id.articleTitle);
            coverImage = mView.findViewById(R.id.articleImage);
            cardView = mView.findViewById(R.id.cardView);
            txtDate = mView.findViewById(R.id.tvDate);

        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.items, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        // Split images
        String str = dataList.get(position).getMedias();
        String[] arrOfStr = str.split(",", 2);
        String articlesName;
        int index = 30;

        if (index < dataList.get(position).getName().length()) {
            articlesName = dataList.get(position).getName().substring(0, index);
            // Continue with the rest of your code
        } else {
            // Handle the case when the index is out of bounds
            articlesName = dataList.get(position).getName().substring(0, 15);
        }

        holder.txtTitle.setText(articlesName+"...");

        holder.txtDate.setText(dateTime(dataList.get(position).getCreated_at()));

        try{
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(new OkHttp3Downloader(context));
            builder.build().load("https://www.cbca-kanisa.org/"+arrOfStr[0])
                    .placeholder((R.drawable.logo))
                    .error(R.drawable.logo)
                    .into(holder.coverImage);
        } catch (RuntimeException e) {
            Log.e("Error loading images", e.getMessage());
        }

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Detailed.class);
            intent.putExtra("title",dataList.get(position).getName());
            intent.putExtra("source","cbca-kanisa.org");
            intent.putExtra("time",dateTime(dataList.get(position).getCreated_at()));
            intent.putExtra("desc",dataList.get(position).getDescription());
            intent.putExtra("imageUrl","https://www.cbca-kanisa.org/"+arrOfStr[0]);
            intent.putExtra("url","https://www.cbca-kanisa.org/actu/"+dataList.get(position).getId());
            context.startActivity(intent);
        });

    }

    public String dateTime(String t){
        PrettyTime prettyTime = new PrettyTime(new Locale(getCountry()));
        String time = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:", Locale.ENGLISH);
            Date date = simpleDateFormat.parse(t);
            time = prettyTime.format(date);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return time;

    }

    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
