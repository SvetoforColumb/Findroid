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
import android.util.Log;
import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import ga.winterhills.findroid.R;

public class DrawMap extends View {

    private Paint mPaint = new Paint();
    private Rect mRect = new Rect();
    MapData mapData;

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
    }


    class MapData extends AsyncTask<Void, Void, Boolean> {
        String[] nameOfCities={"Moscow", "Penza","Tomsk", "Sochi", "Azov", "Amursk", "Volgograd", "Kazan", "Ufa", "Chelyabinsk"};
        class Block{
            public int x;
            int y;
            int idCity; //0 = нет города;
            String city;
            int block; //image
            int up;
            int down;
            int left;
            int right;
        }

        Block Map[];

        MapData(){
            Map=new Block[100];
            for (int i=0; i<100; i++)
            {
                Map[i]=new Block();
            }
        }

        JSONObject json=null;
        String url_getMap="http://www.zaural-vodokanal.ru/php/rob/ForAndroid/getMap.php";
        String block;

        @Override
        protected Boolean doInBackground(Void... args){
            List<NameValuePair> values = new ArrayList<>();
            values.add(new BasicNameValuePair("login", "dasc")); // TODO: передавать сюда login
            json = JSONParser.makeHttpRequest(url_getMap, "GET", values);
            try {
                for (int i = 0; i < 100; i++) {
                    block = json.getString("block #" + i);
                    Map[i]=oneBlock(block);
                    Log.i("mt1","x= "+Map[i].x+" y= "+Map[i].y+" city= "+Map[i].city+" block= "+Map[i].block);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        private Block oneBlock(String js){
            Block bl=new Block();
            try {
                JSONObject jObj = new JSONObject(js);
                bl.x=jObj.getInt("x");
                bl.y=jObj.getInt("y");
                bl.idCity=jObj.getInt("city");
                if(jObj.getInt("city")!=0)bl.city=nameOfCities[bl.idCity-1];
                else bl.city="";
                bl.block=jObj.getInt("image");
                bl.left=jObj.getInt("left");
                bl.right=jObj.getInt("right");
                bl.up=jObj.getInt("up");
                bl.down=jObj.getInt("down");
            } catch (JSONException e) {
                Log.e("TAG", "Error parsing data " + e.toString());
            }
            return bl;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        }


    }

}