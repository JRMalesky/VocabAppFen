package com.firebaseFen.fenapp.vocabapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Pino on 8/23/2017.
 */

public class ForgotPassword extends AppCompatActivity {


    private EditText forgot_email;
    private Button btnreset;
    private TextView btnback;
    private RelativeLayout activity_forgot;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);

        forgot_email = (EditText) findViewById(R.id.forgot_email);
        btnreset=(Button)findViewById(R.id.reset_password);
        btnback = (TextView) findViewById(R.id.forgot_back);
        activity_forgot = (RelativeLayout) findViewById(R.id.activity_forgot_password);
        auth = FirebaseAuth.getInstance();

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassword.this, LogIn.class));
                finish();
            }
        });
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword(forgot_email.getText().toString());
            }
        });
    }

    private void resetPassword(final String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPassword.this, "We have sent password to email"+email, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ForgotPassword.this, "Failed to send password", Toast.LENGTH_LONG).show();
                        }
                    }


                });


    }
}
