package ga.winterhills.findroid;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView email_text;
    TextView lang_list;
    private final int REQUEST_SPEECH_RECOGNIZER = 3000;
    private TextView mTextView;
    private final String mQuestion = "Which company is the largest online retailer on the planet?";
    private String mAnswer = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        Intent intentObj = getIntent();
        String email=intentObj.getStringExtra(LoginActivity.UserLoginTask.EXTRA_MASSAGE);
        email_text = findViewById(R.id.email);
        email_text.setText(email);

        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            updateResults("\nNo voice recognition support on your device!");
        } else {
            LanguageDetailsReceiver ldr = new
                    LanguageDetailsReceiver(this);
            sendOrderedBroadcast(RecognizerIntent
                            .getVoiceDetailsIntent(this), null, ldr, null,
                    Activity.RESULT_OK, null, null);
        }

    }

    void updateResults(String s) {
        lang_list = findViewById(R.id.Aval_langs);
        lang_list.setText(s);
    }


}
