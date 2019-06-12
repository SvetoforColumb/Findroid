package ga.winterhills.findroid;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;

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
        block_map.put(1110,map_block_1110);
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
        block_map.put(1111,map_block_1111m);

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

//        map_block_0000m.setHeight(blocksize);
//        map_block_0000m.setWidth(blocksize);
//        map_block_0001m.setHeight(blocksize);
//        map_block_0001m.setWidth(blocksize);
//        map_block_0010m.setHeight(blocksize);
//        map_block_0010m.setWidth(blocksize);
//        map_block_0011m.setHeight(blocksize);
//        map_block_0011m.setWidth(blocksize);
//        map_block_0100m.setHeight(blocksize);
//        map_block_0100m.setWidth(blocksize);
//        map_block_0101m.setHeight(blocksize);
//        map_block_0101m.setWidth(blocksize);
//        map_block_0110m.setHeight(blocksize);
//        map_block_0110m.setWidth(blocksize);
//        map_block_0111m.setHeight(blocksize);
//        map_block_0111m.setWidth(blocksize);
//        map_block_1000m.setHeight(blocksize);
//        map_block_1000m.setWidth(blocksize);
//        map_block_1001m.setHeight(blocksize);
//        map_block_1001m.setWidth(blocksize);
//        map_block_1010m.setHeight(blocksize);
//        map_block_1010m.setWidth(blocksize);
//        map_block_1011m.setHeight(blocksize);
//        map_block_1011m.setWidth(blocksize);
//        map_block_1100m.setHeight(blocksize);
//        map_block_1100m.setWidth(blocksize);
//        map_block_1101m.setHeight(blocksize);
//        map_block_1101m.setWidth(blocksize);
//        map_block_1110m.setHeight(blocksize);
//        map_block_1110m.setWidth(blocksize);
//        map_block_1111m.setHeight(blocksize);
//        map_block_1111m.setWidth(blocksize);
        block_map.put(0,map_block_0000);
        block_map.put(1,map_block_0001);
        block_map.put(10,map_block_0010);
        block_map.put(11,map_block_0011);
        block_map.put(100,map_block_0100);
        block_map.put(101,map_block_0101);
        block_map.put(110,map_block_0110);
        block_map.put(111,map_block_0111);
        block_map.put(1000,map_block_1000);
        block_map.put(1001,map_block_1001);
        block_map.put(1010,map_block_1010);
        block_map.put(1011,map_block_1011);
        block_map.put(1100,map_block_1100);
        block_map.put(1101,map_block_1101);
        block_map.put(1110,map_block_1110);
        block_map.put(1111,map_block_1111);
        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                x[i*10 + j] = i * blocksize;
                y[i*10 + j] = 85 + (j * blocksize);
            }
        }
        for (int i= 0; i <100; i++){
           // canvas.drawBitmap(block_map.get(mapData.Map[i].block), x[i] , y[i], null);
            canvas.drawBitmap(map_block_1100, x[i] , y[i], null);
        }





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
}

