package com.firebaseFen.fenapp.vocabapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Pino on 9/22/2017.
 */

public class LogIn extends AppCompatActivity {
    private static final String TAG ="LogIn";
    EditText input_email, input_password;
    TextView btnSignup, btnForgotPass;
    FirebaseAuth mauth;
    FirebaseAuth.AuthStateListener mauthListener;
    Button login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.log_in);
        btnSignup = (TextView) findViewById(R.id.enter_signup);
        btnForgotPass = (TextView) findViewById(R.id.enter_forgotpassword);
        input_email = (EditText) findViewById(R.id.enter_email);
        input_password = (EditText) findViewById(R.id.enter_password);

        login.setOnClickListener( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(LogIn.this,GameActivity.class));
            }
        } );
        mauth = FirebaseAuth.getInstance();
        mauthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(LogIn.this, GameActivity.class));
                    //Getdifficulty();
                    //Getlanguage();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }

        };

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this,ForgotPassword.class));
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, SignUp.class));
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                String Email = input_email.getText().toString();
                String Pass = input_password.getText().toString();
                if (!Email.equals("") && !Pass.equals("")) {
                    mauth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LogIn.this, "Log In successful", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(LogIn.this, "Log In failed.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else
                    Toast.makeText(LogIn.this, "Please write email or password :)", Toast.LENGTH_LONG).show();
            }


        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mauth.addAuthStateListener(mauthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mauthListener!=null)
            mauth.removeAuthStateListener(mauthListener);
    }


}
