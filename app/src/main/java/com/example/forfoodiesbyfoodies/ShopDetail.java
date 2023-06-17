package com.example.forfoodiesbyfoodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopDetail extends AppCompatActivity {
    //create imposters
    ImageView shopDetailImg;
    RatingBar shopRating;
    TextView shopDetailName, shopDetailAddress, shopDetailDesc;
    Button shopDetailViewRev, shopDetailReserve, shopDetailAddRev;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        //this intent brings the name of the shop and the user object from the ShopList class
        Intent i = getIntent();
        Shop shop = i.getParcelableExtra("Shops");
        user = i.getParcelableExtra("USER");

        String shopName = shop.getName();

        //link imposters to their XML counterparts
        shopDetailImg = findViewById(R.id.ivShopDetail);
        shopRating = findViewById(R.id.rbShopDetail);
        shopDetailName = findViewById(R.id.tvShopDetailName);
        shopDetailAddress = findViewById(R.id.tvShoptDetailAddress);
        shopDetailDesc = findViewById(R.id.tvShopDetailDesc);
        shopDetailViewRev = findViewById(R.id.btnShopDetailViewRev);
        shopDetailReserve = findViewById(R.id.btnShopDetailReserve);
        shopDetailAddRev = findViewById(R.id.btnShopDetailAddRev);

        //this condition sets the visibility of the Reserve button for restaurants only
        if(shop.getType().equals("s")) {
            shopDetailReserve.setVisibility(View.INVISIBLE);
        }
        else {
            shopDetailReserve.setVisibility(View.VISIBLE);
        }

        //get the values from the Shop object received through the intent and populate the textViews
        shopDetailName.setText(shop.getName());
        shopDetailDesc.setText(shop.getDesc());
        shopDetailAddress.setText(shop.getAddress());
        //the Picasso plugin reads the image from the database and places it in the imageView
        Picasso.get().load(shop.getImgUrl()).fit().into(shopDetailImg);

        //create listeners for buttons
        shopDetailViewRev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vr = new Intent(ShopDetail.this, ViewShopReview.class);
                vr.putExtra("Shop",shopName);
                startActivity(vr);
            }
        });
        shopDetailReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the web address from the Shop object received through the intent
                String Web = shop.getWeb();

                //this intent opens a chrome tab with the parsed url
                Intent nav = new Intent(Intent.ACTION_VIEW);
                nav.setData(Uri.parse(Web));
                startActivity(nav);
            }
        });
        shopDetailAddRev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ar = new Intent(ShopDetail.this, AddShopReview.class);
                ar.putExtra("Name",shopName).putExtra("USER", user);
                startActivity(ar);
            }
        });
    }
}