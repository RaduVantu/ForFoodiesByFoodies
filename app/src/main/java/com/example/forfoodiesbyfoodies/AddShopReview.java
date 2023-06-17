package com.example.forfoodiesbyfoodies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddShopReview extends AppCompatActivity {

    //create imposters
    EditText addRevRev;
    TextView addRevName;
    RatingBar addRevRate;
    Button addRevFinish;
    //we used a set value for because the Query for user in RestOrStall will not work
    String activeuser,activemail;

    //Create database reference
    DatabaseReference revref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop_review);

    //link imposters to their XML counterparts
     addRevRev = findViewById(R.id.etAddReview);
     addRevName = findViewById(R.id.tvAddRevName);
     addRevFinish = findViewById(R.id.btnAddRevFinish);
     addRevRate = findViewById(R.id.rbAddRev);

        //this intents brings the shop name from the ShopDetail activity
        Intent addrev = getIntent();

        //this string stores the shop name brought by the intent
        String shopName = addrev.getStringExtra("Name");

        User user = addrev.getParcelableExtra("USER");

        activeuser = user.getUsername();
        activemail = user.getEmail();

        //set the value of the above string in the addRevName text view
        addRevName.setText(shopName);

        //initialise database revref
        revref = FirebaseDatabase.getInstance().getReference("Reviews/" + shopName);

        addRevFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if condition to verify that text is written in the editText
                if(addRevRev.equals(""))
                {
                    Toast.makeText(AddShopReview.this,"Nothing to write?", Toast.LENGTH_LONG).show();
                }
                else
                {
                    //this float stores the user rating from the rating bar
                    float rating = addRevRate.getRating();
                    //we create a new object, based on the Review class
                    Review ar = new Review(activeuser, addRevRev.getText().toString(), rating);
                    //the Review type object is stored in the database
                    revref.child(activeuser).setValue(ar);

                    //the intent will navigate the user back to the RestOrStall activity
                    Intent i = new Intent(AddShopReview.this, RestOrStall.class);
                    i.putExtra("USER", activemail);
                    startActivity(i);
                }

            }
        });


    }
}