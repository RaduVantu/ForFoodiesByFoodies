package com.example.forfoodiesbyfoodies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    //Declare the db references, auth db and listener, and the imposters
    private FirebaseAuth lAuth;
    private FirebaseAuth.AuthStateListener lAuthMistener;
    private EditText loginEmail,loginPassword;
    private Button loginLogin, loginRegister;
    private String TAG = "MainActivity";

    //we create an arraylist that will store variable of the class user
    private ArrayList<User> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Link imposters to xml objects
        loginEmail = findViewById(R.id.etLoginEmail);
        loginPassword = findViewById(R.id.etLoginPassword);
        loginLogin = findViewById(R.id.btnLoginLogin);
        loginRegister = findViewById(R.id.btnLoginRegister);

        //Instantiate authentication database
        lAuth = FirebaseAuth.getInstance();

        //Instantiate a listener for the auth database
        lAuthMistener = new FirebaseAuth.AuthStateListener()
        {
            //asks the auth database for changes to current logged user
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //if condition to check if user is already logged in
                if(user != null)
                {
                    Log.d(TAG, "You are logged in");

                }else
                {
                    Log.d(TAG, "No user logged in");
                }
            }

        };

        //create listeners for buttons
        loginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uemail = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                //if condition to verify that text is written in the editTexts for email and pass
                if(uemail.equals("") || password.equals(""))
                {
                    Toast.makeText(MainActivity.this, "All fields must be completed",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    //function for user authentication with listener for completion
                    lAuth.signInWithEmailAndPassword(uemail,password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {

                                    //condition for successful login
                                    if(task.isSuccessful())
                                    {
                                        //this intent navigates the user to the RestOrStall activity and
                                        Intent i = new Intent(MainActivity.this, RestOrStall.class);
                                        //putExtra attaches the String containing the user email, for use in the next activity
                                        i.putExtra("USER", uemail);
                                        startActivity(i);
                                    }
                                    else
                                    {
                                        Toast.makeText(MainActivity.this, "Login Failed",
                                                Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                }
            }
        });
        loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r = new Intent(MainActivity.this, Register.class);
                startActivity(r);
            }
        });

    }

    //when app starts, instantiates the authentication listener and will store active user after loggin
    @Override
    protected void onStart() {
        super.onStart();
        lAuth.addAuthStateListener(lAuthMistener);
    }

    //when user the leaves the app, closes the authentication listener and removes active user
    @Override
    protected void onStop() {
        super.onStop();
        if(lAuthMistener != null)
        {
            lAuth.removeAuthStateListener(lAuthMistener);

        }

    }
}
