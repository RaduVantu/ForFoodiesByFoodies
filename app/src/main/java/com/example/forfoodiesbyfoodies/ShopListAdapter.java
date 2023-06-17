package com.example.forfoodiesbyfoodies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopListAdapter extends RecyclerView.Adapter <ShopListAdapter.ShopHolder> {

    //create arrayLists and the interface
    ArrayList<Shop> list;
    ShopHolder.ShopInterface select;

    //constructor for the adapter
    public ShopListAdapter(ArrayList<Shop> list, ShopHolder.ShopInterface _select) {
        this.list = list;
        select = _select;
    }

    @NonNull
    @Override
    public ShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //create the view that will hold the shopcard
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopcard, parent, false);
        //the holder will contain the shopcard and the interface parameters
        ShopHolder holder = new ShopHolder(v, select);
        return holder;
    }

    //this method populates the shopcard with the image and name of the shop
    @Override
    public void onBindViewHolder(@NonNull ShopHolder holder, int position) {

        //this sets the shop name
        holder.tv.setText(list.get(position).getName());
        //this sets the shop image
        Picasso.get().load(list.get(position).getImgUrl()).fit().into(ShopHolder.iv);
    }

    //this returns the list size
    @Override
    public int getItemCount() {
        return list.size();
    }

    //this class defines the shopcard elements and implements the ability to click the card
    public static class ShopHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        static TextView tv;
        static ImageView iv;
        ShopInterface select;

        public ShopHolder(@NonNull View itemView, ShopInterface _select) {
            super(itemView);
            tv = itemView.findViewById(R.id.tvCardTemplate);
            iv = itemView.findViewById(R.id.ivCardTemplate);
            select = _select;
            itemView.setOnClickListener(this);
        }

        //create listeners for the card interface
        @Override
        public void onClick(View v) {
            select.onShopClick(getAdapterPosition());
        }

        public interface ShopInterface { void onShopClick(int n); }
    }
}
