package com.example.forfoodiesbyfoodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    //create imposters
    Button Reg;
    EditText un, fn, email, pwd;

    //Create database reference and auth database reference
    DatabaseReference userRef;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //link imposters to their XML counterparts
        Reg = findViewById(R.id.btnRegReg);
        un = findViewById(R.id.etRegUserName);
        fn = findViewById(R.id.etRegFullName);
        email = findViewById(R.id.etRegEmail);
        pwd = findViewById(R.id.etRegPassword);

        //Instantiate the Auth database
        auth = FirebaseAuth.getInstance();

        //Instantiate the database
        userRef = FirebaseDatabase.getInstance().getReference("User");

        //crest listeners for buttons
        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add the string values of every editText to a separate string variable
                String uname = un.getText().toString();
                String fname = fn.getText().toString();
                String uemail = email.getText().toString();
                String password = pwd.getText().toString();

                //if condition to verify that text is written in the editTexts
                if (uname.equals("") || fname.equals("") || uemail.equals("") ||
                        password.equals(""))
                {
                    //create a Toast to inform the user of any actions needed
                    Toast.makeText(Register.this, "All fields must be completed",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    //function that takes email and password and creates entry in the Auth database
                    auth.createUserWithEmailAndPassword(email.getText().toString(),pwd.getText().toString()).
                            //add listener to check if the operation is completed successfully
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //function that signs in the new user to check for success
                                    auth.signInWithEmailAndPassword(email.getText().toString(), pwd.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            //creates a new user object u by calling the User class
                                            User u = new User(uname,fname,uemail);

                                            // add user to database with username as the
                                            // primary key (user will not be able to edit username)
                                            userRef.child(uname).setValue(u);

                                            //after verifying the success with the signin method
                                            // above, the user is singed out
                                            auth.signOut();

                                            Toast.makeText(Register .this,"Registration complete",
                                                    Toast.LENGTH_LONG).show();

                                            Intent i = new Intent(Register.this, MainActivity.class);
                                            startActivity(i);
                                        }
                                    });
                                }
                            //function that runs when the registration is unsuccessful return a toasts
                            //in case of error
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //this method allows the creation of Toasts that show the reasons for failure and
                            //this way, helps with debugging
                            Toast.makeText(Register.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}