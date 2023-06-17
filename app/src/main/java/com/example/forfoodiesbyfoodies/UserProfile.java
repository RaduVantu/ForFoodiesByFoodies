package com.example.forfoodiesbyfoodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {

    //create imposters
    ImageView userImg;
    TextView userName, userFullName, userEmail;

    //Create storage database reference
    StorageReference picref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //this intent brings the "user" object from previous activity
        Intent u = getIntent();

        //this User type object stores the object from the intent
        User user = u.getParcelableExtra("USER");

        //these strings get the values from the "user" object
        String un = user.getUsername();
        String fn = user.getFullname();
        String em = user.getEmail();

        //link imposters to their XML counterparts
        userImg = findViewById(R.id.ivUser);
        userName = findViewById(R.id.tvUserProfileUserame);
        userFullName = findViewById(R.id.tvUserProfFullName);
        userEmail = findViewById(R.id.tvUserProfEmail);

        //get the values from the above defined strings in to the textViews
        userName.setText(un);
        userFullName.setText(fn);
        userEmail.setText(em);

        //Instantiate the storage database containing the profile picture
        picref = FirebaseStorage.getInstance().getReference("UserImg/profilePic.jpg");

        //the Picasso plugin reads the image from the database and places it in the imageView
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/for-foodies-by-foodies-14c29.appspot.com/o/UserImg%2FprofilePic.jpg?alt=media&token=5c17c98f-d8d6-4cb2-84fb-e77a76abf389")
                .fit().into(userImg);
    }
}