package com.example.forfoodiesbyfoodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestOrStall extends AppCompatActivity {

    //Create imposters
    Button choiceRest, choiceStall;
    Button rosMyprofile, rosNewstall;
    String activemail;

    //create an arraylist that will take User type objects as values
    ArrayList<User> userlist = new ArrayList<>();

    //Create query reference
    Query finduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_or_stall);

        //this intent receives the USER message from Main activity
        Intent u = getIntent();
        //The string activemail will take the value of the string sent from MainActivity
        activemail = u.getStringExtra("USER");

        //link imposters to their XML counterparts
        rosMyprofile = findViewById(R.id.btnRosMyProfile);
        choiceRest = findViewById(R.id.btnRosRest);
        choiceStall = findViewById(R.id.btnRosStall);
        rosNewstall = findViewById(R.id.btnRosNewstall);

        //Instantiate the query on the realtime database at node "User", choosing only entries containing
        // the child "email" equal to the value of the activeuser variable
        finduser = FirebaseDatabase.getInstance().getReference("User")
                .orderByChild("email").equalTo(activemail);

        //event listener for the query
        finduser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    //Create a User type object
                    User user = snapshot1.getValue(User.class);
                    //any User type objects found by the query will be added to the arraylist
                    userlist.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //create listeners for buttons
        rosMyprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prof = new Intent(RestOrStall.this, UserProfile.class);
                //send the values of index 0 of the arraylist to the next activity
                prof.putExtra("USER", userlist.get(0));
                startActivity(prof);
            }
        });

        choiceRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent selectrest = new Intent(RestOrStall.this, ShopList.class);
                // "r" for restaurant
                selectrest.putExtra("TYPE","r").putExtra("USER", userlist.get(0));
                startActivity(selectrest);
            }
        });

        choiceStall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent selectstall = new Intent(RestOrStall.this, ShopList.class);
                // "s" for stall
                selectstall.putExtra("TYPE","s").putExtra("USER", userlist.get(0));
                startActivity(selectstall);
            }
        });

        rosNewstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent selectnewstall = new Intent(RestOrStall.this, AddNewStall.class);
                selectnewstall.putExtra("USER", activemail);
                startActivity(selectnewstall);
            }
        });
    }
}