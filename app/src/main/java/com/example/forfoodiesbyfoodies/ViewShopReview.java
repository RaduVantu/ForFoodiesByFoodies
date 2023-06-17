package com.example.forfoodiesbyfoodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewShopReview extends AppCompatActivity {

    // create imposters, arraylist and query references
    Button viewPrevRev, viewNextRev;
    TextView shopName, userName, viewrev;
    RatingBar rbrev;
    int pos = 0;

    ArrayList<Review> list = new ArrayList<>();

    Query revref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shop_review);

        //link imposters to their XML counterparts
        viewrev = findViewById(R.id.tvViewRev);
        shopName = findViewById(R.id.tvViewRevName);
        userName = findViewById(R.id.tvViewRevUserName);
        rbrev = findViewById(R.id.rbViewRev);
        viewPrevRev = findViewById(R.id.btnViewPrevRev);
        viewNextRev = findViewById(R.id.btnViewNextRev);

        Intent vr = getIntent();

        String name = vr.getStringExtra("Shop");;

        shopName.setText(name);

        revref = FirebaseDatabase.getInstance().getReference("Reviews").orderByChild(name);
        revref.addListenerForSingleValueEvent(fetcher);

        //create the listeners for buttons
        viewPrevRev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pos-- decreases the index while reading the arraylist
                pos--;
                userName.setText(list.get(pos).getUser());
                Float rating = list.get(pos).getStars();;
             //   rbrev.setRating(rating);
                viewrev.setText(list.get(pos).getReview());
            }
        });
        viewNextRev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pos++ increases the index while reading the arraylist
                pos++;
                userName.setText(list.get(pos).getUser());
                Float rating = list.get(pos).getStars();
             //   rbrev.setRating(rating);
                viewrev.setText(list.get(pos).getReview());
            }
        });



    }
    ValueEventListener fetcher = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot possition : snapshot.getChildren()) {
                Review rev = possition.getValue(Review.class);
                list.add(rev);
            }
            userName.setText(list.get(pos).getUser());
         //   rbrev.setRating(list.get(pos).getStars());
            viewrev.setText(list.get(pos).getReview());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}