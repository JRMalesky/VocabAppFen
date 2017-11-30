package com.firebaseFen.fenapp.vocabapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.firebaseFen.fenapp.vocabapp.R.id.activity_signup;

/**
 * Created by Pino on 8/23/2017.
 */

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    Button btnregister;
    TextView btnlogin;
    EditText input_email, input_pass;
    RelativeLayout signup;
    private FirebaseAuth auth;
    Snackbar snackbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        //View
        btnregister = (Button) findViewById(R.id.tv_register);
        input_email = (EditText) findViewById(R.id.signup_email);
        input_pass = (EditText) findViewById(R.id.signup_password);
        btnlogin = (TextView) findViewById(R.id.alreadyregister);

        signup = (RelativeLayout) findViewById(activity_signup);

        btnregister.setOnClickListener(this);
        btnlogin.setOnClickListener(this);


        auth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_register) {
            signUpUser(input_email.getText().toString(),input_pass.getText().toString());
        }
        else if (view.getId() == R.id.alreadyregister) {
            startActivity(new Intent(SignUp.this, LogIn.class));
        }
    }


    public void signUpUser(String email, final String password) {
auth.createUserWithEmailAndPassword(email,password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful())
                {
                   if(password.length()<6){
                       Toast.makeText(SignUp.this, "Password length must be over 6", Toast.LENGTH_LONG).show();
                   }
                }
                else {
                    Toast.makeText(SignUp.this, "Register success!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}