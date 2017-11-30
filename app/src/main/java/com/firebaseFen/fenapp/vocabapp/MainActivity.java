package com.firebaseFen.fenapp.vocabapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.firebaseFen.fenapp.vocabapp.R.id.settings;

public class MainActivity extends AppCompatActivity {
    private String TAG="MainActivity";
    /*FirebaseDatabase EnglishWith;
    DatabaseReference EnglishWithRef;*/
    RelativeLayout mRelativeLayout;
  List<String> easyChin;
   List<String> mediumChin;
 List<String> hardChin;

    SharedPreferences sp;
    private Context mContext;
    private Activity mActivity;
    private SharedPreferences mSharedPreferences;


    TextView tv_text, hintone,def, hint2, hint3, txtscore, txthighscore;
    Button b_scramble;
    Button b_speakenglish;

    FirebaseAuth mauth;
    FirebaseAuth.AuthStateListener mauthListener;
    
    Button b_speakchinese;
    EditText wordtxt, userinput;
    Button answer;

    int numberofhints = 0;
    TextToSpeech tts;
    TextToSpeech TTS;


    /*private TextToSpeech tts;
    private Button S;
    private TextView getTextToSpeak;*/
   String WORD="";
    String word = "";
    String definition = "";
    int numofcorrect = 0;
    int score = 0;
    int numofwrong = 0;
    public static int diff = 6;
    int scoretosave = 0;
    int HighScore = 0;
    int triesleft = 4;
    int indextodatabase;
    int result;
    private int MY_DATA_CHECK_CODE=0;
    String number;
    int lang=0;
    public static int mdiff;

    public  static String SelOne;
    public  static String SelTwo;
    public  static String SelThree;
    public static String Correct;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish ();
                return true;
            case settings:
                startActivity(new Intent(this, MySetting.class));
                return true;

