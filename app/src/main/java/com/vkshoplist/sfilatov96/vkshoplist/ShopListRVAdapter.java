package com.vkshoplist.sfilatov96.vkshoplist;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by sfilatov96 on 29.10.16.
 */
public class ShopListRVAdapter extends RecyclerView.Adapter<ShopListRVAdapter.PersonViewHolder>{
    Context context;
    public final String ONLINE="online";
    ArrayList<ShopListItem> mDataSet;
    ArrayList<ShopListItem> shopListItems;
    private final static String TAG = ShopListRVAdapter.class.getSimpleName();

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name;
        TextView quantity;
        TextView value;


        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            name = (TextView)itemView.findViewById(R.id.name);
            quantity = (TextView)itemView.findViewById(R.id.quantity);
            value = (TextView)itemView.findViewById(R.id.value);
        }
    }

    ShopListRVAdapter(Context context, ArrayList<ShopListItem> shopListItems){
        this.shopListItems = shopListItems;
        mDataSet = this.shopListItems;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_list_item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.name.setText(shopListItems.get(i).name);
        personViewHolder.quantity.setText(shopListItems.get(i).quantity);
        personViewHolder.value.setText(shopListItems.get(i).value);

    }

    @Override
    public int getItemCount() {
        if (shopListItems != null) {
            return shopListItems.size();
        } else return 0;
    }



}
