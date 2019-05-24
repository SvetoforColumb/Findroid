package ga.winterhills.findroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DrawMap draw2D = new DrawMap(this);
        setContentView(draw2D);
    }

}
