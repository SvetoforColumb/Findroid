package ga.winterhills.findroid;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
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
    boolean taskDone;
    private Paint mPaint = new Paint();
    private Rect mRect = new Rect();
    MapData mapData;

    public DrawMap(Context context) {
        super(context);
        mapData = new MapData();
        mapData.execute();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        synchronized(this) {
            try {
                wait(1000);
            } catch(InterruptedException ie){}
        }
        int width = getWidth();
        int height = getHeight();
        // стиль Заливка
        mPaint.setStyle(Paint.Style.FILL);
        // закрашиваем холст белым цветом
        mPaint.setColor(Color.WHITE);
        canvas.drawPaint(mPaint);
        mPaint.setAntiAlias(true);
        Bitmap map_block_0000 = BitmapFactory.decodeResource(getResources(), R.drawable.map_0000);
        Bitmap map_block_0001 = BitmapFactory.decodeResource(getResources(), R.drawable.map_0001);
        Bitmap map_block_0010 = BitmapFactory.decodeResource(getResources(), R.drawable.map_0010);
        Bitmap map_block_0011 = BitmapFactory.decodeResource(getResources(), R.drawable.map_0011);
        Bitmap map_block_0100 = BitmapFactory.decodeResource(getResources(), R.drawable.map_0100);
        Bitmap map_block_0101 = BitmapFactory.decodeResource(getResources(), R.drawable.map_0101);
        Bitmap map_block_0110 = BitmapFactory.decodeResource(getResources(), R.drawable.map_0110);
        Bitmap map_block_0111 = BitmapFactory.decodeResource(getResources(), R.drawable.map_0111);
        Bitmap map_block_1000 = BitmapFactory.decodeResource(getResources(), R.drawable.map_1000);
        Bitmap map_block_1001 = BitmapFactory.decodeResource(getResources(), R.drawable.map_1001);
        Bitmap map_block_1010 = BitmapFactory.decodeResource(getResources(), R.drawable.map_1010);
        Bitmap map_block_1011 = BitmapFactory.decodeResource(getResources(), R.drawable.map_1011);
        Bitmap map_block_1100 = BitmapFactory.decodeResource(getResources(), R.drawable.map_1100);
        Bitmap map_block_1101 = BitmapFactory.decodeResource(getResources(), R.drawable.map_1101);
        Bitmap map_block_1110 = BitmapFactory.decodeResource(getResources(), R.drawable.map_1110);
        Bitmap map_block_1111 = BitmapFactory.decodeResource(getResources(), R.drawable.map_0001);
        map_block_0000= map_block_0000.copy(Bitmap.Config.ARGB_8888, true);
        map_block_0001= map_block_0001.copy(Bitmap.Config.ARGB_8888, true);
        map_block_0010= map_block_0010.copy(Bitmap.Config.ARGB_8888, true);
        map_block_0011= map_block_0011.copy(Bitmap.Config.ARGB_8888, true);
        map_block_0100= map_block_0100.copy(Bitmap.Config.ARGB_8888, true);
        map_block_0101= map_block_0101.copy(Bitmap.Config.ARGB_8888, true);
        map_block_0110= map_block_0110.copy(Bitmap.Config.ARGB_8888, true);
        map_block_0111= map_block_0111.copy(Bitmap.Config.ARGB_8888, true);
        map_block_1000= map_block_1000.copy(Bitmap.Config.ARGB_8888, true);
        map_block_1001= map_block_1001.copy(Bitmap.Config.ARGB_8888, true);
        map_block_1010= map_block_1010.copy(Bitmap.Config.ARGB_8888, true);
        map_block_1011= map_block_1011.copy(Bitmap.Config.ARGB_8888, true);
        map_block_1100= map_block_1100.copy(Bitmap.Config.ARGB_8888, true);
        map_block_1101= map_block_1101.copy(Bitmap.Config.ARGB_8888, true);
        map_block_1110= map_block_1110.copy(Bitmap.Config.ARGB_8888, true);
        map_block_1111= map_block_1111.copy(Bitmap.Config.ARGB_8888, true);
        map_block_0000.setHeight(height/10);
        map_block_0000.setWidth(width/10);
        map_block_0001.setHeight(height/10);
        map_block_0001.setWidth(width/10);
        map_block_0010.setHeight(height/10);
        map_block_0010.setWidth(width/10);
        map_block_0011.setHeight(height/10);
        map_block_0011.setWidth(width/10);
        map_block_0100.setHeight(height/10);
        map_block_0100.setWidth(width/10);
        map_block_0101.setHeight(height/10);
        map_block_0101.setWidth(width/10);
        map_block_0110.setHeight(height/10);
        map_block_0110.setWidth(width/10);
        map_block_0111.setHeight(height/10);
        map_block_0111.setWidth(width/10);
        map_block_1000.setHeight(height/10);
        map_block_1000.setWidth(width/10);
        map_block_1001.setHeight(height/10);
        map_block_1001.setWidth(width/10);
        map_block_1010.setHeight(height/10);
        map_block_1010.setWidth(width/10);
        map_block_1011.setHeight(height/10);
        map_block_1011.setWidth(width/10);
        map_block_1100.setHeight(height/10);
        map_block_1100.setWidth(width/10);
        map_block_1101.setHeight(height/10);
        map_block_1101.setWidth(width/10);
        map_block_1110.setHeight(height/10);
        map_block_1110.setWidth(width/10);
        map_block_1111.setHeight(height/10);
        map_block_1111.setWidth(width/10);
        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        canvas.drawBitmap(map_block_0000, 0, mActionBarSize, null);




        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);
        canvas.drawLine(300,400,800,900, mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.drawCircle(300,400,50,mPaint);
        canvas.drawCircle(800,900,50,mPaint);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(30);
        canvas.drawText(String.valueOf(mapData.Map[0].block), 400,400,mPaint);
        canvas.drawText("Chelyabinsk", 900,900,mPaint);
        mPaint.setTextSize(40);
        //canvas.drawText(width + " " + height, 100, 300, mPaint);
        canvas.save();
        canvas.restore();
    }


    class MapData extends AsyncTask<Void, Void, Boolean> {
        DrawMap drawMap;
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

        public Block Map[];

        MapData(){
            //this.drawMap = drawMap;
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

