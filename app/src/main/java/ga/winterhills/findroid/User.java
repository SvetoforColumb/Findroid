package ga.winterhills.findroid;



import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class User extends AsyncTask<Void, Void, Boolean> {
    public String login;
    public ArrayList<Integer> robots;
    public int money;


    boolean firstLaunch =true;
    public boolean setMoney=false;


    String url_getMoney="http://www.zaural-vodokanal.ru/php/rob/ForAndroid/getMoney.php";
    private static final String url_getRobots="http://www.zaural-vodokanal.ru/php/rob/ForAndroid/getRobots.php";
    private static final String url_setMoney="http://www.zaural-vodokanal.ru/php/rob/ForAndroid/setMoney.php";

    User(String login){
        this.login = login;
    }

    protected Boolean doInBackground(Void... args){
            getMoney();
            robots = new ArrayList<Integer>();
            try {
                List<NameValuePair> values = new ArrayList<NameValuePair>();
                values.add(new BasicNameValuePair("login", login));
                json = jsonParser.makeHttpRequest(url_getRobots, "GET", values);
                int count = json.getInt("count");
                if (count == 0)
                    Log.i("Tttat", "нет роботов");
                else {
                    for (int i = 0; i < count; i++) {
                        robots.add(json.getInt("robot #" + i));
                        Log.i("Tttat", "robot #" + json.getInt("robot #" + i));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        return true;
    }

    private JSONParser jsonParser = new JSONParser();
    private JSONObject json = null;
    private static final String TAG_SUCCESS = "success";
    public int getMoney(){
        try {
            List<NameValuePair> values = new ArrayList<NameValuePair>();
            values.add(new BasicNameValuePair("login", login));
            json = jsonParser.makeHttpRequest(url_getMoney, "GET", values);
            money=json.getInt("money");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return money;
    }

    public void setMoney(int newMoney){
        money=newMoney;
        Log.i("Tttat", "money "+money);
        Log.i("Tttat", "new money "+newMoney);
        Money money = new Money();
        money.newMoney=newMoney;
        money.execute();
    }



    public class Money extends AsyncTask<Void, Void, Void>{
        public int newMoney;
        @Override
        protected Void doInBackground(Void... voids) {
            List<NameValuePair> values = new ArrayList<NameValuePair>();
            values.add(new BasicNameValuePair("login", login));
            values.add(new BasicNameValuePair("money", String.valueOf(newMoney)));
            Log.i("Tttat", "test money "+newMoney);
            json = jsonParser.makeHttpRequest(url_setMoney, "GET", values);
            return null;
        }
    }

}
