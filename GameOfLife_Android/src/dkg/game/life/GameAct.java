package dkg.game.life;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

import java.util.logging.Logger;

public class GameAct extends Activity {
    /** Called when the activity is first created. */

//    private GameOfLife theGame;
//    private Thread gameThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new LifeView(this));

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);


    }

    public class LifeView extends SurfaceView implements SurfaceHolder.Callback {
        private final Logger l = Logger.getLogger(this.getClass().getName());
        private SurfaceHolder holder;
        private GameOfLife game;


        public LifeView(Context context) {
            super(context);
            this.holder = getHolder();

            if (holder != null) {
                holder.addCallback(this);
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }


        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            l.info("Created");
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            game = new GameOfLife(holder, size.x, size.y, .1);
            game.setRunning(true);
            game.start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            l.info("Destroyed");
            boolean retry = true;
            game.setRunning(false);
            while (retry) {
                try {
                    game.join();
                    retry = false;
                } catch (InterruptedException e) {
                }
            }
        }
    }
}