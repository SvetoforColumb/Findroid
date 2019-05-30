package ga.winterhills.findroid;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import edu.cmu.pocketsphinx.SpeechRecognizer;


public class MainFragment extends Fragment {

    TextView email_text;
    TextView lang_list;

    DrawMap drawMap;
    View map_canvas;
    LinearLayout mainLayout;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        captions = new HashMap<>();
        captions.put(KWS_SEARCH, R.string.kws_caption);
        captions.put(MENU_SEARCH, R.string.menu_caption);
        captions.put(CITY_MOSCOW, R.string.city_Moscow);




        ((TextView) view.findViewById(R.id.caption_text))
                .setText("Preparing the recognizer");

        // Check if user has given permission to record audio
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return view;
        }
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        //new MainActivity.SetupTask(this).execute();

        mainLayout = view.findViewById(R.id.main_layout);
        mainLayout.addView(new DrawMap(getContext()));
        return view;
    }


}
