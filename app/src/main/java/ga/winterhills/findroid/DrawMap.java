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
import org.apache.http.message.BasicNameValuePair;
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
        int width = getWidth();
        int height = getHeight();
        mPaint.setTextSize(40);
        //canvas.drawText(width + " " + height, 100, 300, mPaint);
        canvas.save();
        canvas.restore();
//        mapData = new MapData();
//        mapData.execute((Void) null);
    }

    JSONParser jsonParser = new JSONParser();
    JSONObject json=null;
    String url_getMap="http://www.zaural-vodokanal.ru/php/rob/ForAndroid/getMap.php";
    String url_getWays="http://www.zaural-vodokanal.ru/php/rob/ForAndroid/getWays.php";

    class MapData extends AsyncTask<Void, Void, Boolean> {
        String[] nameOfCities={"Moscow", "Penza","Tomsk", "Sochi", "Azov", "Amursk", "Volgograd", "Kazan", "Ufa", "Chelyabinsk"};
        class Block{
            int x;
            int y;
            int idCity; //0 = нет города;
            String city;
            int block; //image
            boolean up;
            boolean down;
            boolean left;
            boolean right;
        }

        Block Map[];

        MapData(){
            Map=new Block[100];
        }



        @Override
        protected Boolean doInBackground(Void... args){
            List<NameValuePair> values = new ArrayList<>();
            values.add(new BasicNameValuePair("login", "dasc")); // TODO: передавать сюда login
            json = JSONParser.makeHttpRequest(url_getMap, "GET", values);
            try {
                for (int i = 0; i < 100; i++) {
                    JSONObject block = new JSONObject(json.getString("block #" + i));
                    Map[i].x=block.getInt("x");
                    Map[i].y=block.getInt("y");
                    Map[i].idCity=block.getInt("city");
                    Map[i].city=nameOfCities[Map[i].idCity-1];
                    Map[i].block=block.getInt("image");
                    Map[i].left=block.getBoolean("left");
                    Map[i].right=block.getBoolean("right");
                    Map[i].up=block.getBoolean("up");
                    Map[i].down=block.getBoolean("down");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        }


    }

}