package ga.winterhills.findroid;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.marcinmoskala.arcseekbar.ArcSeekBar;
import com.marcinmoskala.arcseekbar.ProgressListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static ga.winterhills.findroid.MainActivity.APP_PREFERENCES;


public class ShopFragment extends Fragment {
    View view;

    public ShopFragment() {

    }
    ArcSeekBar speedBar;
    ArcSeekBar energyBar;
    TextView setSpeedView;
    TextView setEnergyView;
    TextView priceView;
    EditText city;
    Button buy;
    SharedPreferences mSettings;

    JSONObject json = null;
    private static final String url_buy="http://www.zaural-vodokanal.ru/php/rob/ForAndroid/buy.php";
    int speed=5;
    int energy=5;
    int price=0;
    String cityTo;

    User user;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shop, container, false);

        speedBar = (ArcSeekBar) view.findViewById(R.id.speed);
        energyBar = (ArcSeekBar) view.findViewById(R.id.energy);
        setSpeedView = (TextView) view.findViewById(R.id.setSpeed);
        setEnergyView = (TextView) view.findViewById(R.id.setEnergy);
        priceView = (TextView) view.findViewById(R.id.price);

        speedBar.setOnProgressChangedListener(new ProgressListener() {
            @Override
            public void invoke(int i) {
                speed=speedBar.getProgress()+1;
                setSpeedView.setText(String.valueOf(speed));
                price = speed*22+energy*33;
                priceView.setText(String.valueOf(price));
            }
        });

        energyBar.setOnProgressChangedListener(new ProgressListener() {
            @Override
            public void invoke(int i) {
                energy=energyBar.getProgress()+5;
                setEnergyView.setText(String.valueOf(energy));
                price = speed*22+energy*33;
                priceView.setText(String.valueOf(price));
            }
        });
        city = view.findViewById(R.id.setCity);
        buy = view.findViewById(R.id.buy);
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        final String login = mSettings.getString("email", "");
        buy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Buy buy = new Buy(login);
                buy.execute();
                cityTo=city.getText().toString();
                while(buy.success==-1) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(buy.success==1){
                    Log.i("Tttat", "пиписька должна быть тут");
                    TextView textView = view.findViewById(R.id.success);
                    textView.setText("Покупка прошла успешно");
                    user.setMoney(user.money-price);
                } else{
                    Log.i("Tttat", "пиписька должна быть не тут");
                    TextView textView = view.findViewById(R.id.success);
                    textView.setText("Такой город не найден. Повторите попытку");
                }
            }
        });
        return view;
    }

    public class Buy extends AsyncTask<Void, Void, Void>{
        public int success= -1;
        String email;

        public Buy(String email) {
            this.email = email;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                List<NameValuePair> values = new ArrayList<NameValuePair>();
                values.add(new BasicNameValuePair("login", email));
                values.add(new BasicNameValuePair("energy", String.valueOf(energy)));
                values.add(new BasicNameValuePair("speed", String.valueOf(speed)));
                values.add(new BasicNameValuePair("price", String.valueOf(price)));
                values.add(new BasicNameValuePair("idCity", String.valueOf(getIdCity(cityTo))));
                json = JSONParser.makeHttpRequest(url_buy, "GET", values);
                Thread.sleep(100);
                int sucs;
                sucs = json.getInt("success");
                Log.i("Tttat", ""+sucs);
                if (sucs == 1) {
                    success=1;
                    Log.i("Tttat", ""+success);
                } else{
                    success=0;
                }
            } catch (InterruptedException e) {
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private int getIdCity(String cityStr){
            String[] nameOfCities={"Moscow", "Penza","Tomsk", "Sochi", "Azov", "Amursk", "Volgograd", "Kazan", "Ufa", "Chelyabinsk"};

            for(int i=0; i<10;i++){
                if(cityStr.equals(nameOfCities[i])) { return i;}
            }
            return 10;
        }
    }

}
