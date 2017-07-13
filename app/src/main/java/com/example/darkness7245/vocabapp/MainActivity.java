package com.example.darkness7245.vocabapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.Random;

public class MainActivity extends AppCompatActivity { //implements TextToSpeech.OnInitListener

    ConstraintLayout constraintLayout;

    TextView tv_text, def, hint2, hint3, txtscore, txthighscore;
    Button b_scramble;
    Button b_s;
    EditText hintone, wordtxt, userinput;

    int numberofhints = 0;
    TextToSpeech tts;
    /*private TextToSpeech tts;
    private Button S;
    private TextView getTextToSpeak;*/

    String word = "";
    String definition = "";
    int numofcorrect = 0;
    int score = 0;
    int numofwrong = 0;
    int diff = 3;
    int scoretosave = 0;
    int HighScore = 0;
    int triesleft = 4;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popup_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        constraintLayout=(ConstraintLayout)findViewById(R.id.primary_constraintLayout);
        switch (item.getItemId()) {
            case R.id.settings:
                return true;
            case R.id.purple:
                constraintLayout.setBackgroundColor(Color.argb(255, 234, 179, 255));
                return true;
            case R.id.white:
                constraintLayout.setBackgroundColor(Color.WHITE);
                 return true;
            case R.id.cyan:
                constraintLayout.setBackgroundColor(Color.CYAN);
                return true;
            case R.id.easy:
                diff = 0;
                b_scramble.callOnClick();
                return true;
            case R.id.med:
                diff = 1;
                b_scramble.callOnClick();
                return true;
            case R.id.hard:
                diff = 2;
                b_scramble.callOnClick();
                return true;
            case R.id.random:
                diff = 3;
                b_scramble.callOnClick();
                return true;
            case R.id.streak:
                diff = 4;
                b_scramble.callOnClick();
                return true;
            case R.id.help:
                startActivity(new Intent(MainActivity.this,Pop.class));
                return true;
            case R.id.save:
                if (score > HighScore)
                {
                    HighScore = score;
                    scoretosave = score;
                    SaveScores(scoretosave);
                }
                else
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
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR)
                    tts.setLanguage(Locale.US);
                b_scramble.callOnClick();
                LoadScores();
            }
        });

        int difficulty = getIntent().getIntExtra("example", -1);

        //easy = (RadioButton) findViewById(R.id.RGdiff).findViewById(R.id.radeasy);
        if (difficulty == 0)
        {
            diff = 0;
        }
        else if (difficulty == 1)
        {
            diff = 1;
        }
        else if (difficulty == 2)
        {
            diff = 2;
        }
        else if (difficulty == 3)
        {
            diff = 4;
        }
        else if (difficulty == 4)
        {
            diff = 3;
        }

        hintone = (EditText) findViewById(R.id.txthintone);
        //hinttwo = (EditText) findViewById(R.id.txthinetwo);
        //hintthree = (EditText) findViewById(R.id.txthintthree);
        //wordtxt = (EditText) findViewById(R.id.txtword);
        userinput = (EditText) findViewById(R.id.txtinput);
        Button hintbtn = (Button) findViewById(R.id.btnHInt);
        //Button nextbtn = (Button) findViewById(R.id.btnnext);
        b_scramble = (Button) findViewById(R.id.btnnext);
        //b_s = (Button) findViewById((R.id.b_s));
        tv_text = (TextView) findViewById(R.id.txtviewword);
        def = (TextView) findViewById(R.id.txtviewdef);
        hint2 = (TextView) findViewById(R.id.txtviewhint2);
        hint3 = (TextView) findViewById(R.id.txtview3rdhint);
        txtscore = (TextView) findViewById(R.id.txtviewscore);
        txthighscore = (TextView) findViewById(R.id.txthighscores);

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


        b_scramble.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                List<String> lines = new ArrayList<String>();


                String text = " ";
                try {
                    InputStream is = getAssets().open("10EnglishWithDefinition.txt");
                    BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
                    String line = reader.readLine();
                    while (line != null) {
                        lines.add(line);
                        line = reader.readLine();

                    }
                    Random r = new Random();
                    text = lines.get(r.nextInt(lines.size()));
                } catch (IOException ex) {

                    ex.printStackTrace();
                }
                //easy
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
                    Random_Difficulty(lines);
                }
                //streak
                if (diff == 4)
                {
                    Streak_Difficulty(lines);
                }
                String empty = "";
                hintone.setText(empty);
                //hinttwo.setText(empty);
                hint2.setText(empty);
                hint3.setText(empty);
                numberofhints = 0;
                userinput.setText(empty);
            }


        });
        hintbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!word.isEmpty()) {
                    if (numberofhints < 4) {
                        numberofhints++;
                    }
                    if (numberofhints == 1) {
                        //first hint first letter of the given word
                        String firstLetter = String.valueOf(word.charAt(0));
                        hintone.setText(firstLetter);
                    } else if (numberofhints == 2) {
                        //second hint sentence

                        String sentword = " ";
                        String sentence = "A dog is a man's best friend.";

                        int index = 0;
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
                    } else if (numberofhints == 3) {
                        //third hint syn and ant
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
                            AddScore(word);
                            txtscore.setText("Score: " + score);
                            b_scramble.callOnClick();
                        } else if (CheckWord(word, userInput) == false) {
                            triesleft--;
                            if(triesleft > 1) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Wrong! You have " + triesleft + " lives left" + ". Please guess again!", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                TextView b = (TextView) toast.getView().findViewById(android.R.id.message);
                                b.setTextColor(Color.RED);
                                toast.show();
                            }
                            else if (triesleft == 1)
                            {
                                Toast toast = Toast.makeText(getApplicationContext(), "Wrong! You have " + triesleft + " life left" + ". Please guess again!", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                TextView b = (TextView) toast.getView().findViewById(android.R.id.message);
                                b.setTextColor(Color.RED);
                                toast.show();
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
    //gets a random word to display to the user
    public void Random_Difficulty(List<String> lines) {
        word = "";
        String text = "";
        Random r = new Random();
        text = lines.get(r.nextInt(lines.size()));
        String[] parts = text.split(":");
        word = parts[0];
        word = word.toLowerCase();
        String scramble = ScrambleWord(word);
        definition = parts[1];
        tv_text.setText(scramble);
        def.setText(definition);
    }

    //the more you get correct in a row the harder it gets
    public void Streak_Difficulty(List<String> lines) {
        String text = "";
        while (true) {
            word = "";
            Random r = new Random();
            text = lines.get(r.nextInt(lines.size()));
            String[] parts = text.split(":");
            word = parts[0];
            word = word.toLowerCase();
            if (CheckDifficulty(word) == 0 && numofcorrect < 4) {
                String scramble = ScrambleWord(word);
                definition = parts[1];
                tv_text.setText(scramble);
                def.setText(definition);
                break;
            } else if (CheckDifficulty(word) == 1 && numofcorrect > 3 && numofcorrect < 7) {
                String scramble = ScrambleWord(word);
                definition = parts[1];
                tv_text.setText(scramble);
                def.setText(definition);
                break;
            } else if (CheckDifficulty(word) == 2 && numofcorrect > 6) {
                String scramble = ScrambleWord(word);
                definition = parts[1];
                tv_text.setText(scramble);
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
            String[] parts = text.split(":");
            word = parts[0];
            word = word.toLowerCase();
            if(CheckDifficulty(word) == 0)
            {
                String scramble = ScrambleWord(word);
                definition = parts[1];
                tv_text.setText(scramble);
                def.setText(definition);
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
            String[] parts = text.split(":");
            word = parts[0];
            word = word.toLowerCase();
            if(CheckDifficulty(word) == 1)
            {
                String scramble = ScrambleWord(word);
                definition = parts[1];
                tv_text.setText(scramble);
                def.setText(definition);
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
            String[] parts = text.split(":");
            word = parts[0];
            word = word.toLowerCase();
            if(CheckDifficulty(word) == 2)
            {
                String scramble = ScrambleWord(word);
                definition = parts[1];
                tv_text.setText(scramble);
                def.setText(definition);
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

    public boolean CheckWord(String a, String b) {

        a = a.toLowerCase();
        b = b.toLowerCase();
        return a.equals(b);
    }

    public void speakOut(View view) {
        String string = word;
        tts.speak(string, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    public void AddScore(String _string) {
        if (CheckDifficulty(_string) == 0) {
            score = score + 200;
        } else if (CheckDifficulty(_string) == 1) {
            score = score + 400;
        } else if (CheckDifficulty(_string) == 2) {
            score = score + 800;
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