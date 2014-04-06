package dkg.game.life;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.*;
import java.util.logging.Logger;

public class GameOfLife {
    private final int CELL_SIZE = 5;
    private final int num_cols;
    private final int num_rows;

    private Paint emptyCell;
    private Paint liveCellPaint;

    private boolean[][] lifeGrid;
    private int offsetX;
    private int offsetY;


    public GameOfLife(final int world_size_x, final int world_size_y,
                      double initPercent) {
        Logger l = Logger.getLogger("GameOfLife");
        l.info(world_size_x + "");
        l.info(world_size_y + "");

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

    public void drawWorld(Canvas can) {
        for (int i = 0; i < lifeGrid.length; i++){
            for (int j = 0; j < lifeGrid[i].length; j++) {
                int left = i * CELL_SIZE + (offsetX + 1) / 2;
                int top = j * CELL_SIZE + (offsetY + 1) / 2;

                Rect currRect = new Rect(left, top,
                                         left + CELL_SIZE, top + CELL_SIZE);

                can.drawRect(currRect, emptyCell);
                if (lifeGrid[i][j]) {
                    can.drawRect(currRect, liveCellPaint);
                }
            }
        }
    }

    public void liveOneGeneration() {
        for(int i = 0; i < num_cols; i++){
            for (int j = 0; j < num_rows; j++){
                lifeGrid[i][j] = liveOneGeneration(i, j);
            }
        }
    }

    public boolean liveOneGeneration(int i, int j) {
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
