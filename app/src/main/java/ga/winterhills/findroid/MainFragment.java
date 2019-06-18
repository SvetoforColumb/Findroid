package ga.winterhills.findroid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Objects;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;
import edu.cmu.pocketsphinx.SpeechRecognizer;


public class MainFragment extends Fragment {

    TextView email_text;
    TextView lang_list;

    View map_canvas;
    LinearLayout mainLayout;

    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_LOGIN = "Login";// = "False";

    /* Named searches allow to quickly reconfigure the decoder */

    private static final String MENU_SEARCH = "go";
    private static final String KWS_SEARCH = "wakeup";
    private static final String CITY_MOSCOW = "moscow";


    private SpeechRecognizer recognizer;
    private HashMap<String, Integer> captions;
    SharedPreferences mSettings;
    OnViewCreatedListener onViewCreatedListener;
    private AppBarLayout appBarLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        captions = new HashMap<>();
        captions.put(KWS_SEARCH, R.string.kws_caption);
        captions.put(MENU_SEARCH, R.string.menu_caption);
        captions.put(CITY_MOSCOW, R.string.city_Moscow);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        VoiceChatView chatView = (VoiceChatView) getView().findViewById(R.id.chat_view);
        //chatView.addMessage(new ChatMessage("hello\nmessage", System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
    }

    public interface OnViewCreatedListener {

        public void ViewCreated();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onViewCreatedListener = (OnViewCreatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onViewCreatedListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        DrawMap drawMapView = new DrawMap(getContext());
        mainLayout = getView().findViewById(R.id.main_layout);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (56 * scale + 0.5f);
        LinearLayout.LayoutParams drawMapLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, displaymetrics.widthPixels + pixels);
        drawMapView.setLayoutParams(drawMapLayoutParams);
        mainLayout.addView(drawMapView,0);
        onViewCreatedListener.ViewCreated();

    }
}
