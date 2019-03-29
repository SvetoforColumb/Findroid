package ga.winterhills.findroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import java.util.ArrayList;
import java.util.List;

public class LanguageDetailsReceiver extends BroadcastReceiver {
    List<String> mLanguages;
    MainActivity main_activity;

    public LanguageDetailsReceiver(MainActivity ssl) {
        main_activity = ssl;
        mLanguages= new ArrayList<String>();
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle extras = getResultExtras(true);
        mLanguages = extras.getStringArrayList
                (RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES);
        if (mLanguages == null) {
            main_activity.updateResults("No voice data found.");
        } else {
            String s = "\nList of language voice data:\n";
            for (int i = 0; i < mLanguages.size(); i++) {
                s += (mLanguages.get(i) + ", ");
            }
            s += "\n";
            main_activity.updateResults(s);
        }
    }
}
