package ga.winterhills.findroid;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MapDrawView extends SurfaceView implements SurfaceHolder.Callback {
    private MapDrawThread mapDrawThread;

    Context context;

    public MapDrawView(Context context) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mapDrawThread = new MapDrawThread(getHolder(), getResources(), context);
        mapDrawThread.setRunning(true);
        mapDrawThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        // завершаем работу потока
        mapDrawThread.setRunning(false);
        while (retry) {
            try {
                mapDrawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // если не получилось, то будем пытаться еще и еще
            }
        }
    }
}

