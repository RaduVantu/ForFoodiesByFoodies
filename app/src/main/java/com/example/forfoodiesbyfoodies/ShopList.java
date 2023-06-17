package com.example.forfoodiesbyfoodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopList extends AppCompatActivity implements ShopListAdapter.ShopHolder.ShopInterface {

    //create recyclerview, list and button imposters
    RecyclerView rvShopList;
    RecyclerView.LayoutManager man;
    ShopListAdapter adapt;
    User user;
    Query shopref;
    String RorS;
    ArrayList<Shop> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        //link imposters to their XML counterparts
        rvShopList = findViewById(R.id.rvSList);

        Intent select = getIntent();
        //the string RorS received through the intent, carries the type of shop the the list will populate
        RorS = select.getStringExtra("TYPE");

        user = select.getParcelableExtra("USER");

        //initialise the query with the RorS condition
        shopref = FirebaseDatabase.getInstance().getReference("Shops")
                .orderByChild("type").equalTo(RorS);

        //the layout manager can display the cards in the recycle viewer vertically or horizontally,
        //depending on our choice. the default is vertical
        man = new LinearLayoutManager(ShopList.this);
        //apply the manager to the recycle view
        rvShopList.setLayoutManager(man);

        shopref.addListenerForSingleValueEvent(fetcher);
    }


    ValueEventListener fetcher = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Shop shop = dataSnapshot.getValue(Shop.class);
                    list.add(shop);
                }
                //this adapter creates the cards for the recycler, based on the arrayList
                adapt = new ShopListAdapter(list, ShopList.this);
                //the adapter populates the recycler with the created cards
                rvShopList.setAdapter(adapt);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    public void onShopClick(int n) {
            Intent r = new Intent(ShopList.this, ShopDetail.class);
            r.putExtra("Shops", list.get(n)).putExtra("USER", user);
            startActivity(r);
    }
}