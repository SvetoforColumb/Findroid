package ga.winterhills.findroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        Intent intentObj = getIntent();
        String email=intentObj.getStringExtra(LoginActivity.UserLoginTask.EXTRA_MASSAGE);
        TextView textView = findViewById(R.id.email);
        textView.setText(email);
    }
}
