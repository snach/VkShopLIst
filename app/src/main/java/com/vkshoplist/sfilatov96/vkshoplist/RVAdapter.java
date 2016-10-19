package com.vkshoplist.sfilatov96.vkshoplist;

/**
 * Created by sfilatov96 on 18.10.16.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {
    Bitmap bitmap;
    Context context;
    public final String ONLINE="online";
    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name;
        TextView is_online;
        ImageView personPhoto;


        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            name = (TextView)itemView.findViewById(R.id.person_name);
            is_online = (TextView)itemView.findViewById(R.id.online_status);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }

    List<Person> persons;

    RVAdapter(Context context, List<Person> persons){
        this.persons = persons;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.name.setText(String.format("%s %s", persons.get(i).first_name,
                persons.get(i).last_name));
        personViewHolder.is_online.setText(persons.get(i).is_online);
        if(persons.get(i).is_online == ONLINE){
            personViewHolder.is_online.setTextColor(Color.GREEN);
        } else {
            personViewHolder.is_online.setTextColor(Color.RED);
        }
        //Log.d("context",getC)
        Picasso.with(context).load(persons.get(i).avater).transform(new CircularTransformation(100)).placeholder(R.drawable.user_placeholder).into(personViewHolder.personPhoto);
        //personViewHolder.personPhoto.setImageBitmap();
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    public  Bitmap getBitmapFromURL(String src) {
        new DownloadImageTask().execute(src);
        return bitmap;
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        Bitmap image = null;
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            bitmap = image;
        }
    }
}