            case R.id.purple:
                mRelativeLayout.setBackgroundColor(Color.argb(255, 234, 179, 255));
                return true;
            case R.id.white:
                mRelativeLayout.setBackgroundColor(Color.WHITE);
                return true;
            case R.id.cyan:
                mRelativeLayout.setBackgroundColor(Color.CYAN);
                return true;
            case R.id.easy:
                if(lang==0){
                diff = 0;
                }else if(lang==1){
                    diff=10;
                }
                b_scramble.callOnClick();
                return true;
            case R.id.med:
                if(lang==0){
                diff = 1;
                }else if(lang==1){
                    diff=11;
                }
                b_scramble.callOnClick();
                return true;
            case R.id.hard:
                if(lang==0){
                    diff = 2;
                }else if(lang==1){
                    diff=12;
                }
                b_scramble.callOnClick();
                return true;
            case R.id.random:
                if(lang==0){
                    diff = 4;
                }else if(lang==1){
                    mdiff=14;
                }
                b_scramble.callOnClick();
                return true;
            case R.id.streak:
                if(lang==0){
                    diff = 3;
                }else if(lang==1){
                    mdiff=13;
                }
                b_scramble.callOnClick();
                return true;
            case R.id.help:
                startActivity(new Intent(MainActivity.this, Pop.class));
                return true;
            case R.id.save:
                if (score > HighScore) {
                    HighScore = score;
                    scoretosave = score;
                    SaveScores(scoretosave);
                } else
                    Toast.makeText(getBaseContext(), "Your score did not beat the Highscore", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.load:
                LoadScores();
                return true;
            case R.id.clear:
                ClearScore();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActionBar();

        mRelativeLayout = (RelativeLayout) findViewById(R.id.primary_relativeLayout);
          sp=PreferenceManager.getDefaultSharedPreferences(this);
        hintone = (TextView) findViewById(R.id.txthintone);
        //hinttwo = (EditText) findViewById(R.id.txthinetwo);
        //hintthree = (EditText) findViewById(R.id.txthintthree);
        //wordtxt = (EditText) findViewById(R.id.txtword);
        userinput = (EditText) findViewById(R.id.txtinput);
        Button hintbtn = (Button) findViewById(R.id.btnHInt);
        //Button nextbtn = (Button) findViewById(R.id.btnnext);
        b_scramble = (Button) findViewById(R.id.btnnext);
        //b_s = (Button) findViewById((R.id.b_s));
        b_speakenglish=(Button)findViewById(R.id.bt_speakenglish);
        b_speakenglish.setVisibility(View.INVISIBLE);
        b_speakchinese=(Button)findViewById(R.id.bt_speakchinese);
        b_speakchinese.setVisibility(View.INVISIBLE);


        tv_text = (TextView) findViewById(R.id.txtviewword);
        tv_text.setTextSize(28);
        def = (TextView) findViewById(R.id.txtviewdef);
        hint2 = (TextView) findViewById(R.id.txtviewhint2);
        hint3 = (TextView) findViewById(R.id.txtview3rdhint);
        txtscore = (TextView) findViewById(R.id.txtviewscore);  txtscore.setText("Score:100 " );
        txthighscore = (TextView) findViewById(R.id.txthighscores);
        mContext=getApplicationContext();
        mActivity=MainActivity.this;
        //mSharedPreferences=PreferenceManager.getDefaultSharedPreferences(mContext);
        PreferenceManager.setDefaultValues(this,R.xml.settings_screen,false);
      number=sp.getString(MySetting.DIFFICULTY_LIST_PREF,"2");

        b_scramble.setFocusable(true);
        b_scramble.setFocusableInTouchMode(true);
        b_scramble.requestFocus();

        def.setVisibility(View.INVISIBLE);
/*answer=(Button)findViewById(R.id.bt_answer);
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/


      easyChin = new ArrayList<String>();

        try {
            InputStream is = getAssets().open("EasyDifficultyChinese.txt");
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line = reader.readLine();
            while (line != null) {
                easyChin.add(line);
                line = reader.readLine();

            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        FirebaseDatabase easyC=FirebaseDatabase.getInstance();
        DatabaseReference easyCref=easyC.getReference("ChineseandEnglishWithDefinition").child("Easychinese");

       
    /*for(int i=0;i<easyChin.size();i++){
            easyCref.push().setValue(easyChin.get(i).toString());
        }*/


        easyCref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String easy=dataSnapshot.getValue(String.class);


                    easyChin.add(easy);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String easy=dataSnapshot.getValue(String.class);


                easyChin.remove(easy);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



      mediumChin = new ArrayList<String>();

        try {
            InputStream is = getAssets().open("MediumDifficultyChinese.txt");
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line = reader.readLine();
            while (line != null) {
                mediumChin.add(line);
                line = reader.readLine();

            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }


        FirebaseDatabase mediumC=FirebaseDatabase.getInstance();
        DatabaseReference mediumCref=mediumC.getReference("ChineseandEnglishWithDefinition").child("Mediumchinese");

       /*for(int i=0;i<mediumChin.size();i++){
            mediumCref.push().setValue(mediumChin.get(i).toString());
        }*/

        mediumCref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String medium=dataSnapshot.getValue(String.class);

                    mediumChin.add(medium);

            }

            @Override

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String medium=dataSnapshot.getValue(String.class);

                mediumChin.remove(medium);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




      hardChin = new ArrayList<String>();

        try {
            InputStream is = getAssets().open("HardDifficultyChinese.txt");
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line = reader.readLine();
            while (line != null) {
                hardChin.add(line);
                line = reader.readLine();

            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        FirebaseDatabase hardC=FirebaseDatabase.getInstance();
        final DatabaseReference hardCref=hardC.getReference("ChineseandEnglishWithDefinition").child("Hardchinese");
        /*for(int i=0;i<hardChin.size();i++){
            hardCref.push().setValue(hardChin.get(i).toString());
        }*/


        hardCref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String hard=dataSnapshot.getValue(String.class);

                    hardChin.add(hard);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String hard=dataSnapshot.getValue(String.class);

                hardChin.remove(hard);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final List<String> easyantsyn = new ArrayList<String>();

        try {
            InputStream is = getAssets().open("Easyantandsyn.txt");
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line = reader.readLine();
            while (line != null) {
                easyantsyn.add(line);
                line = reader.readLine();

            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }


        final List<String> mediumantsyn = new ArrayList<String>();

        try {
            InputStream is = getAssets().open("Mediumantandsyn.txt");
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line = reader.readLine();
            while (line != null) {
                mediumantsyn.add(line);
                line = reader.readLine();

            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }


        final List<String> hardantsyn = new ArrayList<String>();

        try {
            InputStream is = getAssets().open("Hardantandsyn.txt");
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line = reader.readLine();
            while (line != null) {
                hardantsyn.add(line);
                line = reader.readLine();

            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        final List<String> easysentence = new ArrayList<String>();

        try {
            InputStream is = getAssets().open("EasyChineseSentence.txt");
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line = reader.readLine();
            while (line != null) {
                easysentence.add(line);
                line = reader.readLine();

            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        final List<String> mediumsentence = new ArrayList<String>();

        try {
            InputStream is = getAssets().open("MediumChineseSentence.txt");
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line = reader.readLine();
            while (line != null) {
                mediumsentence .add(line);
                line = reader.readLine();

            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        final List<String> hardsentence = new ArrayList<String>();

        try {
            InputStream is = getAssets().open("HardChineseSentence.txt");
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line = reader.readLine();
            while (line != null) {
                hardsentence .add(line);
                line = reader.readLine();

            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }





        final int difficulty = GameActivity.mDifficulty;
        final int language = GameActivity.mLanguage;

        //easy = (RadioButton) findViewById(R.id.RGdiff).findViewById(R.id.radeasy);
        if (language == 0) {
            lang=0;
            if (difficulty == 0) {
                diff = 0;

            } else if (difficulty == 1) {
                diff = 1;

            } else if (difficulty == 2) {
                diff = 2;

            } else if (difficulty == 3) {
                diff = 3;

            } else if (difficulty == 4) {
                diff = 4;

            }
        } else if (language == 1) {
              lang=1;
            if (difficulty == 0) {
                diff = 10;

            } else if (difficulty == 1) {
                diff = 11;

            } else if (difficulty == 2) {
                diff = 12;

            }else if(difficulty==3){
              mdiff=13;
            }
            else if(difficulty==4){
                mdiff=14;
            }

        }




       tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
           public void onInit(int status) {
               if (status != TextToSpeech.ERROR)
                    tts.setLanguage(Locale.US);

                }

               //b_scramble.callOnClick();
               //LoadScores();

       });


        TTS = new TextToSpeech ( getApplicationContext(), new TextToSpeech.OnInitListener ( ) {
            @Override
              public void onInit(int status){
                  if(status==TextToSpeech.SUCCESS){
                      result=TTS.setLanguage(Locale.CHINESE);
                  }else{
                      Toast.makeText(getApplicationContext(),
                              "Feature not supported in your Device",
                              Toast.LENGTH_SHORT).show();
                  }
              }
          });









        //myRef= FirebaseDatabase.getInstance().getReference();

        //word = word.toLowerCase();
        //String scrambledword = ScrambleWord(word);
        //wordtxt.setText(scrambledword);
        final List<String> synlist = new ArrayList<>();

        try {
            InputStream is = getAssets().open("antandsyn.txt");
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line = reader.readLine();
            while (line != null) {
                synlist.add(line);
                line = reader.readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        final List<String> sentences = new ArrayList<String>();
        try {
            InputStream is = getAssets().open("wordsandsentecnes.txt");
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line = reader.readLine();
            while (line != null) {
                sentences.add(line);
                line = reader.readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        final List<String> lines = new ArrayList<String>();

        try {
            InputStream is = getAssets().open("EnglishWithDefinition.txt");
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();

            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
         /*EnglishWith=FirebaseDatabase.getInstance();
       EnglishWithRef=EnglishWith.getReference("EnglishWithDefinition");
            //EnglishWithRef.child("EnglishWithDefinition").push().setValue(new words("jjj","hhjhj"));



         EnglishWithRef.addChildEventListener(new ChildEventListener() {
             @Override
             public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                 String englishwith=dataSnapshot.getValue(String.class);
                 lines.add(englishwith);
             }

             @Override
             public void onChildChanged(DataSnapshot dataSnapshot, String s) {

             }

             @Override
             public void onChildRemoved(DataSnapshot dataSnapshot) {

             }

             @Override
             public void onChildMoved(DataSnapshot dataSnapshot, String s) {

             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });*/

        if (diff == 0)
        {
            Easy_Difficulty(lines);
        }
        //medium
        if (diff == 1)
        {
            Med_Difficulty(lines);
        }
        //hard
        if (diff == 2)
        {
            Hard_Difficulty(lines);
        }
        //random
        if (diff == 3)
        {
            Streak_Difficulty(lines);

        }
        //streak
        if (diff == 4)
        {
            Random_Difficulty(lines);
        }
        if(diff==10)
        {
            easyChinese(easyChin);
        }
        if(diff==11)
        {
            mediumChinese(mediumChin);
        }
        if(diff==12){
            hardChinese(hardChin);
        }
        if(mdiff==13){
            streakChinese();
        }
        if(mdiff==14){
            randomChinese();
        }

        b_scramble.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                    b_speakchinese.setVisibility(View.INVISIBLE);
                    b_speakenglish.setVisibility(View.INVISIBLE);

                    //easy
                    if (diff == 0) {
                        Easy_Difficulty ( lines );
                    }
                    //medium
                    if (diff == 1) {
                        Med_Difficulty ( lines );
                    }
                    //hard
                    if (diff == 2) {
                        Hard_Difficulty ( lines );
                    }
                    //random
                    if (diff == 3) {
                        Streak_Difficulty ( lines );
                    }
                    //streak
                    if (diff == 4) {
                        Random_Difficulty ( lines );

                    }
                    if (diff == 10) {
                        easyChinese ( easyChin );
                    }
                    if (diff == 11) {
                        mediumChinese ( mediumChin );
                    }
                    if (diff == 12) {
                        hardChinese ( hardChin );
                    }
                    if (mdiff == 13) {
                        streakChinese ( );
                    }
                    if (mdiff == 14) {
                        randomChinese ( );
                    }
                    String empty = "";
                    hintone.setText ( empty );
                    //hinttwo.setText(empty);
                    hint2.setText ( empty );
                    hint3.setText ( empty );
                    numberofhints = 0;
                    userinput.setText ( empty );



            }


        });
        hintbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!word.isEmpty()) {
                    if (numberofhints < 5) {
                        numberofhints++;
                    }
                    if (numberofhints == 1) {
                        score-=10;
                        //first hint first letter of the given word
                        if (language == 0) {
                            String firstLetter = String.valueOf(word.charAt(0));
                            hintone.setText("The word starts with a "+"' "+firstLetter+" '");
                        } else if (language == 1) {
                            String FirstLetter = String.valueOf(word.charAt(0));
                            hintone.setText("The word starts with a "+"' "+FirstLetter+" '");
                        }
                    } else if (numberofhints == 2) {
                        score-=10;
                        //second hint sentence

                        String sentword = " ";
                        String sentence = "A dog is a man's best friend.";

                        int index = 0;
                        if (language == 0) {
                            while (true) {
                                String[] sentpart = sentences.get(index).split(":");
                                sentword = sentpart[0];
                                if (CheckWord(word, sentword)) {
                                    sentence = sentpart[1];
                                    break;
                                } else if (index == sentences.size()) {
                                    break;
                                }
                                index++;
                            }
                            //hinttwo.setText(sentence);
                            hint2.setText(sentence);
                        } else if (language == 1) {
                            if (diff == 10) {
                                while (true) {
                                    String[] sentpart = easysentence.get(index).split(":");
                                    sentword = sentpart[0];
                                    if (CheckWord(word, sentword)) {
                                        sentence = sentpart[1];
                                        break;
                                    } else if (index == easysentence.size()) {
                                        break;
                                    }
                                    index++;
                                }

                                hint2.setText(sentence);

                            } else if (diff == 11) {
                                while (true) {
                                    String[] sentpart = mediumsentence.get(index).split(":");
                                    sentword = sentpart[0];
                                    if (CheckWord(word, sentword)) {
                                        sentence = sentpart[1];
                                        break;
                                    } else if (index == mediumsentence.size()) {
                                        break;
                                    }
                                    index++;
                                }

                                hint2.setText(sentence);

                            } else if (diff == 12) {
                                while (true) {
                                    String[] sentpart = hardsentence.get(index).split(":");
                                    sentword = sentpart[0];
                                    if (CheckWord(word, sentword)) {
                                        sentence = sentpart[1];
                                        break;
                                    } else if (index == hardsentence.size()) {
                                        break;
                                    }
                                    index++;
                                }

                                hint2.setText(sentence);

                            }
                        }
                    } else if (numberofhints == 3) {
                        score-=10;
                           if(triesleft>=0){
                               AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                               alertDialog.setTitle("               4rd hint");
                               alertDialog.setMessage("You have run through all 3 hints, Press HINT again to get 4rd hint");
                               alertDialog.show();
                           }

                        //third hint syn and ant

                        if (language == 0) {
                            String synandant = "pup, doggo, doggy, puppo, puppy";
                            String synword = " ";
                            int index = 0;
                            while (true) {
                                String[] sentpart = synlist.get(index).split(":");
                                synword = sentpart[0];
                                if (CheckWord(word, synword)) {
                                    synandant = sentpart[1];
                                    break;
                                } else if (index == synlist.size()) {
                                    break;
                                }
                                index++;
                            }
                            hint3.setText(synandant);
                        } else if (language == 1) {
                            String synandant = "pup, doggo, doggy, puppo, puppy";
                            String synword = " ";
                            int index = 0;
                            if (diff == 10) {


                                while (true) {
                                    String[] sentpart = easyantsyn.get(index).split(":");
                                    synword = sentpart[0];
                                    if (CheckWord(word, synword)) {
                                        synandant = sentpart[1];
                                        break;
                                    } else if (index == easyantsyn.size()) {
                                        break;
                                    }
                                    index++;
                                }
                                hint3.setText(synandant);
                            } else if (diff == 11) {


                                while (true) {
                                    String[] sentpart = mediumantsyn.get(index).split(":");
                                    synword = sentpart[0];
                                    if (CheckWord(word, synword)) {
                                        synandant = sentpart[1];
                                        break;
                                    } else if (index == mediumantsyn.size()) {
                                        break;
                                    }
                                    index++;
                                }
                                hint3.setText(synandant);
                            } else if (diff == 12) {


                                while (true) {
                                    String[] sentpart = hardantsyn.get(index).split(":");
                                    synword = sentpart[0];
                                    if (CheckWord(word, synword)) {
                                        synandant = sentpart[1];
                                        break;
                                    } else if (index == hardantsyn.size()) {
                                        break;
                                    }
                                    index++;
                                }
                                hint3.setText(synandant);
                            }
                        }


                    }else if (numberofhints == 4) {
                        score-=10;
                          if(language==0){
                              b_speakenglish.setVisibility(View.VISIBLE);
                          }else if(language==1){
                              b_speakenglish.setVisibility(View.VISIBLE);
                              b_speakchinese.setVisibility(View.VISIBLE);
                          }


                    }
                }
            }
        });

        userinput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        String userInput = userinput.getText().toString();
                        if (CheckWord(word, userInput) == true) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            TextView t = (TextView) toast.getView().findViewById(android.R.id.message);
                            t.setTextColor(Color.GREEN);
                            toast.show();
                            numofcorrect++;
                            if(language==0){
                            AddScore(word);
                            }else if(language==1){
                                if(diff==10){
                                  AddChineseScore(easyChin);
                                }else if(diff==11){
                                  AddChineseScore(mediumChin);
                                }else if(diff==12){
                                    AddChineseScore(hardChin);
                                }

                            }

                            txtscore.setText("Score: " + score);
                            b_scramble.callOnClick();
                        } else if (CheckWord(word, userInput) == false) {
                            triesleft--;


                                if (triesleft > 1) {
                                    Toast toast = Toast.makeText ( getApplicationContext ( ), "Wrong! You have " + triesleft + " lives left" + ". Please guess again!", Toast.LENGTH_SHORT );
                                    toast.setGravity ( Gravity.CENTER_VERTICAL, 0, 0 );
                                    TextView b = (TextView) toast.getView ( ).findViewById ( android.R.id.message );
                                    b.setTextColor ( Color.RED );
                                    toast.show ( );


                                } else if (triesleft == 1) {
                                    Toast toast = Toast.makeText ( getApplicationContext ( ), "Wrong! You have " + triesleft + " life left" + ". Please guess again!", Toast.LENGTH_SHORT );
                                    toast.setGravity ( Gravity.CENTER_VERTICAL, 0, 0 );
                                    TextView b = (TextView) toast.getView ( ).findViewById ( android.R.id.message );
                                    b.setTextColor ( Color.RED );
                                    toast.show ( );
                                }

                            String empty = "";
                            userinput.setText(empty);
                            numofcorrect = 0;
                            numofwrong++;

                            if (numofwrong > 3) {
                                Toast endgametoast = Toast.makeText(getApplicationContext(), "Game over!", Toast.LENGTH_SHORT);
                                TextView g = (TextView) endgametoast.getView().findViewById(android.R.id.message);
                                g.setTextColor(Color.RED);
                                endgametoast.show();
                                if (score > HighScore) {
                                    Toast.makeText(getBaseContext(), "Congratulations you beat your Highscore!", Toast.LENGTH_SHORT).show();
                                    HighScore = score;
                                    scoretosave = score;
                                    SaveScores(scoretosave);
                                    LoadScores();
                                }
                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog.setTitle("Alert");
                                alertDialog.setMessage("Game Over! Would you like to play again?");
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                b_scramble.callOnClick();
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(MainActivity.this,GameActivity.class);
                                                startActivity(intent);
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                                triesleft = 4;
                                numofwrong = 0;
                                score = 0;
                                txtscore.setText("Score:");
                            }
                        }
                        return true;
                    }
                }
                return false;
            }



        });




    }


public void selectThree(View v)
{
SingleChoice three_dialog=new SingleChoice();
    three_dialog.show(getSupportFragmentManager(),"three dialog");
}


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
             //startActivity(new Intent(MainActivity.this,GameActivity.class));
            //NavUtils.navigateUpFromSameTask(this);
            //finish();

    }

    }




    //gets a random word to display to the user
    public void Random_Difficulty(List<String> lines) {
        word = "";
        String text = "";
        Random r = new Random();
        text = lines.get(r.nextInt(lines.size()));
        String[] parts = text.split(":",11);
        word = parts[1];
        word = word.toLowerCase();
        //String scramble = ScrambleWord(word);
        definition = parts[1];
        tv_text.setText(word);
        def.setText(definition);
    }

    //the more you get correct in a row the harder it gets
    public void Streak_Difficulty(List<String> lines) {
        String text = "";
        while (true) {
            word = "";
            Random r = new Random();
            text = lines.get(r.nextInt(lines.size()));
            String[] parts = text.split(":",11);
            word = parts[1];
            word = word.toLowerCase();
            if (CheckDifficulty(word) == 0 && numofcorrect <=2) {
                //String scramble = ScrambleWord(word);
                definition = parts[2];
                tv_text.setText(word);
                def.setText(definition);
                break;
            } else if (CheckDifficulty(word) == 1 && numofcorrect > 2 && numofcorrect < 6) {
                //String scramble = ScrambleWord(word);
                definition = parts[2];
                tv_text.setText(word);
                def.setText(definition);
                break;
            } else if (CheckDifficulty(word) == 2 && numofcorrect >=6) {
                //String scramble = ScrambleWord(word);
                definition = parts[2];
                tv_text.setText(word);
                def.setText(definition);
                break;
            }
        }
    }

    public void Easy_Difficulty(List<String> lines)
    {
        String text = "";
        while (true)
        {
            word = "";
            Random r = new Random();
            text = lines.get(r.nextInt(lines.size()));
            String[] parts = text.split(":",11);
            word = parts[1];
            word = word.toLowerCase();
            if(CheckDifficulty(word) == 0)
            {
                //String scramble = ScrambleWord(word);

                definition = parts[2];
                tv_text.setText(word);
                def.setText(definition);
                SelOne=parts[3];
                SelTwo=parts[4];
                SelThree=parts[5];
                Correct=parts[0];
                break;
            }
        }
    }
    public void Med_Difficulty(List<String> lines)
    {
        String text = "";
        while (true)
        {
            word = "";
            Random r = new Random();
            text = lines.get(r.nextInt(lines.size()));
            String[] parts = text.split(":",11);
            word = parts[1];
            word = word.toLowerCase();
            if(CheckDifficulty(word) == 1)
            {
                //String scramble = ScrambleWord(word);
                definition = parts[2];
                tv_text.setText(word);
                def.setText(definition);
                SelOne=parts[3];
                SelTwo=parts[4];
                SelThree=parts[5];
                Correct=parts[0];
                break;
            }
        }
    }
    public void Hard_Difficulty(List<String> lines)
    {
        String text = "";
        while (true)
        {
            word = "";
            Random r = new Random();
            text = lines.get(r.nextInt(lines.size()));
            String[] parts = text.split(":",11);
            word = parts[1];
            word = word.toLowerCase();
            if(CheckDifficulty(word) == 2)
            {
                //String scramble = ScrambleWord(word);
                definition = parts[2];
                tv_text.setText(word);
                def.setText(definition);
                SelOne=parts[3];
                SelTwo=parts[4];
                SelThree=parts[5];
                Correct=parts[0];
                break;
            }
        }
    }

    public String ScrambleWord(String _string) {
        Random rand = new Random();
        char a[] = _string.toCharArray();

        for (int i = 0; i < a.length; i++) {
            //switching the letter around with a random index each time
            int j = rand.nextInt(a.length);
            char temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }

        return new String(a);
    }



    public int CheckDifficulty(String a) {
        if (a.length() <= 6) {
            //easy
            return 0;
        } else if (a.length() > 6 && a.length() < 9) {
            //medium
            return 1;
        } else {
            //hard
            return 2;
        }
    }

    public int CheckChineseDifficulty(List<String> asentence)
    {
        if(asentence.equals("easyChin")) {
            return 10;
        }else if(asentence.equals("mediumChin")){
            return 11;
        }else {
        return 12;
        }

    }

    public void easyChinese(List<String> asentence) {
        String text = "";
        while (true) {


                WORD = "";
                word = "";
                Random r = new Random();
                text = asentence.get(r.nextInt(asentence.size()));
                String[] parts = text.split(":", 11);
                WORD = parts[0];



                    word = parts[1];
                    definition = parts[2];
            SelOne=parts[3];
            SelTwo=parts[4];
            SelThree=parts[5];
            Correct=parts[1];


                tv_text.setText(WORD);

                def.setText(definition);
                break;


            }





    }

    public void mediumChinese(List<String> asentence) {
        String text = "";
        while (true) {

            WORD = "";
            word = "";
            Random r = new Random();
            text = asentence.get(r.nextInt(asentence.size()));
            String[] parts = text.split(":", 11);
            WORD = parts[0];
            word = parts[1];
            definition = parts[2];
            tv_text.setText(WORD);
            def.setText(definition);
            SelOne=parts[3];
            SelTwo=parts[4];
            SelThree=parts[5];
            Correct=parts[1];
            break;
        }

    }

    public void hardChinese(List<String> asentence) {
        String text = "";
        while (true) {

                WORD = "";
                word = "";
                Random r = new Random();
                text = asentence.get(r.nextInt(asentence.size()));
                String[] parts = text.split(":", 11);
                WORD = parts[0];
                word = parts[1];
                definition = parts[2];
                tv_text.setText(WORD);
                def.setText(definition);
            SelOne=parts[3];
            SelTwo=parts[4];
            SelThree=parts[5];
            Correct=parts[1];
                break;


        }

    }

    //the more you get correct in a row the harder it gets
    public void streakChinese() {



            if (numofcorrect <=2) {
                easyChinese(easyChin);
                diff=10;

            } else if (numofcorrect > 2 && numofcorrect <6) {
              mediumChinese(mediumChin);
                diff=11;

            } else if (numofcorrect >= 6) {
               hardChinese(hardChin);
                diff=12;

            }

    }



    public void randomChinese() {
        Random rand = new Random();
        int i=rand.nextInt(3)+1;
        if(i==1){

         easyChinese(easyChin);
            diff=10;


        }else if(i==2){

         mediumChinese(mediumChin);
            diff=11;

        }else if(i==3){

            hardChinese(hardChin);
            diff=12;

        }


    }



    public boolean CheckWord(String a, String b) {

        a = a.toLowerCase();
        b = b.toLowerCase();
        return a.equals(b);
    }

    public void speakOut(View view) {
        String string = word;
        tts.speak(string, TextToSpeech.QUEUE_FLUSH, null, null);
    }

  public void SPEAKOUT(View view) {
      if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
          Toast.makeText(getApplicationContext(),
                  "Feature no supported in your Device",
                  Toast.LENGTH_SHORT).show();
      } else{

      String sstring = WORD;
      TTS.speak(sstring, TextToSpeech.QUEUE_FLUSH, null,null);
  }
    }

    



    public void AddScore(String _string) {
        if (CheckDifficulty(_string) == 0) {
            score = score + 100;
        } else if (CheckDifficulty(_string) == 1) {
            score = score + 200;
        } else if (CheckDifficulty(_string) == 2) {
            score = score + 300;
        }

    }

    public void AddChineseScore(List<String> asentence){
        if(asentence==easyChin){
            score=score+100;
        }else if(asentence==mediumChin){
            score=score+200;
        }else if(asentence==hardChin){
            score=score+300;
        }
    }



    public void SaveScores(int _score) {
        // add-write text into file
        try {
            String s = String.valueOf(_score);
            FileOutputStream fileout = openFileOutput("scores.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(s);
            outputWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "Score saved successfully!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        LoadScores();
    }
    public void LoadScores() {
        //reading text from file
        try {
            String put = "";
            InputStream in = openFileInput("scores.txt");
            BufferedReader reader = new BufferedReader((new InputStreamReader(in)));
            String line = reader.readLine();
            while (line != null) {
                put = line;
                line = reader.readLine();
            }
            txthighscore.setText("Highscore: " + put);
            reader.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ClearScore() {
        score = 0;
        txtscore.setText("Score: ");
        try {
            FileOutputStream fileout = openFileOutput("scores.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(0);
            outputWriter.close();

            Toast.makeText(getBaseContext(), "Score cleared successfully!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        LoadScores();
    }


   /* private void writeNewEnglishwithid(String wordId,String English,String Definition){
        words w=new words(English,Definition);
        EnglishWithRef.child("EnglishWithDefinition").child(wordId).setValue(w);

    }*/





        /*tts = new TextToSpeech(this, this);

        getTextToSpeak = (TextView) findViewById(R.id.textView);
        S = (Button) findViewById(R.id.S);
        S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakOutNow();
            }
        });

    }


    public void onInit(int text) {
        if (text == TextToSpeech.SUCCESS){
            int language = tts.setLanguage(Locale.ENGLISH);
        if (language == TextToSpeech.LANG_MISSING_DATA || language == getTextToSpeak.LANG_NOT_SUPPORTED) {
            S.setEnabled(true);
            speakOutNow();
        } else {
        }
    }
    else
    {

    }

}

    public void speakOutNow()
    {
        String text=getTextToSpeak.getText().toString();
        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }
}
*/

}