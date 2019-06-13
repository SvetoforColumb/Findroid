package ga.winterhills.findroid;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrawMap extends View {
    boolean taskDone;
    private Paint mPaint = new Paint();
    private Rect mRect = new Rect();
    MapData mapData;
    int[] x;
    int[] y;
    HashMap<Integer, Bitmap> block_map;
    Bitmap map_block_0000;
    Bitmap map_block_0001;
    Bitmap map_block_0010;
    Bitmap map_block_0011;
    Bitmap map_block_0100;
    Bitmap map_block_0101;
    Bitmap map_block_0110;
    Bitmap map_block_0111;
    Bitmap map_block_1000;
    Bitmap map_block_1001;
    Bitmap map_block_1010;
    Bitmap map_block_1011;
    Bitmap map_block_1100;
    Bitmap map_block_1101;
    Bitmap map_block_1110;
    Bitmap map_block_1111;
    Bitmap map_block_0000m;
    Bitmap map_block_0001m;
    Bitmap map_block_0010m;
    Bitmap map_block_0011m;
    Bitmap map_block_0100m;
    Bitmap map_block_0101m;
    Bitmap map_block_0110m;
    Bitmap map_block_0111m;
    Bitmap map_block_1000m;
    Bitmap map_block_1001m;
    Bitmap map_block_1010m;
    Bitmap map_block_1011m;
    Bitmap map_block_1100m;
    Bitmap map_block_1101m;
    Bitmap map_block_1110m;
    Bitmap map_block_1111m;

    public DrawMap(Context context) {
        super(context);
        mapData = new MapData();
        mapData.execute();
        x  = new int[100];
        y = new int[100];
        block_map = new HashMap<Integer, Bitmap>();
        map_block_0000 = BitmapFactory.decodeResource(getResources(), R.drawable.map_0000);
        map_block_0001 = BitmapFactory.decodeResource(getResources(), R.drawable.map_0001);
        map_block_0010 = BitmapFactory.decodeResource(getResources(), R.drawable.map_0010);
        map_block_0011 = BitmapFactory.decodeResource(getResources(), R.drawable.map_0011);
        map_block_0100 = BitmapFactory.decodeResource(getResources(), R.drawable.map_0100);
        map_block_0101 = BitmapFactory.decodeResource(getResources(), R.drawable.map_0101);
        map_block_0110 = BitmapFactory.decodeResource(getResources(), R.drawable.map_0110);
        map_block_0111 = BitmapFactory.decodeResource(getResources(), R.drawable.map_0111);
        map_block_1000 = BitmapFactory.decodeResource(getResources(), R.drawable.map_1000);
        map_block_1001 = BitmapFactory.decodeResource(getResources(), R.drawable.map_1001);
        map_block_1010 = BitmapFactory.decodeResource(getResources(), R.drawable.map_1010);
        map_block_1011 = BitmapFactory.decodeResource(getResources(), R.drawable.map_1011);
        map_block_1100 = BitmapFactory.decodeResource(getResources(), R.drawable.map_1100);
        map_block_1101 = BitmapFactory.decodeResource(getResources(), R.drawable.map_1101);
        map_block_1110 = BitmapFactory.decodeResource(getResources(), R.drawable.map_1110);
        map_block_1111 = BitmapFactory.decodeResource(getResources(), R.drawable.map_1111);
        map_block_0000m = convertToMutable(map_block_0000);
        map_block_0001m = convertToMutable(map_block_0001);
        map_block_0010m = convertToMutable(map_block_0010);
        map_block_0011m = convertToMutable(map_block_0011);
        map_block_0100m = convertToMutable(map_block_0100);
        map_block_0101m = convertToMutable(map_block_0101);
        map_block_0110m = convertToMutable(map_block_0110);
        map_block_0111m = convertToMutable(map_block_0111);
        map_block_1000m = convertToMutable(map_block_1000);
        map_block_1001m = convertToMutable(map_block_1001);
        map_block_1010m = convertToMutable(map_block_1010);
        map_block_1011m = convertToMutable(map_block_1011);
        map_block_1100m = convertToMutable(map_block_1100);
        map_block_1101m = convertToMutable(map_block_1101);
        map_block_1110m = convertToMutable(map_block_1110);
        map_block_1111m = convertToMutable(map_block_1111);

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
//
//        map_block_0000= map_block_0000.copy(Bitmap.Config.ARGB_8888, true);
//        map_block_0001= map_block_0001.copy(Bitmap.Config.ARGB_8888, true);
//        map_block_0010= map_block_0010.copy(Bitmap.Config.ARGB_8888, true);
//        map_block_0011= map_block_0011.copy(Bitmap.Config.ARGB_8888, true);
//        map_block_0100= map_block_0100.copy(Bitmap.Config.ARGB_8888, true);
//        map_block_0101= map_block_0101.copy(Bitmap.Config.ARGB_8888, true);
//        map_block_0110= map_block_0110.copy(Bitmap.Config.ARGB_8888, true);
//        map_block_0111= map_block_0111.copy(Bitmap.Config.ARGB_8888, true);
//        map_block_1000= map_block_1000.copy(Bitmap.Config.ARGB_8888, true);
//        map_block_1001= map_block_1001.copy(Bitmap.Config.ARGB_8888, true);
//        map_block_1010= map_block_1010.copy(Bitmap.Config.ARGB_8888, true);
//        map_block_1011= map_block_1011.copy(Bitmap.Config.ARGB_8888, true);
//        map_block_1100= map_block_1100.copy(Bitmap.Config.ARGB_8888, true);
//        map_block_1101= map_block_1101.copy(Bitmap.Config.ARGB_8888, true);
//        map_block_1110= map_block_1110.copy(Bitmap.Config.ARGB_8888, true);
//        map_block_1111= map_block_1111.copy(Bitmap.Config.ARGB_8888, true);
        int blocksize = height/10;
        map_block_0000m = getResizedBitmap(map_block_0000,blocksize,blocksize);
        map_block_0001m = getResizedBitmap(map_block_0001,blocksize,blocksize);
        map_block_0010m = getResizedBitmap(map_block_0010,blocksize,blocksize);
        map_block_0011m = getResizedBitmap(map_block_0011,blocksize,blocksize);
        map_block_0100m = getResizedBitmap(map_block_0100,blocksize,blocksize);
        map_block_0101m = getResizedBitmap(map_block_0101,blocksize,blocksize);
        map_block_0110m = getResizedBitmap(map_block_0110,blocksize,blocksize);
        map_block_0111m = getResizedBitmap(map_block_0111,blocksize,blocksize);
        map_block_1000m = getResizedBitmap(map_block_1000,blocksize,blocksize);
        map_block_1001m = getResizedBitmap(map_block_1001,blocksize,blocksize);
        map_block_1010m = getResizedBitmap(map_block_1010,blocksize,blocksize);
        map_block_1011m = getResizedBitmap(map_block_1011,blocksize,blocksize);
        map_block_1100m = getResizedBitmap(map_block_1100,blocksize,blocksize);
        map_block_1101m = getResizedBitmap(map_block_1101,blocksize,blocksize);
        map_block_1110m = getResizedBitmap(map_block_1110,blocksize,blocksize);
        map_block_1111m = getResizedBitmap(map_block_1111,blocksize,blocksize);
        block_map.put(0,map_block_0000m);
        block_map.put(1,map_block_0001m);
        block_map.put(10,map_block_0010m);
        block_map.put(11,map_block_0011m);
        block_map.put(100,map_block_0100m);
        block_map.put(101,map_block_0101m);
        block_map.put(110,map_block_0110m);
        block_map.put(111,map_block_0111m);
        block_map.put(1000,map_block_1000m);
        block_map.put(1001,map_block_1001m);
        block_map.put(1010,map_block_1010m);
        block_map.put(1011,map_block_1011m);
        block_map.put(1100,map_block_1100m);
        block_map.put(1101,map_block_1101m);
        block_map.put(1110,map_block_1110m);
        block_map.put(1111,map_block_1111m);
        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                x[i*10 + j] = i * blocksize + 1;
                y[i*10 + j] = 85 + (j * blocksize);
            }
        }
        for (int i= 0; i <100; i++){
           canvas.drawBitmap(block_map.get(mapData.Map[i].block), x[i] , y[i], null);

           }
        for (int i= 0; i <100; i++){
            if (mapData.Map[i].idCity != 0){
                mPaint.setColor(Color.GREEN);
                canvas.drawCircle(x[i] + (blocksize % 2),y[i] + (blocksize % 2) + 2,10,mPaint);
                mPaint.setColor(Color.WHITE);
                mPaint.setTextSize(15);
                canvas.drawText(mapData.Map[i].city,x[i] + (blocksize % 2) + 10 ,y[i] + (blocksize % 2) + 20,mPaint);

            }
        }
        canvas.save();
        canvas.restore();
    }


    String[] nameOfCities={"Moscow", "Penza","Tomsk", "Sochi", "Azov", "Amursk", "Volgograd", "Kazan", "Ufa", "Chelyabinsk"};
    class MapData extends AsyncTask<Void, Void, Boolean> {
        DrawMap drawMap;
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
            values.add(new BasicNameValuePair("login", "dasc")); // TODO: передавать сюда login вместо dasc
            json = JSONParser.makeHttpRequest(url_getMap, "GET", values);
            try {
                for (int i = 0; i < 100; i++) {
                    block = json.getString("block #" + i);
                    Map[i]=oneBlock(block);

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
    public static Bitmap convertToMutable(Bitmap imgIn) {
        try {
            //this is the file going to use temporally to save the bytes.
            // This file will not be a image, it will store the raw image data.
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");

            //Open an RandomAccessFile
            //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
            //into AndroidManifest.xml file
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

            // get the width and height of the source bitmap.
            int width = imgIn.getWidth();
            int height = imgIn.getHeight();
            Bitmap.Config type = imgIn.getConfig();

            //Copy the byte to the file
            //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, imgIn.getRowBytes()*height);
            imgIn.copyPixelsToBuffer(map);
            //recycle the source bitmap, this will be no longer used.
            imgIn.recycle();
            System.gc();// try to force the bytes from the imgIn to be released

            //Create a new bitmap to load the bitmap again. Probably the memory will be available.
            imgIn = Bitmap.createBitmap(width, height, type);
            map.position(0);
            //load it back from temporary
            imgIn.copyPixelsFromBuffer(map);
            //close the temporary file and channel , then delete that also
            channel.close();
            randomAccessFile.close();

            // delete the temp file
            file.delete();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imgIn;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


    String url_getWay="http://www.zaural-vodokanal.ru/php/rob/ForAndroid/get_way_get.php.php";
    String url_getPoint="http://www.zaural-vodokanal.ru/php/rob/ForAndroid/getPoint.php.php";
    JSONObject json = null;
    class Way{
        public boolean getWay(int idRobot, int idCityTo)    {
            try {
                List<NameValuePair> values = new ArrayList<NameValuePair>();
                values.add(new BasicNameValuePair("robot_id", String.valueOf(idRobot)));
                values.add(new BasicNameValuePair("city_id", String.valueOf(idCityTo)));
                json = JSONParser.makeHttpRequest(url_getWay, "GET", values);
                Thread.sleep(100);
                int success;
                success = json.getInt("success");
                if (success == 1) {
                    return true;
                } else{
                    return false;
                }
            } catch (InterruptedException e) {
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        public void getPoint(int idRobot){
            try {
                List<NameValuePair> values = new ArrayList<NameValuePair>();
                values.add(new BasicNameValuePair("robot_id", String.valueOf(idRobot)));
                json = JSONParser.makeHttpRequest(url_getPoint, "GET", values);
                int x;
                int y;
                x = json.getInt("x");
                y = json.getInt("y");
                //TODO: можно отрисовывать прямо здесь
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}

