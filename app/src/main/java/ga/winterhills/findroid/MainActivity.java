package ga.winterhills.findroid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.NameValuePair;
import java.util.List;
import java.util.ArrayList;
import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity implements
        RecognitionListener {
    TextView email_text;
    TextView lang_list;

    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_LOGIN = "Login";// = "False";

    /* Named searches allow to quickly reconfigure the decoder */

    private static final String MENU_SEARCH = "go";
    private static final String KWS_SEARCH = "wakeup";
    private static final String CITY_MOSCOW = "moscow";

    /* Keyword we are looking for to activate menu */
    private static final String KEYPHRASE = "start";

    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private SpeechRecognizer recognizer;
    private HashMap<String, Integer> captions;
    SharedPreferences mSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean hasVisited = mSettings.getBoolean("hasVisited", false);
        if (!hasVisited) {
            SharedPreferences.Editor e = mSettings.edit();
            e.putBoolean(APP_PREFERENCES_LOGIN, false);
            e.putBoolean("hasVisited", true);
            e.apply();
            Intent intentObj = new Intent(MainActivity.this, LoginActivity.class);
            finish();
            startActivity(intentObj);
        }
        Map map = new Map();
        map.execute((Void) null);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        Intent intentObj = getIntent();
        String email=intentObj.getStringExtra(LoginActivity.UserLoginTask.EXTRA_MASSAGE);
        email_text = findViewById(R.id.email);
        email_text.setText(email);

        // Prepare the data for UI
        captions = new HashMap<>();
        captions.put(KWS_SEARCH, R.string.kws_caption);
        captions.put(MENU_SEARCH, R.string.menu_caption);
        captions.put(CITY_MOSCOW, R.string.city_Moscow);

        ((TextView) findViewById(R.id.caption_text))
                .setText("Preparing the recognizer");

        // Check if user has given permission to record audio
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new SetupTask(this).execute();
    }


    private static class SetupTask extends AsyncTask<Void, Void, Exception> {
        WeakReference<MainActivity> activityReference;
        SetupTask(MainActivity activity) {
            this.activityReference = new WeakReference<>(activity);
        }
        @Override
        protected Exception doInBackground(Void... params) {
            try {
                Assets assets = new Assets(activityReference.get());
                File assetDir = assets.syncAssets();
                activityReference.get().setupRecognizer(assetDir);
            } catch (IOException e) {
                return e;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Exception result) {
            if (result != null) {
                ((TextView) activityReference.get().findViewById(R.id.caption_text))
                        .setText("Failed to init recognizer " + result);
            } else {
                activityReference.get().switchSearch(KWS_SEARCH);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Recognizer initialization is a time-consuming and it involves IO,
                // so we execute it in async task
                new SetupTask(this).execute();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }
    }

    /**
     * In partial result we get quick updates about current hypothesis. In
     * keyword spotting mode we can react here, in other modes we need to wait
     * for final result in onResult.
     */
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        if (text.equals(KEYPHRASE))
            switchSearch(MENU_SEARCH);
        else if (text.equals(MENU_SEARCH))
            switchSearch(MENU_SEARCH);
        else if (text.equals(CITY_MOSCOW))
            switchSearch(CITY_MOSCOW);
//        else if (text.equals(FORECAST_SEARCH))
//            switchSearch(FORECAST_SEARCH);
        else
            ((TextView) findViewById(R.id.result_text)).setText(text);
    }

    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
        ((TextView) findViewById(R.id.result_text)).setText("");
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    /**
     * We stop recognizer here to get a final result
     */
    @Override
    public void onEndOfSpeech() {
        if (!recognizer.getSearchName().equals(KWS_SEARCH))
            switchSearch(KWS_SEARCH);
    }

    private void switchSearch(String searchName) {
        recognizer.stop();

        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else
            recognizer.startListening(searchName, 60000);

        String caption = getResources().getString(captions.get(searchName));
        ((TextView) findViewById(R.id.caption_text)).setText(caption);
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))

                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)

                .getRecognizer();
        recognizer.addListener(this);

        /* In your application you might not need to add all those searches.
          They are added here for demonstration. You can leave just one.
         */

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

        // Create grammar-based search for selection between demos
        File menuGrammar = new File(assetsDir, "menu.gram");
        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);

//        // Create grammar-based search for digit recognition
//        File digitsGrammar = new File(assetsDir, "digits.gram");
//        recognizer.addGrammarSearch(DIGITS_SEARCH, digitsGrammar);
//
//        // Create language model search
//        File languageModel = new File(assetsDir, "weather.dmp");
//        recognizer.addNgramSearch(FORECAST_SEARCH, languageModel);
//
//        // Phonetic search
//        File phoneticModel = new File(assetsDir, "en-phone.dmp");
//        recognizer.addAllphoneSearch(PHONE_SEARCH, phoneticModel);
    }


    @Override
    public void onError(Exception e) {
        ((TextView) findViewById(R.id.caption_text)).setText(e.getMessage());
    }

    @Override
    public void onTimeout() {
        // todo: what to do in timeout
    }

    JSONParser jsonParser = new JSONParser();
    JSONObject json=null;
    String url_getCoorCity="https://findroid.napoleonthecake.ru/getCoorCity.php";
    String url_getWays="https://findroid.napoleonthecake.ru/getWays.php";

    public class Map extends AsyncTask<Void, Void, Boolean> {
        String[] nameOfCities={"Moscow", "Penza", "Chelyabinsk", "Tomsk", "Sochi", "Azov", "Amursk", "Volgograd", "Kazan", "Ufa"};
        City[] country;

        class City {
            String name;
            int id;
            int x;
            int y;
            ArrayList<Way> roads;

            City() {}
            City(String name1, int x1, int y1, int id1) {
                name = name1;
                x = x1;
                y = y1;
                id = id1;
                roads=new ArrayList<>();
            }
        }

        class Way{
            int idFrom;
            int idTo;
            int value;

            Way(int from, int to, int val)
            {
                idFrom =from-1;
                idTo=to-1;
                value=val;
            }
        }

        Map(){
            country=new City[10];
        }

        @Override
        protected Boolean doInBackground(Void... args){
            List<NameValuePair> values = new ArrayList<>();
            json = JSONParser.makeHttpRequest(url_getCoorCity, "GET", values);
            for(int i=0; i<nameOfCities.length; i++){
                try {
                    country[i]=getParamCity(json.getString(nameOfCities[i]), nameOfCities[i]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            json = JSONParser.makeHttpRequest(url_getWays, "GET", values);
            int count=0;
            try {
                while(json.getString("way #"+count)!=null) {
                    Way way=getWay(json.getString("way #"+count));
                    country[way.idFrom].roads.add(way);
                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        City getParamCity(String params, String name) throws JSONException {
            JSONObject jObj = new JSONObject(params);
            City city=new City(name, jObj.getInt("coor_x"), jObj.getInt("coor_y"), jObj.getInt("id"));
            return city;
        }

        Way getWay(String params) throws JSONException {
            JSONObject jObj = new JSONObject(params);
            Way way=new Way(jObj.getInt("cityFrom"), jObj.getInt("cityTo"), jObj.getInt("value"));
            return way;
        }
    }
}
