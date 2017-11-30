package com.firebaseFen.fenapp.vocabapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Pino on 7/26/2017.
 */



public class words {



    private String Chinese;
    private String English;
    private String Definition;

    public words() {

    }

    public words(String English, String Definition){
        this.English=English;
        this.Definition=Definition;
    }

   public words(String Chinese,String English,String Definition){
        this.Chinese=Chinese;
        this.English=English;
        this.Definition=Definition;
    }

    public String getChinese() {
        return Chinese;
    }

    public void setChinese(String chinese) {
        Chinese = chinese;
    }

    public String getEnglish() {
        return English;
    }

    public void setEnglish(String english) {
        English = english;
    }

    public String getDefinition() {
        return Definition;
    }

    public void setDefinition(String definition) {
        Definition = definition;
    }


}
