package dkg.game.life;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import java.util.*;

public class GameOfLife extends Thread {
    private final int CELL_SIZE = 20;
    private final int num_cols;
    private final int num_rows;

    private Paint emptyCell;
    private Paint liveCellPaint;

    private boolean[][] lifeGrid;
    private int offsetX;
    private int offsetY;

    private final SurfaceHolder holder;
    private boolean running = true;

    public GameOfLife(SurfaceHolder holder, final int world_size_x, final int world_size_y,
                      double initPercent) {
        this.holder = holder;

        this.num_cols = world_size_x / CELL_SIZE;
        this.num_rows = world_size_y / CELL_SIZE;
        this.offsetX = world_size_x % CELL_SIZE;
        this.offsetY = world_size_y % CELL_SIZE;

        lifeGrid = new boolean[num_cols][num_rows];

        emptyCell = new Paint();
        emptyCell.setStyle(Paint.Style.STROKE);
        emptyCell.setColor(Color.BLACK);

        Random r = new Random();

        liveCellPaint = new Paint();
        liveCellPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        liveCellPaint.setColor(Color.GREEN);

        for(int i = 0; i < num_cols; i++){
            for (int j = 0; j < num_rows; j++){
                lifeGrid[i][j] = r.nextDouble() < initPercent;
            }
        }
    }

    @Override
    public void run() {
        while(running) {
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();
                synchronized (holder) {
                    liveOneGeneration();
                    drawWorld(canvas);
                }
            }
            finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            try {
                sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRunning(boolean b) {
        running = b;
    }

    private void drawWorld(Canvas can) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        can.drawPaint(paint);

        for (int i = 0; i < lifeGrid.length; i++){
            for (int j = 0; j < lifeGrid[i].length; j++) {
                int left = i * CELL_SIZE + (offsetX + 1) / 2;
                int top = j * CELL_SIZE + (offsetY + 1) / 2;

                Rect currRect = new Rect(left, top,
                                         left + CELL_SIZE, top + CELL_SIZE);

                if (lifeGrid[i][j]) {
                    can.drawRect(currRect, liveCellPaint);
                }
                can.drawRect(currRect, emptyCell);

            }
        }
    }

    private void liveOneGeneration() {
        for(int i = 0; i < num_cols; i++){
            for (int j = 0; j < num_rows; j++){
                lifeGrid[i][j] = liveOneGeneration(i, j);
            }
        }
    }

    private boolean liveOneGeneration(int i, int j) {
        int num_living = 0;
        Boolean isAlive = lifeGrid[i][j];

        for (int i_adj = i - 1; i_adj <= i + 1; i_adj++) {
            for (int j_adj = j - 1; j_adj <= j + 1; j_adj++) {
                if (i_adj == i && j_adj == j)
                    continue;

                if (i_adj > -1 && i_adj < num_cols &&
                    j_adj > -1 && j_adj < num_rows) {
                    num_living += lifeGrid[i_adj][j_adj] ? 1 : 0;
                }
            }
        }

        return (isAlive && num_living <= 3 && num_living >= 2) ||
                (!isAlive && num_living == 3);

    }
}
