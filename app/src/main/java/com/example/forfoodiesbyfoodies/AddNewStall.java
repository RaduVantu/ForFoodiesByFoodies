package com.example.forfoodiesbyfoodies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddNewStall extends AppCompatActivity {

    //create imposters
    ImageView newStallPic;
    EditText newStallName, newStallAddress, newStallDesc;
    Button newStallAdd;
    String activemail;
    Uri path;

    //REQ is an order number used by onActivityResult to signal successful completion for the img upload
    public static final int REQ = 1;

    //type = s sets the type of the shop to stall
    String Type = "s";

    //Create database and query references
    StorageReference stallstoref;
    DatabaseReference stalldbref;
    Query stallq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_stall);

        Intent u = new Intent();
        activemail = u.getStringExtra("USER");

        //link imposters to their XML counterparts
        newStallPic = findViewById(R.id.ivAddStallPic);
        newStallName = findViewById(R.id.etAddStallName);
        newStallAddress = findViewById(R.id.etAddStallAddress);
        newStallDesc = findViewById(R.id.etAddStallDesc);
        newStallAdd =  findViewById(R.id.btnAddStall);

        //Initialise the databases
        stallstoref = FirebaseStorage.getInstance().getReference("Shops");
        stalldbref = FirebaseDatabase.getInstance().getReference("Shops");

        //create listeners for buttons
        newStallPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //upimg will search the device storage for image type files
                Intent upimg = new Intent();
                upimg.setType("image/*");
                upimg.setAction(Intent.ACTION_GET_CONTENT);
                //here we use REQ as the placeholder for this function in the execution queue
                startActivityForResult(upimg, REQ);

            }
        });

        newStallAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if condition to verify that text is written in the editTexts
                if(newStallAddress.equals("") || newStallName.equals("") || newStallDesc.equals("") ||
                        newStallPic.getDrawable() != null)
                {
                    //if at least one of the edit texts does not have any text in them, or if a
                    // photo is not selected, a message will inform the user
                    Toast.makeText(AddNewStall.this,
                            "All fields must be completed and photo added",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    //this string stores the new stall name
                    String shopName = newStallName.getText().toString();

                    //this query takes the shopName string and checks if the new stall name exist in the database
                    stallq = stalldbref.child("Shops").child(shopName);

                    //this function checks the instance of the database for the requested value, in our case the name of the new stall
                    ValueEventListener checkexisting = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){

                                //this function will show a message to the user, in case the shop exists
                                Toast.makeText(AddNewStall.this, "This stall already exists",
                                        Toast.LENGTH_LONG).show();
                            }
                            else {
                                //the selected image for the new stall is uploaded to the storage
                                StorageReference imgref = stallstoref.child(shopName).child(shopName+"."+ext(path));

                                //success listener to confirm successful upload of the image to storage
                                imgref.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //if upload succsessful return the image url
                                        imgref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri path) {
                                                //this string keeps the image uri
                                                String stallpicuri = path.toString();
                                                //this creates an object of the new stall using the constructors from the shop class
                                                Shop stall = new Shop(newStallName.getText().toString(),
                                                        newStallAddress.getText().toString(),
                                                        newStallDesc.getText().toString(), stallpicuri,
                                                        null, Type);

                                                //we send the newly created object to be stored in the database
                                                stalldbref.child(stall.getName()).setValue(stall);

                                                //this intent navigates to the RestOrStall activity if the add operation is successful
                                                Intent addDone = new Intent(AddNewStall.this,
                                                        RestOrStall.class);
                                                addDone.putExtra("USER", activemail);
                                                startActivity(addDone);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //if the upload is unsuccessful the image is deleted from storage and a message is shown to the user
                                                imgref.delete();
                                                Toast.makeText(AddNewStall.this,
                                                        "Upload Failed", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }

                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    //executes the event listener defined above
                    stallq.addListenerForSingleValueEvent(checkexisting);
                }
            }

        });
    }

    //this function returns if the image selection from the user device was successful
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ && resultCode == RESULT_OK && data.getData() !=null){
            path = data.getData();
            //the Picasso plugin handles the reading and placing of images in the app
            Picasso.get().load(path).fit().into(newStallPic);
        }
    }
    //this function returns the extension for user selected files
    private String ext (Uri uri){
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mimer = MimeTypeMap.getSingleton();
        return mimer.getExtensionFromMimeType(resolver.getType(uri));
    }

}
