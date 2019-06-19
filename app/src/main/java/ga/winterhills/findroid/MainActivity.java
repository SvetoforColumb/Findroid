package ga.winterhills.findroid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.intentservice.chatui.models.ChatMessage;
import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecognitionListener, MainFragment.OnViewCreatedListener {

    public interface WayInterface {
        public void showResult(boolean result);
    }

    User user;
    Way way;

    TextView email_text;
    TextView money_text;

    View map_canvas;
    LinearLayout mainLayout;

    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_LOGIN = "Login";// = "False";

    /* Named searches allow to quickly reconfigure the decoder */

    private static final String MENU_SEARCH = "go";
    private static final String KWS_SEARCH = "wakeup";
    private static final String CITY_Moskow = "moscow";
    private static final String CITY_Penza = "penza";
    private static final String CITY_Sochi = "sochi";
    private static final String CITY_Volgograd = "volgograd";
    private static final String CITY_Kazan = "kazan";
    private static final String CITY_Chelyabinsk = "chelyabinsk";

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
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String email = mSettings.getString("email", "");
        user = new User(email);
        user.execute();
        SharedPreferences.Editor e = mSettings.edit();
        e.putString("robot", String.valueOf(1));
        e.apply();
        //enter login activity if not login

        //boolean hasVisited = mSettings.getBoolean("hasVisited", false);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        Fragment fragment = null;
        Class fragmentClass =  MainFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        for (int i = 0; i < user.robots.size(); i++){
            menu.add(0,user.robots.get(i),user.robots.get(i) ,user.robots.get(i) + " robot");
        }



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        SharedPreferences.Editor e = mSettings.edit();
        e.putString("robot", String.valueOf(id));
        e.apply();
        Toast toast = Toast.makeText(this, "Robot " + id + " selected", Toast.LENGTH_SHORT);
        toast.show();
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass = null;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentClass = MainFragment.class;
        } else if (id == R.id.nav_shop) {
            fragmentClass = ShopFragment.class;
        } else if (id == R.id.nav_log_out) {
            Intent intentObj = new Intent(MainActivity.this, LoginActivity.class);
            finish();
            startActivity(intentObj);
        } else if (id == R.id.nav_exit) {
            finish();
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        item.setChecked(true);
        setTitle(item.getTitle());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void ViewCreated() {
        captions = new HashMap<>();
        captions.put(KWS_SEARCH, R.string.kws_caption);
        captions.put(MENU_SEARCH, R.string.menu_caption);
        captions.put(CITY_Moskow, R.string.city_Moscow);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String email = mSettings.getString("email", "");
        email_text = findViewById(R.id.email_nav);
        email_text.setText(email);
        money_text = findViewById(R.id.money_nav);
        String money = user.money + " $";
        money_text.setText(money);
//        ((TextView) findViewById(R.id.caption_text)) // todo: add to chatview
//                .setText("Preparing the recognizer");
        VoiceChatView chatView = (VoiceChatView) findViewById(R.id.chat_view);
        chatView.addMessage(new ChatMessage("Preparing the recognizer", System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
        // Check if user has given permission to record audio
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }

      //  }
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new MainActivity.SetupTask(this).execute();
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
                VoiceChatView chatView = (VoiceChatView) activityReference.get().findViewById(R.id.chat_view);
                chatView.addMessage(new ChatMessage("Failed to init recognizer " + result, System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
            } else {
                activityReference.get().switchSearch(KWS_SEARCH);
            } // todo: add to chatview
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
                new MainActivity.SetupTask(this).execute();
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
        VoiceChatView chatView = (VoiceChatView) findViewById(R.id.chat_view);
        String text = hypothesis.getHypstr();
        if (text.equals(KEYPHRASE)) {
            chatView.addMessage(new ChatMessage(text, System.currentTimeMillis(), ChatMessage.Type.SENT));
            switchSearch(MENU_SEARCH);
        }
        else if (text.equals(MENU_SEARCH)){
            chatView.addMessage(new ChatMessage(text, System.currentTimeMillis(), ChatMessage.Type.SENT));
            switchSearch(MENU_SEARCH);
        }
        else {
            chatView.addMessage(new ChatMessage(text, System.currentTimeMillis(), ChatMessage.Type.SENT));
        }
    }

    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
//        ((TextView) findViewById(R.id.result_text)).setText("");

        int robot_id = Integer.parseInt(mSettings.getString("robot", "1"));
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            VoiceChatView chatView = (VoiceChatView) findViewById(R.id.chat_view);
            if (text.equals(CITY_Moskow)) { // id 1
                Way task = new Way(robot_id, 1,  new WayInterface() {
                    @Override
                    public void showResult(boolean result) {
                        if (result) {
                            Toast.makeText(MainActivity.this, "Succesful",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                task.execute();
                chatView.addMessage(new ChatMessage("go" + text, System.currentTimeMillis(), ChatMessage.Type.SENT));
                chatView.addMessage(new ChatMessage("Going Moskow", System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
//                switchSearch(MENU_SEARCH);
            }
            else if (text.equals(CITY_Penza)) { // 2
                Way task = new Way(robot_id, 2,  new WayInterface() {
                    @Override
                    public void showResult(boolean result) {
                        if (result) {
                            Toast.makeText(MainActivity.this, "Succesful",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                task.execute();
                chatView.addMessage(new ChatMessage("go" + text, System.currentTimeMillis(), ChatMessage.Type.SENT));
                chatView.addMessage(new ChatMessage("Going Penza", System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
//                switchSearch(MENU_SEARCH);
            }
            else if (text.equals(CITY_Sochi)) { // 4
                Way task = new Way(robot_id, 4,  new WayInterface() {
                    @Override
                    public void showResult(boolean result) {
                        if (result) {
                            Toast.makeText(MainActivity.this, "Succesful",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                task.execute();
                chatView.addMessage(new ChatMessage("go" + text, System.currentTimeMillis(), ChatMessage.Type.SENT));
                chatView.addMessage(new ChatMessage("Going Sochi", System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
//                switchSearch(MENU_SEARCH);
            }
            else if (text.equals(CITY_Kazan)) { // 8
                Way task = new Way(robot_id, 8,  new WayInterface() {
                    @Override
                    public void showResult(boolean result) {
                        if (result) {
                            Toast.makeText(MainActivity.this, "Succesful",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                task.execute();
                chatView.addMessage(new ChatMessage("go" + text, System.currentTimeMillis(), ChatMessage.Type.SENT));
                chatView.addMessage(new ChatMessage("Going Kazan", System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
//                switchSearch(MENU_SEARCH);
            }
            else if (text.equals(CITY_Volgograd)) {// 7
                Way task = new Way(robot_id, 7,  new WayInterface() {
                    @Override
                    public void showResult(boolean result) {
                        if (result) {
                            Toast.makeText(MainActivity.this, "Succesful",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                task.execute();
                chatView.addMessage(new ChatMessage("go" + text, System.currentTimeMillis(), ChatMessage.Type.SENT));
                chatView.addMessage(new ChatMessage("Going Volgograd", System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
//                switchSearch(MENU_SEARCH);
            }
            else if (text.equals(CITY_Chelyabinsk)) { // 10
                Way task = new Way(robot_id, 10,  new WayInterface() {
                    @Override
                    public void showResult(boolean result) {
                        if (result) {
                            Toast.makeText(MainActivity.this, "Succesful",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                task.execute();
                chatView.addMessage(new ChatMessage("go" + text, System.currentTimeMillis(), ChatMessage.Type.SENT));
                chatView.addMessage(new ChatMessage("Going Chelyabinsk", System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
                //switchSearch(MENU_SEARCH);
            }
            //chatView.addMessage(new ChatMessage(text, System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
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
        VoiceChatView chatView = (VoiceChatView) findViewById(R.id.chat_view);
        chatView.addMessage(new ChatMessage(caption, System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
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
        VoiceChatView chatView = (VoiceChatView) findViewById(R.id.chat_view);
        chatView.addMessage(new ChatMessage(e.getMessage(), System.currentTimeMillis(), ChatMessage.Type.RECEIVED));

    }

    @Override
    public void onTimeout() {
        switchSearch(KWS_SEARCH);
    }

    String url_getWay="http://www.zaural-vodokanal.ru/php/rob/ForAndroid/get_way_get.php";

    JSONObject json = null;



    class Way extends AsyncTask<Void, Void, Boolean> {
        public int idRobot;
        public int idCityTo;
        private WayInterface resultListener;

        public Way(int idRobot, int idCityTo, WayInterface resultListener) {
            this.idRobot = idRobot;
            this.idCityTo = idCityTo;
            this.resultListener  = resultListener;
        }



        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                List<NameValuePair> values = new ArrayList<NameValuePair>();
                values.add(new BasicNameValuePair("robot_id", String.valueOf(idRobot)));
                values.add(new BasicNameValuePair("city_id", String.valueOf(idCityTo)));
                json = JSONParser.makeHttpRequest(url_getWay, "GET", values);
                Thread.sleep(100);
                int success;
                success = json.getInt("success");
                if (success == 1) {
                    return true;
                } else{
                    return false;
                }
            } catch (InterruptedException e) {
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (resultListener != null)
                resultListener.showResult(aBoolean);
        }



    }


}
