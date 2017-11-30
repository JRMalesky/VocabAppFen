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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Student on 5/19/2017.
 */


public class GameActivity extends AppCompatActivity {
    private static final String TAG ="GameActivity";
    Button save;
    Button enter_game;
    DatabaseReference mRefUser;
    TextView b_logout;
    FirebaseAuth mauth;
    FirebaseAuth.AuthStateListener mauthListener;
    //EditText input_email, input_password;
    //TextView btnSignup, btnForgotPass;
    public static int mDifficulty = 3;
    public static int mLanguage = 0;
   // FirebaseAuth mauth;
   // FirebaseAuth.AuthStateListener mauthListener;
    String inputemailString, inputpassString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mRefUser = FirebaseDatabase.getInstance().getReference();
        mauth = FirebaseAuth.getInstance();
        mauthListener=new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(GameActivity.this, LogIn.class));
                }

            }

        };
        /*btnSignup = (TextView) findViewById(R.id.enter_signup);
        btnForgotPass = (TextView) findViewById(R.id.enter_forgotpassword);
        input_email = (EditText) findViewById(R.id.enter_email);
        input_password = (EditText) findViewById(R.id.enter_password);*/

        enter_game = (Button) findViewById(R.id.enter_game);
        enter_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameActivity.this,MainActivity.class));
                Getdifficulty();
                Getlanguage();
            }
        });
        b_logout=(TextView) findViewById ( R.id.logout ) ;


        b_logout.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                mauth.signOut ();
            }
        } );
        /*mauth = FirebaseAuth.getInstance();
        mauthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(GameActivity.this, MainActivity.class));
                    Getdifficulty();
                    Getlanguage();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }

        };

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(GameActivity.this,ForgotPassword.class));
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameActivity.this, SignUp.class));
            }
        });*/


        /*enter_game.setFocusable(true);
        enter_game.setFocusableInTouchMode(true);
        enter_game.requestFocus();*/




       /* enter_game.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                String Email = input_email.getText().toString();
                String Pass = input_password.getText().toString();
                if (!Email.equals("") && !Pass.equals("")) {
                    mauth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(GameActivity.this, "Log In successful", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(GameActivity.this, "Log In failed.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else
                    Toast.makeText(GameActivity.this, "Please write email or password...", Toast.LENGTH_LONG).show();
            }


        });*/
    }
    /*@Override
    protected void onStart() {
        super.onStart();
        mauth.addAuthStateListener(mauthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mauthListener!=null)
            mauth.removeAuthStateListener(mauthListener);
    }*/
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

    public void activity()
    {
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        Getdifficulty();
        Getlanguage();
        startActivity(intent);


    }

    private final int Getdifficulty()
    {
        RadioButton easy, med, hard, streak, random;
        easy = (RadioButton) findViewById(R.id.radeasy);
        med = (RadioButton) findViewById(R.id.radmed);
        hard = (RadioButton) findViewById(R.id.radhard);
        streak = (RadioButton) findViewById(R.id.radstreak);
        random = (RadioButton) findViewById(R.id.radrand);
        if(easy.isChecked())
        {
            mDifficulty = 0;
            return 0;
        }
        else if(med.isChecked())
        {
            mDifficulty = 1;
            return 1;
        }
        else if(hard.isChecked())
        {
            mDifficulty = 2;
            return 2;
        }
        else if(streak.isChecked())
        {
            mDifficulty = 3;
            return 3;
        }
        else if(random.isChecked())
        {
            mDifficulty = 4;
            return 4;
        }
        else
            return 4;
    }


    private final int Getlanguage()
    {
        RadioButton chinese,english;
        chinese=(RadioButton)findViewById(R.id.radChinese);
        english=(RadioButton)findViewById(R.id.radEnglish);
        if(chinese.isChecked())
        {
            mLanguage = 1;
            return 1;
        }
        else
        {
            mLanguage = 0;
            return  0;
        }

    }




}

        /*mauthListener=new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    finish();
                    startActivity(new Intent(activity_gamectivity.this,MainActivity.class));

                }else{
                    //startActivity(new Intent(activity_gamectivity.this,MainActivity.class));




                }
            }
        };*/


        /*enter_game.setOnClickListener(new View.OnClickListener ()
        {
            @Override
            public void onClick(View v) {
               inputemailString=input_email.getText().toString().trim();
                inputemailString=input_password.getText().toString().trim();
                if(!TextUtils.isEmpty(inputemailString)&&!TextUtils.isEmpty(inputpassString)){
                    mauth.signInWithEmailAndPassword(inputemailString,inputpassString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()){
                             activity();
                         }else{
                             Toast.makeText(activity_gamectivity.this,"User Login Failed",Toast.LENGTH_LONG).show();
                         }
                        }
                    });
                }
            }
        })*/



       /* mauthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    //enter_game.setEnabled(true);
                    activity();


                }
            }
        };*/

      /* if(mauth.getCurrentUser()!=null)
        {
            startActivity(new Intent(activity_gamectivity.this,MainActivity.class));
        }*/




        /*save.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View view){
                storeData();
            }
        });*/

















   /* public void storeData()
    {
        userInfo userInfomation=new userInfo(name.getText().toString(),email.getText().toString(),phone.getText().toString());
        mRefUser.child("userInfo").push().setValue(userInfomation);




    }*/

    /*@Override
    public void onClick(View view) {

        if(view.getId()==R.id.tv_signup)
        {

            startActivity(new Intent(activity_gamectivity.this,SignUp.class));

        }else if(view.getId()==R.id.tv_password){

            startActivity(new Intent(activity_gamectivity.this,ForgotPassword.class));

        }else if(view.getId()==R.id.enter_game){

            String Email = input_email.getText().toString();
            String Pass = input_password.getText().toString();
            if(!Email.equals("") && !Pass.equals(""))
            {
                mauth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(activity_gamectivity.this, "Log In successful", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(activity_gamectivity.this, "Log In failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
            else
                Toast.makeText(activity_gamectivity.this, "Please write email or password...", Toast.LENGTH_LONG).show();
        }



    }*/

    /*public void loginUser(final String email,final String password) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mauth.signInWithEmailAndPassword(email, password )
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult> ( ) {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(activity_gamectivity.this,MainActivity.class));
                            }else{
                                Toast.makeText(activity_gamectivity.this,"User Login Failed",Toast.LENGTH_LONG).show();
                            }
                        }

                    } );
        }
    }*/











