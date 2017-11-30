package com.firebaseFen.fenapp.vocabapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import static com.firebaseFen.fenapp.vocabapp.MainActivity.Correct;
import static com.firebaseFen.fenapp.vocabapp.MainActivity.SelOne;
import static com.firebaseFen.fenapp.vocabapp.MainActivity.SelThree;
import static com.firebaseFen.fenapp.vocabapp.MainActivity.SelTwo;

/**
 * Created by Pino on 9/23/2017.
 */

public class SingleChoice extends DialogFragment {

    CharSequence[] items={SelOne,SelTwo,SelThree};
    String selection;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your answer");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                switch (arg1) {
                    case 0:
                        selection = (String) items[arg1];
                        break;
                    case 1:
                        selection = (String) items[arg1];
                        break;
                    case 2:
                        selection = (String) items[arg1];
                        break;
                }
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (Correct.equals(selection)) {
                    //Toast.makeText(getActivity(), selection, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "Correct ! !", Toast.LENGTH_SHORT).show();
                }
                 else
                 {
                     Toast.makeText(getActivity(), "Wrong answer:(", Toast.LENGTH_SHORT).show();
                 }
            }
        });
        builder.setNegativeButton("Cancel",null);
        return builder.create();
    }

}



