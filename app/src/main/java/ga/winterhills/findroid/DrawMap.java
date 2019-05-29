package ga.winterhills.findroid;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.view.View;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ga.winterhills.findroid.R;

public class DrawMap extends View {

    private Paint mPaint = new Paint();
    private Rect mRect = new Rect();
    //MapData mapData;

    public DrawMap(Context context) {
        super(context);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        // стиль Заливка
        mPaint.setStyle(Paint.Style.FILL);
        // закрашиваем холст белым цветом
        mPaint.setColor(Color.WHITE);
        canvas.drawPaint(mPaint);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);
        canvas.drawLine(300,400,800,900, mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.drawCircle(300,400,50,mPaint);
        canvas.drawCircle(800,900,50,mPaint);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(30);
        canvas.drawText("Moscow", 400,400,mPaint);
        canvas.drawText("Chelyabinsk", 900,900,mPaint);
        canvas.save();
        canvas.restore();
//        mapData = new MapData();
//        mapData.execute((Void) null);
    }

    JSONParser jsonParser = new JSONParser();
    JSONObject json=null;
    String url_getCoorCity="https://findroid.napoleonthecake.ru/getCoorCity.php";
    String url_getWays="https://findroid.napoleonthecake.ru/getWays.php";

//    class MapData extends AsyncTask<Void, Void, Boolean> {
//        String[] nameOfCities={"Moscow", "Penza", "Chelyabinsk", "Kazan", "Sochi", "London", "Berlin", "Volgograd"};
//        City[] country;
//        Canvas canvas;
//        Paint paint;
//
//        class City {
//            String name;
//            int id;
//            int x;
//            int y;
//            ArrayList<Way> roads;
//
//            City(String name1, int x1, int y1, int id1) {
//                name = name1;
//                x = x1;
//                y = y1;
//                id = id1;
//                roads=new ArrayList<>();
//            }
//        }
//
//        class Way{
//            int idFrom;
//            int idTo;
//            int value;
//
//            Way(int from, int to, int val)
//            {
//                idFrom =from-1;
//                idTo=to-1;
//                value=val;
//            }
//        }
//
//        MapData(){
//            country=new City[8];
//        }
//
//
//
//        @Override
//        protected Boolean doInBackground(Void... args){
//            List<NameValuePair> values = new ArrayList<>();
//            json = JSONParser.makeHttpRequest(url_getCoorCity, "GET", values);
//            for(int i=0; i<nameOfCities.length; i++){
//                try {
//                    country[i]=getParamCity(json.getString(nameOfCities[i]), nameOfCities[i]);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            json = JSONParser.makeHttpRequest(url_getWays, "GET", values);
//            int count=0;
//            try {
//                while(json.getString("way #"+count)!=null) {
//                    Way way=getWay(json.getString("way #"+count));
//                    if(way.idFrom<country.length)
//                        country[way.idFrom].roads.add(way);
//                    else break;
//                    count++;
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//
//        }
//
//        City getParamCity(String params, String name) throws JSONException {
//            JSONObject jObj = new JSONObject(params);
//            return new City(name, jObj.getInt("coor_x"), jObj.getInt("coor_y"), jObj.getInt("id"));
//        }
//
//        Way getWay(String params) throws JSONException {
//            JSONObject jObj = new JSONObject(params);
//            return new Way(jObj.getInt("cityFrom"), jObj.getInt("cityTo"), jObj.getInt("value"));
//        }
//    }

}