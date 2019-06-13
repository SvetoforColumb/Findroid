package ga.winterhills.findroid;



import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class User {
    public String login;
    public int money;

    private JSONParser jsonParser = new JSONParser();
    private JSONObject json = null;
    private static final String TAG_SUCCESS = "success";
    private static final String url_getMoney="http://www.zaural-vodokanal.ru/php/rob/ForAndroid/getMoney.php";
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

    private static final String url_setMoney="http://www.zaural-vodokanal.ru/php/rob/ForAndroid/setMoney.php";
    public void setMoney(int newMoney){
        List<NameValuePair> values = new ArrayList<NameValuePair>();
        money=newMoney;
        values.add(new BasicNameValuePair("login", login));
        values.add(new BasicNameValuePair("money", String.valueOf(money)));
        json = jsonParser.makeHttpRequest(url_getMoney, "POST", values);
    }

}
